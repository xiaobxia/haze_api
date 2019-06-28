package com.vxianjin.gringotts.pay.controller.chanpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseConstant;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseParameter;
import com.vxianjin.gringotts.pay.common.util.chanpay.ChanPayUtil;
import com.vxianjin.gringotts.pay.component.ChanpayService;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.component.UserInfoComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPBindCardConfirmReq;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.FuiouCardService;
import com.vxianjin.gringotts.pay.service.chanpay.ChanpayCardService;
import com.vxianjin.gringotts.util.ArrayUtil;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.controller.BaseController;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.pojo.BankAllInfo;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserBankService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 畅捷通支付-绑卡处理类
 *
 * @author wison 2017-12-07 13:33:02
 */
@Controller
@RequestMapping(value = "/chanpayBindCard")
public class ChanpayBindCardController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ChanpayBindCardController.class);

    @Resource
    private IUserService        userService;
    @Resource
    private IUserBankService    userBankService;
    @Resource
    private IBorrowOrderService borrowOrderService;
    @Resource
    private ChanpayService      chanpayService;
    @Resource
    private IBorrowOrderDao     borrowOrderDao;
    @Resource
    private UserInfoComponent   userInfoComponent;
    @Resource
    private ChanpayCardService  chanpayCardService;
    @Resource
    private OrderLogComponent   orderLogComponent;
    /**
     * 当天失败认证次数
     */
    private final static String CHANPAY_BIND_FAIL_COUNT = "CHANPAY_BIND_FAIL_COUNT_";
    /**
     * 生成手机认证码频繁标识key
     */
    private final static String CHANPAY_SMS_CODE = "CHANPAY_SMS_CODE_";


    /**
     * 进入银行卡页面
     */
    @RequestMapping(value = "credit-card/firstUserBank")
    public String firstUserBank(HttpServletRequest request, Model model) {
        try {
            User logUser = this.loginFrontUserByDeiceId(request);

            //调整业务顺序，避免非登陆用户也要去查一次银行卡列表，查询数据库
            if (logUser == null || StringUtils.isBlank(logUser.getId())) {
                logger.info("ChanpayBindCardController firstUserBank 登录已失效,请重新登录");
                model.addAttribute("msg", "登录已失效,请重新登录");
                model.addAttribute("code", ResponseStatus.FAILD.getName());
            } else {
                logger.info("ChanpayBindCardController firstUserBank userId="+logUser.getId());
                String deviceId = request.getParameter("deviceId");
                String mobilePhone = request.getParameter("mobilePhone");
                //查询所有的银行
                List<BankAllInfo> mapList = userBankService.findAllBankInfos();
                model.addAttribute("bankList", mapList);

                Map<Integer, BankAllInfo> bankAllMap = new HashMap<>();
                for (BankAllInfo bankAllInfo : mapList) {
                    bankAllMap.put(bankAllInfo.getBankId(), bankAllInfo);
                }
                model.addAttribute("mobilePhone", mobilePhone);
                model.addAttribute("deviceId", deviceId);

                //该用户银行卡列表
                List<UserCardInfo> userBankCardList = userService
                    .findUserBankCardList(Integer.parseInt(logUser.getId()));
                if (!ArrayUtil.isEmpty(userBankCardList)) {
                    List<JSONObject> jsonList = new ArrayList<>();
                    List<JSONObject> jsonListCopy = new ArrayList<>();
                    for (UserCardInfo usercard : userBankCardList) {
                        BankAllInfo bankAllInfo = bankAllMap.get(usercard.getBank_id());
                        String cardNo = usercard.getCard_no();
                        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(usercard));
                        jsonObject.put("bankLogoImg1", bankAllInfo.getBankLogoImg1());
                        jsonObject.put("bankLogoImg2", bankAllInfo.getBankLogoImg2());
                        jsonObject.put("bankCardNo", cardNo.substring(0, 4) + " **** **** "
                            .concat(cardNo.substring(cardNo.length() - 4, cardNo.length())));
                        jsonObject.put("open_bank", bankAllInfo.getOpenBank());
                        if (1 == usercard.getCardDefault()) {
                            jsonListCopy.add(jsonObject);
                        } else {
                            jsonList.add(jsonObject);
                        }
                    }
                    jsonListCopy.addAll(jsonList);
                    model.addAttribute("userBankCardcount", jsonListCopy.size());
                    model.addAttribute("userBankCardList", jsonListCopy);
                    model.addAttribute("code", ResponseStatus.SUCCESS.getName());
                }
            }
        } catch (Exception e) {
            logger.error("firstUserBank error", e);
        }
        return "userinfo/userCardList";
    }

    /**
     * 是否超过十张绑卡
     */
    @RequestMapping(value = "credit-card/addCardOrNot")
    public void addUserCardOrNot(HttpServletRequest request, HttpServletResponse response) {
        User logUser = this.loginFrontUserByDeiceId(request);
        Map<String, Object> map = new HashMap<>();
        String code = "0";
        map.put("code", code);
        map.put("msg", "");
        if (logUser != null) {
            List<UserCardInfo> userBankCardList = userService
                .findUserBankCardList(Integer.parseInt(logUser.getId()));
            if (ArrayUtil.isNotEmpty(userBankCardList)) {
                if (10 <= userBankCardList.size()) {
                    code = "1";
                    map.put("code", code);
                    map.put("msg", "当前绑卡超过十张或等于十张，无法继续绑卡!");
                }
            }
        } else {
            code = "2";
            map.put("code", code);
            map.put("msg", "登录已失效,请重新登录!");
        }
        JSONUtil.toObjectJson(response, JSONUtil.beanToJson(map));
    }
    /**
     * 绑定银行卡
     */
    @RequestMapping(value = "credit-card/bindNewCard")
    public String bindNewCard(HttpServletRequest request,Model model) {
        User logUser = this.loginFrontUserByDeiceId(request);
        String code = ResponseStatus.FAILD.getName();
        if (logUser != null) {
            //查询所有的银行
            List<Map<String, Object>> mapList = userBankService.findAllBankInfo();
            model.addAttribute("bankList", mapList);
            User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
            //真实姓名
            model.addAttribute("realName", user.getRealname());
            //身份证号
            model.addAttribute("Idcard", user.getIdNumber());
            model.addAttribute("deviceId", request.getParameter("deviceId"));
            model.addAttribute("mobilePhone", request.getParameter("mobilePhone"));
            List<UserCardInfo> userBankCardList = userService
                    .findUserBankCardList(Integer.parseInt(logUser.getId()));
            code = ResponseStatus.SUCCESS.getName();
            if (ArrayUtil.isNotEmpty(userBankCardList) && userBankCardList.size() >= 10) {
                code = ResponseStatus.FAILD.getName();
                model.addAttribute("msg", "最多绑定十张卡");
            }
        } else {
            model.addAttribute("msg", "登录已失效,请重新登录");
        }
        model.addAttribute("code", code);
        model.addAttribute("bankName", "请选择银行");
        //到添加银行卡页面
        return "userinfo/chanpay/firstUserBank";
    }

    /**
     * 显示默认银行卡信息
     */
    @RequestMapping(value = "credit-card/showUserBankCardInfo")
    public String showUserBankCardInfo(HttpServletRequest request, HttpServletResponse response,
                                       Model model, Integer bankId) throws Exception {
        User logUser = this.loginFrontUserByDeiceId(request);
        if (logUser != null){
            UserCardInfo info = userService.findBankCardByCardId(bankId);
            if (info != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                String deviceId = request.getParameter("deviceId");
                String mobilePhone = request.getParameter("mobilePhone");
                model.addAttribute("deviceId", deviceId);
                model.addAttribute("mobilePhone", mobilePhone);
                //真实姓名
                model.addAttribute("realName", user.getRealname());
                //身份证号
                model.addAttribute("Idcard", user.getIdNumber());
                model.addAttribute("cardId", info.getId());
                model.addAttribute("bankId", info.getBank_id());
                model.addAttribute("bankName", info.getBankName());
                model.addAttribute("bankCard", info.getCard_no());
                model.addAttribute("userPhone", info.getPhone());
                //银行开户姓名
                model.addAttribute("openName", info.getOpenName());
                model.addAttribute("code", ResponseStatus.SUCCESS.getName());
            } else {
                model.addAttribute("msg", "银行卡不存在");
                model.addAttribute("code", ResponseStatus.FAILD.getName());
            }
        }else{
            model.addAttribute("msg", ResponseStatus.LOGIN.getValue());
            model.addAttribute("code", ResponseStatus.LOGIN.getName());
        }
        //到展示银行卡页面
        return "userinfo/chanpay/showUserBank";
    }

    /**
     * 进入银行卡页面
     */
    @RequestMapping(value = "credit-card/findUserBindCards4Repayment")
    public void findUserBindCards4Repayment(HttpServletResponse response, Integer borrowId) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(borrowId);
        if (bo != null) {
            //该用户银行卡列表
            List<UserCardInfo> userBankCardList = userService.findUserBankCardList(bo.getUserId());
            Gson gson = new Gson();
            String json = gson.toJson(userBankCardList);
            JSONUtil.toObjectJson(response, json);
        }
    }

    /**
     * 重新绑定银行卡
     */
    @RequestMapping(value = "credit-card/updateUserBank")
    public String updateUserBank(HttpServletRequest request,Model model) {
        User logUser = this.loginFrontUserByDeiceId(request);
        String deviceId = request.getParameter("deviceId");
        String mobilePhone = request.getParameter("mobilePhone");
        logger.info("ChanpayBindCardController firstUserBank userId="
                    + (logUser != null ? logUser.getId() : "null"));
        //查询所有的银行
        List<Map<String, Object>> mapList = userBankService.findAllBankInfo();
        if (logUser != null) {
            User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
            model.addAttribute("bankList", mapList);
            model.addAttribute("bankName", "请选择银行");
            //真实姓名
            model.addAttribute("realName", user.getRealname());
            //身份证号
            model.addAttribute("Idcard", user.getIdNumber());
            model.addAttribute("mobilePhone", mobilePhone);
            model.addAttribute("deviceId", deviceId);
        }else{
            model.addAttribute("msg", ResponseStatus.LOGIN.getValue());
            model.addAttribute("code", ResponseStatus.LOGIN.getName());
        }
        return "userinfo/chanpay/firstUserBank";
    }



    /**
     * 切换默认银行卡
     */
    @RequestMapping(value = "credit-card/switchDefaultCard")
    public void switchDefaultCard(HttpServletRequest request, HttpServletResponse response) {
        User logUser = this.loginFrontUserByDeiceId(request);
        String code = ResponseStatus.FAILD.getName();
        Map<String, String> map = new HashMap<>();
        if (logUser == null) {
            map.put("code", code);
            map.put("msg", "登录已失效,请重新登录");
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(map));
            return;
        }

        String bankId = request.getParameter("bankId");
        Integer checkResult = borrowOrderService.checkBorrow(Integer.parseInt(logUser.getId()));
        String msg = "";
        if (1 == checkResult) {
            msg = "您有借款申请正在审核或未还款完成，暂不能修改银行卡";
            map.put("code", code);
            map.put("msg", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(map));
            return;
        }
        logger.info("ChanpayBindCardController switchDefaultCard userId=" + logUser.getId()
                    + " checkResult=" + checkResult);


        if (StringUtils.isEmpty(bankId)) {
            msg = "你要绑定哪张卡";
        } else {
            UserCardInfo userBankCard = userService
                    .findUserBankCard(Integer.valueOf(logUser.getId()));
            if (userBankCard != null) {
                //原来的默认卡切换成非默认
                userBankCard.setCardDefault(0);
                userBankService.updateUserBankCard(userBankCard);
            }
            UserCardInfo bankCardByCardId = userBankService
                    .findBankCardByCardId(Integer.valueOf(bankId));
            //选择的卡切换成默认卡
            bankCardByCardId.setCardDefault(1);
            userBankService.updateUserBankCard(bankCardByCardId);

            //查询当前放款失败接口订单
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", logUser.getId());
            paramMap.put("status", BorrowOrder.STATUS_FKSB);
            BorrowOrder hasBorrowOrder = borrowOrderDao.selectBorrowByParams(paramMap);
            logger.info("ChanpayBindCardController switchDefaultCard borrowOrder userId="
                    + logUser.getId() + " status="
                    + (hasBorrowOrder != null ? hasBorrowOrder.getStatus() : "null"));


            if (null != hasBorrowOrder
                    && BorrowOrder.STATUS_FKSB.equals(hasBorrowOrder.getStatus())) {

                //日志记录
                OrderLogModel logModel = new OrderLogModel();
                logModel.setUserId(logUser.getId());
                logModel.setOperateType(OperateType.BORROW.getCode());
                logModel.setBorrowId(String.valueOf(hasBorrowOrder.getId()));
                logModel.setOperateType(OperateType.BORROW.getCode());
                logModel.setBeforeStatus(String.valueOf(BorrowOrder.STATUS_FKSB));
                logModel.setAfterStatus(String.valueOf(BorrowOrder.STATUS_FKSB));
                logModel.setRemark("可重新放款");

                BorrowOrder updateBorrowOrder = new BorrowOrder();
                updateBorrowOrder.setId(hasBorrowOrder.getId());
                updateBorrowOrder.setCardNo(bankCardByCardId.getCard_no());
                updateBorrowOrder.setPayRemark("可重新放款");
                borrowOrderService.updateById(updateBorrowOrder);
                logger.info(
                        "ChanpayBindCardController switchDefaultCard borrowOrder success userId="
                                + logUser.getId());

                orderLogComponent.addNewOrderLog(logModel);
            }
            logger.info(
                    "ChanpayBindCardController switchDefaultCard success userId=" + logUser.getId());

            msg = "切换成功";
            code = ResponseStatus.SUCCESS.getName();
        }
        map.put("msg", msg);
        map.put("code", code);
        JSONUtil.toObjectJson(response, JSONUtil.beanToJson(map));
    }

    /**
     * 重新绑卡时，校验是否有借款订单
     */
    @RequestMapping(value = "credit-card/validBorrowOrder")
    public void validBorrowOrder(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retMap = new HashMap<>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "绑定失败";
        User logUser = null;
        try {
            logUser = this.loginFrontUserByDeiceId(request);
            if (null == logUser) {
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
                return;
            }
            logger.info("ChanpayBindCardController validBorrowOrder userId=" + logUser.getId()
                        + " user is not null");
            User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
            if (null == user) {
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
                return;
            }
            logger.info("ChanpayBindCardController validBorrowOrder userId=" + logUser.getId()
                        + " user is not null userPhone=" + user.getUserPhone());
            Integer checkResult = borrowOrderService.checkBorrow(Integer.parseInt(logUser.getId()));
            /*   if (1 == checkResult) {
                msg = "您有借款申请正在审核或未还款完成，暂不能修改银行卡。";
                return;
            }*/
            logger.info("ChanpayBindCardController validBorrowOrder userId=" + logUser.getId()
                        + " checkResult=" + checkResult);
            code = ResponseStatus.SUCCESS.getName();
            msg = ResponseStatus.SUCCESS.getValue();
        } catch (Exception e) {
            logger.error("ChanpayBindCardController validBorrowOrder userId="
                         + (null != logUser ? logUser.getId() : "") + " error",
                e);
        } finally {
            retMap.put("code", code);
            retMap.put("message", msg);
            logger.info("ChanpayBindCardController validBorrowOrder userId="
                        + (null != logUser ? logUser.getId() : "") + " message=" + msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(retMap));
        }
    }

    /**
     * 绑卡请求
     */
    @RequestMapping(value = "credit-card/userBankRequest")
    public void userBankRequest(HttpServletRequest request, HttpServletResponse response) {
        //用户手机号
        String telephone = request.getParameter("mobilePhone");
        Map<String, Object> retMap = new HashMap<>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "操作异常，请重试";
        User logUser = null;
        try {
            logUser = this.loginFrontUserByDeiceId(request);
            if (null == logUser) {
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
                return;
            }
            logger.info("ChanpayBindCardController userBankRequest userId=" + logUser.getId()
                        + " user is not null");

            User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
            if (null == user) {
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
                return;
            }
            logger.info("ChanpayBindCardController userBankRequest userId=" + logUser.getId()
                        + " user is not null userPhone=" + user.getUserPhone());

            if ((StringUtils.isBlank(user.getRealname()))
                || (!"1".equals(user.getRealnameStatus()))) {
                code = ResponseStatus.FAILD.getName();
                msg = "请填写个人信息";
                return;
            }
            logger.info(
                "ChanpayBindCardController userBankRequest userId=" + logUser.getId() + " 个人信息验证通过");

            Map<String, String> pams = this.getParameters(request);
            //预留手机号
            String phone = pams.get("phone") == null ? "" : pams.get("phone");
            //银行编号
            String bankId = pams.get("bank_id") == null ? "" : pams.get("bank_id");
            //银行卡号
            String cardNo = pams.get("card_no") == null ? "" : pams.get("card_no");

            logger.info("ChanpayBindCardController userBankRequest userId=" + logUser.getId()
                        + " phone:" + phone + ",bankId:" + bankId + ",cardNo:" + cardNo);
            if (StringUtils.isBlank(phone)) {
                code = ResponseStatus.FAILD.getName();
                msg = "预留手机号码不能为空";
                return;
            }
            if (StringUtils.isBlank(bankId)) {
                code = ResponseStatus.FAILD.getName();
                msg = "银行不能为空";
                return;
            }
            Map<String, String> bankAllInfo = userBankService
                .selectBankByPrimaryKey(Integer.parseInt(bankId));

            if (StringUtils.isBlank(cardNo)) {
                code = ResponseStatus.FAILD.getName();
                msg = "银行卡号不能为空";
                return;
            }

            HashMap<String, Object> bankMap = new HashMap(){{
                put("userId", Integer.parseInt(user.getId()));
            }};
            List<String> cardListr = userBankService.findUserCardByUserId(bankMap).stream().map(userCardInfo -> userCardInfo.getCard_no()).collect(Collectors.toList());
            if (cardListr.contains(cardNo)) {
                code = ResponseStatus.FAILD.getName();
                msg = "银行卡号已存在";
                return;
            }

            if (null != bankAllInfo
                && BankAllInfo.BANK_STATUS_NO.equals(bankAllInfo.get("bankStatus"))) {
                code = ResponseStatus.FAILD.getName();
                msg = "暂时不支持" + bankAllInfo.get("bankName") + "的绑定更换业务";
                return;
            }

            //生成绑卡请求号（唯一，且整个绑卡过程中保持不变）
            String TrxId = GenerateNo.nextOrdId();
            Map<String, String> paramMap = BaseParameter.requestBaseParameter(BaseParameter.NMG_BIZ_API_AUTH_REQ);
            paramMap.put("TrxId", TrxId);
            paramMap.put("ExpiredTime", "10m");
            //用户ID
            paramMap.put("MerUserId", PropertiesConfigUtil.get("RISK_BUSINESS") + user.getId());
            paramMap.put("BkAcctTp", "01");
            //银行卡号
            paramMap.put("BkAcctNo", ChanPayUtil.encrypt(cardNo.trim(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            //银行卡用户姓名
            paramMap.put("CstmrNm", ChanPayUtil.encrypt(user.getRealname(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            paramMap.put("IDTp", "01");
            //银行卡用户身份证号
            paramMap.put("IDNo", ChanPayUtil.encrypt(user.getIdNumber(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            //银行卡预留手机号
            paramMap.put("MobNo", ChanPayUtil.encrypt(phone.trim(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            paramMap.put("SmsFlag", "1");

            //发送银行卡绑定请求
            Map<String, Object> resultMap = chanpayService.getBindCardRequest(paramMap);
            logger.info("ChanpayBindCardController userBankRequest userId=" + logUser.getId()
                        + " resultMap=" + JSON.toJSONString(resultMap));
            if ("0000".equals(String.valueOf(resultMap.get("code")))) {

                code = ResponseStatus.SUCCESS.getName();
                msg = ResponseStatus.SUCCESS.getValue();
                //短信冷却时间
                retMap.put("time", 60);
                retMap.put("requestNo", TrxId);
                //存入redis里，避免发送过于频繁
                checkForFront(CHANPAY_SMS_CODE, TrxId + telephone, 60);
            } else {
                code = "2";
                msg = resultMap.get("msg") + "";
            }

        } catch (Exception e) {
            logger.error("ChanpayBindCardController userBankRequest userId="
                         + (null != logUser ? logUser.getId() : "") + " error",
                e);
        } finally {
            retMap.put("code", code);
            retMap.put("message", msg);
            logger.info("ChanpayBindCardController userBankRequest userId="
                        + (null != logUser ? logUser.getId() : "") + " message=" + msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(retMap));
        }
    }

    /**
     * 绑卡确认
     */
    @PostMapping(value = "credit-card/userBankConfirm")
    public void userBankConfirm(HttpServletRequest request, HttpServletResponse response) {
        ResultModel<User> userInfoCheckResult = new ResultModel(false,
            ResponseStatus.FAILD.getName(), "绑定失败");

        //获取当前用户
        User logUser = this.loginFrontUserByDeiceId(request);

        if (null == logUser) {
            logger.info("ChanpayBindCardController userBankConfirm userId=" + logUser.getId()
                        + " user is not null");
            userInfoCheckResult.setCode(ResponseStatus.LOGIN.getName());
            userInfoCheckResult.setMessage(ResponseStatus.LOGIN.getValue());
            JSONUtil.toObjectJson(response, JSON.toJSONString(userInfoCheckResult));
            return;
        }

        //【1】验证用户个人信息
        userInfoCheckResult = userInfoComponent.userInfoConfirm(logUser.getId());

        if (!userInfoCheckResult.isSucc()) {
            JSONUtil.toObjectJson(response, JSON.toJSONString(userInfoCheckResult));
            return;
        }
        User user = userInfoCheckResult.getData();

        //【2】请求参数验证
        Map<String, String> pams = this.getParameters(request);

        ResultModel paramsCheck = userBankConfirmReqCheck(pams, response, user.getId());

        if (!paramsCheck.isSucc()) {
            JSONUtil.toObjectJson(response, JSON.toJSONString(paramsCheck));
            return;
        }

        String phone = pams.get("phone") == null ? "" : pams.get("phone");
        String bankId = pams.get("bank_id") == null ? "" : pams.get("bank_id");
        String cardNo = pams.get("card_no") == null ? "" : pams.get("card_no");
        //绑卡请求编号
        String requestNo = pams.get("request_no") == null ? "" : pams.get("request_no");
        //短信验证码
        String smsCode = pams.get("sms_code") == null ? "" : pams.get("sms_code");

        logger.info("ChanpayBindCardController userBankConfirm userId=" + logUser.getId() + " phone:"
                    + phone + ",bankId:" + bankId + ",cardNo:" + cardNo + ",requestNo:" + requestNo
                    + ",smsCode:" + smsCode);

        try {
            //封装model
            YPBindCardConfirmReq bindCardConfirmReq = new YPBindCardConfirmReq();

            final String orderNo = GenerateNo.nextOrdId();
            bindCardConfirmReq.setRequestNo(requestNo.trim());
            bindCardConfirmReq.setSmsCode(smsCode.trim());
            bindCardConfirmReq.setUserId(logUser.getId());
            bindCardConfirmReq.setBankId(bankId);
            getBindCardConfirmDataMap(bindCardConfirmReq);
            bindCardConfirmReq.setOrderNo(orderNo);
            bindCardConfirmReq.setPhone(phone);
            bindCardConfirmReq.setCardNo(cardNo);
            bindCardConfirmReq.setUserName(user.getRealname());

            //【3】用户绑卡
            ResultModel<String> result = chanpayCardService.userBankConfirm(bindCardConfirmReq);
            JSONUtil.toObjectJson(response, JSON.toJSONString(result));
        } catch (Exception e) {
            logger.error("ChanpayBindCardController userBankRequest userId="
                            + (null != logUser ? logUser.getId() : "") + " error",
                    e);
        } finally {
            logger.info("ChanpayBindCardController userBankRequest userId="
                    + (null != logUser ? logUser.getId() : ""));
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(null));
        }
    }

    private ResultModel userBankConfirmReqCheck(Map<String, String> pams,
                                                HttpServletResponse response, String userId) {

        ResultModel resultModel = new ResultModel(false);

        String phone = pams.get("phone") == null ? "" : pams.get("phone");
        String bankId = pams.get("bank_id") == null ? "" : pams.get("bank_id");
        String cardNo = pams.get("card_no") == null ? "" : pams.get("card_no");
        //绑卡请求编号
        String requestNo = pams.get("request_no") == null ? "" : pams.get("request_no");
        //短信验证码
        String smsCode = pams.get("sms_code") == null ? "" : pams.get("sms_code");

        if (StringUtils.isBlank(bankId)) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("银行不能为空");
            return resultModel;
        }

        if (StringUtils.isBlank(phone)) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("预留手机号码不能为空");
            return resultModel;
        }

        if (StringUtils.isBlank(cardNo)) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("银行卡号不能为空");
            return resultModel;
        }

        if (StringUtils.isBlank(smsCode)) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("验证码不能为空");
            return resultModel;
        }

        if (StringUtils.isBlank(requestNo)) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("请求参数异常，请确认后重试");
            return resultModel;
        }

        HashMap<String, Object> bankMap = new HashMap(){{
            put("userId", Integer.parseInt(userId));
        }};
        List<String> cardListr = userBankService.findUserCardByUserId(bankMap).stream().map(userCardInfo -> userCardInfo.getCard_no()).collect(Collectors.toList());
        if (cardListr.contains(cardNo)) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("银行卡号已存在");
            return resultModel;
        }

        Map<String, String> bankAllInfo = userBankService
            .selectBankByPrimaryKey(Integer.parseInt(bankId));

        if (null != bankAllInfo
            && BankAllInfo.BANK_STATUS_NO.equals(bankAllInfo.get("bankStatus"))) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("暂时不支持" + bankAllInfo.get("bankName") + "的绑定更换业务");
            return resultModel;
        }

        resultModel.setSucc(true);
        return resultModel;
    }

    private void getBindCardConfirmDataMap(YPBindCardConfirmReq bindCardConfirmReq) {
        //商户编号
        String merchantNo = FuiouConstants.API_MCHNT_CD;
        String requestNo = bindCardConfirmReq.getRequestNo();
        String smsCode = bindCardConfirmReq.getSmsCode();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        dataMap.put("validatecode", smsCode);
        bindCardConfirmReq.setDataMap(dataMap);
    }

}
