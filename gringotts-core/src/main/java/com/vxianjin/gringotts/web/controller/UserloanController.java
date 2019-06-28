package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.ThreadPool;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.dao.UserQuotaSnapshotDao;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.pay.model.UserQuotaModel;
import com.vxianjin.gringotts.util.NumberToCN;
import com.vxianjin.gringotts.util.date.CalendarUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.common.result.Status;
import com.vxianjin.gringotts.web.dao.IIndexDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IAutoRiskService;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.ICfcaSignAndViewService;
import com.vxianjin.gringotts.web.service.impl.BackConfigParamsService;
import com.vxianjin.gringotts.web.service.impl.UserService;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import com.vxianjin.gringotts.web.utils.RequestUtils;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户借款记录
 *
 * @author 2016年12月9日 16:51:09
 */
@Controller
@RequestMapping("credit-loan/")
public class UserloanController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserloanController.class);
    @Resource
    JedisCluster jedisCluster;
    /**
     * 云法通支付令业务
     */
    @Resource
    ICfcaSignAndViewService cfcaSignAndViewService;
    @Resource
    private IBorrowOrderService borrowOrderService;
    @Resource
    private UserService userService;
    @Resource
    private IIndexDao indexDao;

    @Resource
    private IAutoRiskService autoRiskService;

    @Resource
    private BorrowProductConfigDao borrowProductConfigDao;

    @Resource
    private UserQuotaSnapshotDao userQuotaSnapshotDao;
    @Resource
    private BackConfigParamsService backConfigParamsService;
    /**
     * H5 借款说明
     */
    @RequestMapping("description")
    public String borrowDescription() {
        return "borrow/borrowDescription";
    }

    public ResponseContent allowBorrowV2(String userId,String period,Integer money, Integer productId){
        ResponseContent serviceResult = new ResponseContent("500", "未知异常");
        if (money == null || StringUtils.isBlank(period)) {
            serviceResult.setMsg("参数错误");
            return serviceResult;
        }

        User user = userService.searchByUserid(Integer.valueOf(userId));

        //【1】个人信息校验
        try {
            checkUserBorrowMoney(user,period,money);
        }catch (PayException e){
            serviceResult.setMsg(e.getMessage());
            return serviceResult;
        }


        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put("userId",userId);
        queryMap.put("borrowAmount",String.valueOf(money * 100));
        queryMap.put("borrowDay",period);
        queryMap.put("borrowProductId",String.valueOf(productId));

        //【2】判断该产品线是否存在
        BorrowProductConfig config;
        if (productId == null) {
            config= borrowProductConfigDao.queryByBorrowDayAndAmount(new BigDecimal(queryMap.get("borrowAmount")),Integer.valueOf(period));
        } else {
            config= borrowProductConfigDao.selectByPrimaryKey(productId);
        }

        if (config == null){
            serviceResult.setMsg("该借款期限下无法借款");
            return serviceResult;
        }

        //【3】查询用户当前期限下最大可借金额
        UserQuotaModel quotaModel = userQuotaSnapshotDao.querUserQuota(queryMap);
        BigDecimal userMaxBorrowAmount;
        if (quotaModel == null) {
            BorrowProductConfig borrowProductConfig = borrowProductConfigDao.queryByBorrowByStatus(0);//0为默认
            /*if (period.equals("7") && money.compareTo(1600) == 0){
                userMaxBorrowAmount = new BigDecimal("1600");
                userQuotaSnapshotDao.addUserQuota(Integer.valueOf(userId),config.getId(),config.getBorrowAmount(),config.getBorrowDay());
            }else{
                serviceResult.setMsg("用户该借款期限下无可借金额");
                return serviceResult;
            }*/
            userMaxBorrowAmount = borrowProductConfig.getBorrowAmount().divide(BigDecimal.valueOf(100));
            try {
                userQuotaSnapshotDao.addUserQuota(Integer.valueOf(userId),config.getId(),config.getBorrowAmount(),config.getBorrowDay());
            } catch (DuplicateKeyException e) {
                logger.info("数据库中有该用户的数据，不做异常处理，执行更新，错误信息：{}", e.getMessage());
                userQuotaSnapshotDao.updateUserQuota(Integer.valueOf(userId), config.getId(), config.getBorrowDay(), config.getBorrowAmount());
            }
        }else{
            userMaxBorrowAmount =  quotaModel.getBorrowAmount().divide(new BigDecimal("100"));
        }


        /*int minAmount = Integer.parseInt(user.getAmountMin()) / 100;
        if (money < minAmount) {
            serviceResult.setMsg("您的最低借款额度为" + minAmount + "元。");
            return serviceResult;
        }

       if (money > userMaxBorrowAmount.intValue()) {
            serviceResult.setMsg("您的最高借款额度为" + userMaxBorrowAmount.intValue() + "元。");
            return serviceResult;
        }*/

        if (money != userMaxBorrowAmount.intValue()) {
            serviceResult.setMsg("您的最高借款额度为" + userMaxBorrowAmount.intValue() + "元，只能申请全额借款。");
            return serviceResult;
        }

        Map<String, String> interval = borrowOrderService.findAuditFailureOrderByUserId(user.getId());
        if ("-1".equals(interval.get("code"))) {
            serviceResult.setExt(interval.get("canLoan"));
            serviceResult.setMsg(interval.get("msg"));
        } else {
            serviceResult = new ResponseContent(ResponseContent.SUCCESS, "可以借款");
        }

        return serviceResult;
    }

    //    public ResponseContent allowBorrow(String userId,String period,Integer money) {
    //        ResponseContent serviceResult = new ResponseContent("500", "未知异常");
    //        if (money == null || StringUtils.isBlank(period)) {
    //            serviceResult.setMsg("参数错误");
    //            return serviceResult;
    //        }
    //
    //        User user = userService.searchByUserid(Integer.valueOf(userId));
    //
    //        //【1】个人信息校验
    //        try {
    //            checkUserBorrowMoney(user,period,money);
    //        }catch (PayException e){
    //            serviceResult.setMsg(e.getMessage());
    //            return serviceResult;
    //        }
    //        //【2】借款期限校验
    //        String currperiodIsopen = "0";// 默认关
    //        String currperiod = period;// 当前借款期限
    //        Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
    //        String periodTypes = keys.get("period_type_isopen");
    //        try {
    //            String periodTypesArr[] = periodTypes.split(";");
    //            for (String p : periodTypesArr) {
    //                String periods[] = p.split(":");
    //                if (currperiod.equals(periods[0])) {
    //                    currperiodIsopen = periods[1];
    //                    break;
    //                }
    //            }
    //        } catch (Exception e) {
    //            logger.error("judge periodsTypeIsOpen error", e);
    //            throw new PayException("judge periodsTypeIsOpen error");
    //        }
    //
    //        if (!"1".equals(currperiodIsopen)) {
    //            throw new PayException("暂不受理" + currperiod + "天借款服务");
    //        }
    //
    //        //【3】借款金额校验
    //        try {
    //            int minAmount = Integer.parseInt(user.getAmountMin()) / 100;
    //            if (minAmount <= 0) {
    //                serviceResult.setMsg("您的最低借款额度为0元，暂不能借款。");
    //                return serviceResult;
    //            } else if (money < minAmount) {
    //                serviceResult.setMsg("您的最低借款额度为" + minAmount + "元。");
    //                return serviceResult;
    //            }
    //
    //            int maxAmount = Integer.parseInt(user.getAmountMax()) / 100;
    //            if (maxAmount <= 0) {
    //                serviceResult.setMsg("您的最高借款额度为0元，暂不能借款。");
    //                return serviceResult;
    //            } else if (money > maxAmount) {
    //                serviceResult.setMsg("您的最高借款额度为" + maxAmount + "元。");
    //                return serviceResult;
    //            }
    //
    //            int amountAvailable = Integer.parseInt(user.getAmountAvailable()) / 100;
    //            if (amountAvailable <= 0) {
    //                serviceResult.setMsg("您的剩余可借额度为0元，暂不能借款。");
    //                return serviceResult;
    //            } else if (money > amountAvailable) {
    //                serviceResult.setMsg("您的剩余可借额度为" + amountAvailable + "元");
    //                return serviceResult;
    //            }
    //            // 系统最大额度
    //            Integer sysAmountMax = Integer.parseInt(keys.get("max_amount_sys")) / 100;
    //            if (money > sysAmountMax) {
    //                serviceResult.setMsg("您目前最高可借款额度不能超过" + sysAmountMax + "元。");
    //            } else {
    //                Map<String, String> interval = borrowOrderService
    //                        .findAuditFailureOrderByUserId(user.getId());
    //                if ("-1".equals(interval.get("code"))) {
    //                    serviceResult.setExt(interval.get("canLoan"));
    //                    serviceResult.setMsg(interval.get("msg"));
    //                } else {
    //                    serviceResult = new ResponseContent(ResponseContent.SUCCESS, "可以借款");
    //                }
    //            }
    //            return serviceResult;
    //        } catch (Exception e) {
    //            logger.error("allowBorrow error ", e);
    //            serviceResult.setMsg("allowBorrow error");
    //            return serviceResult;
    //        }
    //    }

    private void checkUserBorrowMoney(User user,String period,Integer money){
        String key = "xjx_borrow_confirm_loan"; // redis key

        if (!"2".equals(user.getNewFlag())) {
            throw new PayException("您当前为新用户，信用正在审核中，请您耐心等待");
        }
        jedisCluster.setex(key, 60, user.getId() + "");

        if (!"1".equals(user.getRealnameStatus())) {
            throw new PayException("请先实名认证");
        }

        if (StringUtils.isBlank(user.getFirstContactPhone())
            || StringUtils.isBlank(user.getSecondContactPhone())) {
            throw new PayException("请完善紧急联系人信息");
        }

        /*if ("1".equals(user.getZmStatus())) {
            throw new PayException("芝麻信用未认证");
        }*/

        if (!"2".equals(user.getTdStatus())) {
            if ("1".equals(user.getTdStatus())) {
                throw new PayException("手机运营商未认证");
            } else {
                throw new PayException("手机运营商正在认证中，请耐心等待");
            }
        }

        Integer checkResult = borrowOrderService.checkBorrow(Integer.parseInt(user.getId()));
        if (1 == checkResult) {
            throw new PayException("您有借款申请正在审核或未还款完成，暂不能借款。");
        }

        if (money < 10) {
            throw new PayException("您的借款金额不能低于10元。");
        }

    }


    //校验用户信息
    private User checkUserLoginMsg(HttpServletRequest request,boolean isDelKey){
        String key = "xjx_borrow_apply_loan"; // redis key

        User user = this.loginFrontUserByDeiceId(request);
        if (user == null || StringUtils.isBlank(user.getId())) {
            throw new PayException("请先登陆");
        }
        key = key + "_" + user.getId(); // 确定key唯一
        if (StringUtils.isNotBlank(jedisCluster.get(key))) {
            isDelKey = false;
            throw new PayException("您的借款申请正在处理中，请稍后再试");
        }
        return user;
    }

    //申请成功后
    private void confirLoanSucc(Integer userId,Integer productId, HttpServletRequest request,Integer money,Integer period,Map<String, Object> data,Map<String, Object> json){
        //查询当前用户的借款和期限对应的费率配置
        // 借款金额
        HashMap<String, String> query = new HashMap<>();
        query.put("borrowAmount",String.valueOf(money * 100));
        query.put("borrowDay",String.valueOf(period));

        BorrowProductConfig config;
        if (productId == null) {//灵活配置
            config = borrowProductConfigDao.selectByBorrowMoneyAndPeriod(query);
        } else {
            config =  borrowProductConfigDao.selectByPrimaryKey(productId);
        }


        if (config == null){
            logger.error(MessageFormat.format("产品线配置不存在，借款金额{0},期限{1}",money * 100,period));
            return ;
        }
        if (config.getTotalFeeRate() == null || config.getBorrowAmount() == null){
            logger.error(MessageFormat.format("产品线ID：{0}配置异常",config.getId()));
            return;
        }

        CfcaContractInfo contractInfo = new CfcaContractInfo();
        CfcaContractTemplate template = new CfcaContractTemplate();
        // 从数据库获取最新的user信息，因需要判断用户的可借额度
        User user = userService.searchByUserid(userId);
        // 修改用户设备号
        if (StringUtils.isBlank(user.getEquipmentNumber()) || "H5".equals(user.getEquipmentNumber())
                || user.getUserName().equals(user.getEquipmentNumber())) {
            User u = new User();
            String equipmentNumber = request.getParameter("deviceId");// 设备号
            if (StringUtils.isBlank(equipmentNumber)) {
                if (!StringUtils.isBlank(user.getUserName())) {
                    u.setId(user.getId());
                    u.setEquipmentNumber(user.getUserName());
                    userService.updateByPrimaryKeyUser(u);
                }
            } else {
                u.setId(user.getId());
                u.setEquipmentNumber(equipmentNumber);
                userService.updateByPrimaryKeyUser(u);
            }
        }
        HashMap<String, Object> param = new HashMap<String, Object>();
        // 查询用户的借款入账账户信息
        param.put("userId", user.getId()); //
        param.put("type", "2"); // 借记卡
        param.put("status", "1"); // 状态成功
        UserCardInfo cardInfo = userService.findUserBankCard(Integer.valueOf(user.getId().trim()));//查询有效的用户银行卡信息
        UserCardInfo info = new UserCardInfo();
        if (cardInfo != null) {
            info = cardInfo;
        }

        // 结算服务费
        double apr_fee = config.getTotalFeeRate().divide(new BigDecimal("100")).doubleValue();
        json.put("productId", productId);
        json.put("counter_fee", String.valueOf(apr_fee));
        json.put("true_money", String.valueOf(money - apr_fee));
        json.put("period",String.valueOf(period));
        json.put("money", String.valueOf(money));
        json.put("tips", "您将在" + period+ "天后还款" + money + "元");
        json.put("bank_name", info.getBankName() == null ? "" : info.getBankName());
        json.put("card_no", info.getCard_no() == null ? "" : info.getCard_no());
        if (info.getCard_no() != null) {
            //截取银行卡号后4位
            json.put("card_no_lastFour", info.getCard_no().substring(info.getCard_no().length() - 4, info.getCard_no().length()));
        }
        json.put("protocol_msg", "");
        //借款咨询服务协议
        json.put("protocol_url", PropertiesConfigUtil.get("APP_HOST_API") + "/credit-loan/agreement");
        //扣款协议
//					json.put("withholding_url",
//							request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
//									+ "/agreement/withholdAuthorization");
        //平台服务协议
        json.put("platformservice_url", PropertiesConfigUtil.get("APP_HOST_API") + "/agreement/platformServiceNew");
        //绑定银行卡H5
        json.put("firstUserBank_url", PropertiesConfigUtil.get("APP_HOST_API") + "/" + backConfigParamsService.findThirdPartyPayment() + "BindCard/credit-card/firstUserBank");
        json.put("real_pay_pwd_status", StringUtils.isBlank(user.getPayPassword()) ? "0" : "1"); // 支付密码状态；1：已设置支付密码；0：未设置支付密码
        json.put("verify_loan_pass", "1"); // 是否通过认证：1：认证全部通过；0：认证不通过


        //支付令业务开始
        //总开关 1 关闭 0 开启
        String cfcaSwitch = jedisCluster.get("CFCA_SWITCH");
        if (0 == (null != user.getSignSwitch() ? user.getSignSwitch() : 0) && "0".equals(cfcaSwitch)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String clientType = request.getParameter("clientType");
            String deviceId = request.getParameter("deviceId");

            //应还金额
            BigDecimal allMye = new BigDecimal(money);
            //本金金额
            double mye2 = money - apr_fee;
            BigDecimal tureMye = new BigDecimal(mye2);
            //合同金额
            contractInfo.setContractPrice(allMye);
            //设置回调URL  PropertiesConfigUtil.get("APP_HOST_API")
            contractInfo.setLinkedUrl(CfcaCommonUtil.NOTIFY_URL + "/lnkj/sign_status_notify/" + clientType + "/" + deviceId + "/" + user.getId());
            //借款用途
            template.setUseage(CfcaCommonUtil.USAGE);
            //本金
            template.setPrice(allMye.toString());
            //本金大写
            template.setPriceUpper(NumberToCN.number2CNMontrayUnit(allMye));
            //应还金额
            template.setRepaymentPrice(allMye.toString());
            //服务费
            template.setServerPrice(String.valueOf(apr_fee));
            //年利率
            template.setRate(CfcaCommonUtil.RATE);
            //逾期利率
            template.setOverdueRate(CfcaCommonUtil.OVERDUE_RATE);
            //续借利率
            template.setAgainRate(CfcaCommonUtil.AGAIN_RATE);
            //还款方式
            template.setRepaymentMethod(CfcaCommonUtil.REPAYMENT_METHOD);
            //还款期数
            template.setRepaymentCount(CfcaCommonUtil.REPAYMENT_COUNT);

            //借款期限
            int payCount = period;
            Date now = new Date();
            Date endTime = CalendarUtil.plusDay4Date(payCount - 1, now);
            //借款起息日
            template.setStartTime(sdf.format(now));
            //借款到期日
            template.setEndTime(sdf.format(endTime));
            //还款日
            template.setRepaymentDate(sdf.format(endTime));
            //创建日期
            template.setCreateTime(sdf.format(now));
            contractInfo.setTemplate(template);
            try {
                cfcaSignAndViewService.generatePayToken(user.getId(), contractInfo);
            } catch (Exception e) {
                logger.error(MessageFormat.format("用户:{0},生成支付令url 异常",userId));
                throw new PayException("生成支付令url 异常");
            }
            if (StringUtils.isNotEmpty(contractInfo.getCfcaContractId())) {
                json.put("sign_and_view_url", PropertiesConfigUtil.get("APP_HOST_API") + "/lnkj" + "/goto_sign" + "/" + contractInfo.getCfcaContractId());
                json.put("contract_id", contractInfo.getCfcaContractId());
            }
        }
    }

    /**
     * 申请to 借款
     *
     * @param request req
     * @param response res
     */
    /*@RequestMapping("get-confirm-loan")
    public void confirmLoan(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        // redis key
        String key = "xjx_borrow_apply_loan";
        //【1】参数校验
        Map<String, String> params = getParameters(request);
        logger.info("get-confirm-loan param=" + JSON.toJSONString(params));
        if (StringUtils.isBlank(params.get("money")) || StringUtils.isBlank(params.get("period"))){
            result.put("code", code);
            result.put("message","参数错误");
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
            return;
        }
        //【2】用户信息校验
        boolean isDelKey = true;
        User user;
        try{
            user =  checkUserLoginMsg(request,isDelKey);
        }catch (PayException e){
            result.put("code", code);
            result.put("message", e.getMessage());
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
            return;
        }
        Integer borrowMoney = Integer.valueOf(params.get("money"));
        Integer period = Integer.valueOf(params.get("period"));
        Integer productId = Integer.valueOf(params.get("productId"));

        //【3】校验用户是否可以借款
        ResponseContent serviceResult = allowBorrowV2(String.valueOf(user.getId()),
            params.get("period"), borrowMoney);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> json = new HashMap<>();
        try {
            if (serviceResult.isSuccessed()) {
                confirLoanSucc(Integer.valueOf(user.getId()), productId, request, borrowMoney, period, data, json);
                code = ResponseStatus.SUCCESS.getName();
                msg = ResponseStatus.SUCCESS.getValue();
            } else {
                msg = serviceResult.getMsg();
            }
        } catch (Exception e) {
            logger.error("credit-loan_get-confirm-loan======", e);
            e.printStackTrace();
            code = ResponseStatus.ERROR.getName();
            msg = ResponseStatus.ERROR.getValue();
        } finally {
            if (isDelKey) {
                try {
                    jedisCluster.del(key);
                } catch (Exception e) {
                    try {
                        jedisCluster.expire(key, 0);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            result.put("code", code);
            result.put("message", msg);
            data.put("item", json);
            result.put("data", data);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
        }
    }*/
    /**
     * 申请借款v2 （提额新需求）
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("/v2/get-confirm-loan")
    public void confirmLoanV2(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        String key = "xjx_borrow_apply_loan"; // redis key

        //【1】参数校验
        Map<String, String> params = getParameters(request);

        logger.info("V2/get-confirm-loan param=" + JSON.toJSONString(params));
        if (StringUtils.isBlank(params.get("money")) || StringUtils.isBlank(params.get("period"))){
            result.put("code", code);
            result.put("message","参数错误");
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
            return;
        }

        //【2】用户信息校验
        boolean isDelKey = true;
        User user;
        try{
             user =  checkUserLoginMsg(request,isDelKey);
        }catch (PayException e){
            result.put("code", code);
            result.put("message", e.getMessage());
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
            return;
        }

        Integer borrowMoney = Integer.valueOf(params.get("money"));
        Integer period = Integer.valueOf(params.get("period"));
        Integer productId = Integer.valueOf(params.get("productId"));

        //【3】校验用户是否可以借款
        ResponseContent serviceResult = allowBorrowV2(String.valueOf(user.getId()),params.get("period"),borrowMoney,productId);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> json = new HashMap<>();
        try {
            //【4】用户可以借款，返回借款费率和信息
            if (serviceResult.isSuccessed()) {
                confirLoanSucc(Integer.valueOf(user.getId()),productId,request,borrowMoney,period,data,json);
                code = ResponseStatus.SUCCESS.getName();
                msg = ResponseStatus.SUCCESS.getValue();
            } else {
                msg = serviceResult.getMsg();
            }
        } catch (Exception e) {
            logger.error("V2/credit-loan_get-confirm-loan======", e);
            e.printStackTrace();
            code = ResponseStatus.ERROR.getName();
            msg = ResponseStatus.ERROR.getValue();
        } finally {
            if (isDelKey) {
                try {
                    jedisCluster.del(key);
                } catch (Exception e) {
                    try {
                        jedisCluster.expire(key, 0);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            result.put("code", code);
            result.put("message", msg);
            data.put("item", json);
            result.put("data", data);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
        }
    }

    /**
     * 云法通合同签署状态查询
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("query-cfca-status")
    public void signStatusQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> parames = getParameters(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String contractId = parames.get("contract_id");
        Map<String, Object> data = new HashMap<>();
        String code = Status.SUCCESS.getName();
        String msg = Status.SUCCESS.getValue();
        result.put("code", code);
        result.put("message", msg);
        result.put("time", sdf.format(new Date()));

        if (StringUtils.isEmpty(contractId)) {
            data.put("status", "0");
        } else {
            Integer status = cfcaSignAndViewService.cfcaQueryStatus(contractId);
            data.put("status", null == status ? 0 : status);
        }
        result.put("data", data);
        JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
    }

    /**
     * 申请借款
     *
     * @param request req
     * @param response res
     */
    /*@RequestMapping("apply-loan")
    public void applyLoan(HttpServletRequest request, HttpServletResponse response) {
        logger.info("apply-loan start");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        // redis key
        String key = "xjx_borrow_apply_loan";
        String code = ResponseStatus.FAILD.getName();
        String msg = "申请失败";
        boolean isDelKey = true;
        try {
            boolean isTg = false;
            Map<String, String> params = getParameters(request);
            logger.info("apply-loan params=" + (params != null ? JSON.toJSONString(params) : "null"));
            User user = this.loginFrontUserByDeiceId(request);
            if (user == null || StringUtils.isBlank(user.getId())) {
                String ip = RequestUtils.getIpAddr(request);
                Map<String, String> map3 = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
                String value = map3.get("TG_SERVER_IP");
                if (StringUtils.isNotBlank(value)) {
                    String[] ips = value.split(",");
                    List<String> list = Arrays.asList(ips);
                    if (list.contains(ip)) {
                        isTg = true;
                        user = userService.searchByUserid(Integer.valueOf(AESUtil.decrypt(params.get("userId"), map3.get("TG_SERVER_KEY"))));
                    }
                }
            }
            if (user == null) {
                if (isTg) {
                    msg = "用户不存在";
                } else {
                    msg = "请先登录";
                }
                return;
            } else if (!isTg) {
                // 从数据库获取最新的user信息，因需要判断用户的可借额度
                user = userService.searchByUserid(Integer.parseInt(user.getId()));
            }
            key = key + "_" + user.getId(); // 确定key唯一
            if (StringUtils.isNotBlank(jedisCluster.get(key))) {
                isDelKey = false;
                msg = "您的借款申请正在处理中，请稍后再试";
            } else {
                //【1】校验用户是否可以借款
                Integer borrowMoney = Integer.valueOf(params.get("money"));
                Integer period = Integer.valueOf(params.get("period"));

                //【3】校验用户是否可以借款
                ResponseContent serviceResult = allowBorrowV2(String.valueOf(user.getId()),
                    params.get("period"), borrowMoney);

                if (serviceResult.isSuccessed()) {
//                    AESUtil aesEncrypt = new AESUtil();
                    String checkPassWord = MD5Util.MD5(AESUtil.encrypt(params.get("pay_password"), ""));// 加密
                    if (!isTg && !user.getPayPassword().equals(checkPassWord)) {
                        msg = "支付密码错误";
                        return;
                    }
                    //【2】保存用户借款信息
                    Map<String, Object> map = borrowOrderService.saveLoan(params, user);
                    code = map.get("code").toString();
                    msg = map.get("msg").toString();
                    item.put("order_id", map.get("orderId"));
                    // 申请借款成功-更新info_user_info borrow_status 状态为可见

                    if("0".equals(code)){
                        Object orderId = map.get("orderId");
                        dealRemoteCreditReport((int)orderId);

                        HashMap<String, Object> borrowMap = new HashMap<>();
                        borrowMap.put("USER_ID", user.getId());
                        borrowMap.put("BORROW_STATUS", "1");
                        // System.out.println("borrowMap="+borrowMap+"  申请借款，需要显示进度");
                        indexDao.updateInfoUserInfoBorrowStatus(borrowMap);
                    }
                    jedisCluster.del(key);
                } else {
                    msg = serviceResult.getMsg();
                }
            }


        } catch (Exception e) {
            logger.error("credit-loan_get-apply-loan======", e);
            code = ResponseStatus.ERROR.getName();
            msg = ResponseStatus.ERROR.getValue();
        } finally {
            if (isDelKey) {
                try {
                    jedisCluster.del(key);
                } catch (Exception e) {
                    try {
                        jedisCluster.expire(key, 0);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            logger.info("apply-loan end");
            result.put("code", code);
            result.put("message", msg);
            data.put("item", item);
            result.put("data", data);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
        }
    }*/


    /**
     * 申请借款(提额需求)
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("v2/apply-loan")
    public void applyLoanV2(HttpServletRequest request, HttpServletResponse response) {
        logger.info("v2/apply-loan start");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        // redis key
        String key = "xjx_borrow_apply_loan";
        String code = ResponseStatus.FAILD.getName();
        String msg = "申请失败";
        boolean isDelKey = true;
        try {
            boolean isTg = false;
            Map<String, String> params = getParameters(request);
            logger.info("apply-loan params=" + (params != null ? JSON.toJSONString(params) : "null"));
            User user = this.loginFrontUserByDeiceId(request);
            if (user == null || StringUtils.isBlank(user.getId())) {
                String ip = RequestUtils.getIpAddr(request);
                Map<String, String> map3 = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
                String value = map3.get("TG_SERVER_IP");
                if (StringUtils.isNotBlank(value)) {
                    String[] ips = value.split(",");
                    List<String> list = Arrays.asList(ips);
                    if (list.contains(ip)) {
                        isTg = true;
                        user = userService.searchByUserid(Integer.valueOf(AESUtil.decrypt(params.get("userId"), map3.get("TG_SERVER_KEY"))));
                    }
                }
            }
            if (user == null) {
                if (isTg) {
                    msg = "用户不存在";
                } else {
                    msg = "请先登录";
                }
                return;
            } else if (!isTg) {
                // 从数据库获取最新的user信息，因需要判断用户的可借额度
                user = userService.searchByUserid(Integer.parseInt(user.getId()));
            }
            key = key + "_" + user.getId(); // 确定key唯一
            if (StringUtils.isNotBlank(jedisCluster.get(key))) {
                isDelKey = false;
                msg = "您的借款申请正在处理中，请稍后再试";
            } else {
                //【1】校验用户是否可以借款
                Integer borrowMoney = Integer.valueOf(params.get("money"));
                Integer productId = Integer.valueOf(params.get("productId"));
//                Integer period = Integer.valueOf(params.get("period"));

                //【3】校验用户是否可以借款
                ResponseContent serviceResult = allowBorrowV2(String.valueOf(user.getId()),params.get("period"),borrowMoney,productId);

                if (serviceResult.isSuccessed()) {
//                    AESUtil aesEncrypt = new AESUtil();
                    // 加密
                    /*String checkPassWord = MD5Util.MD5(AESUtil.encrypt(params.get("pay_password"), ""));
                    if (!isTg && !user.getPayPassword().equals(checkPassWord)) {
                        msg = "支付密码错误";
                        return;
                    }*/

                    //【2】保存用户借款信息
                    Map<String, Object> map = borrowOrderService.saveLoanV2(params, user);
                    code = map.get("code").toString();
                    msg = map.get("msg").toString();
                    item.put("order_id", map.get("orderId"));
                    // 申请借款成功-更新info_user_info borrow_status 状态为可见
                    if("0".equals(code)){
                        Object orderId = map.get("orderId");

                        dealRemoteCreditReport((int)orderId);

                        HashMap<String, Object> borrowMap = new HashMap<>();
                        borrowMap.put("USER_ID", user.getId());
                        borrowMap.put("BORROW_STATUS", "1");
                        indexDao.updateInfoUserInfoBorrowStatus(borrowMap);
                    }
                    jedisCluster.del(key);
                } else {
                    msg = serviceResult.getMsg();
                }
            }
        } catch (Exception e) {
            logger.error("V2/credit-loan_get-apply-loan======", e);
            code = ResponseStatus.ERROR.getName();
            msg = ResponseStatus.ERROR.getValue();
        } finally {
            if (isDelKey) {
                try {
                    jedisCluster.del(key);
                } catch (Exception e) {
                    try {
                        jedisCluster.expire(key, 0);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            logger.info("apply-loan end");
            result.put("code", code);
            result.put("message", msg);
            data.put("item", item);
            result.put("data", data);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
        }
    }

    @RequestMapping("autoRisk")
    public void autoRisk(HttpServletRequest request) {
        String assetBorrowId = request.getParameter("assetBorrowId");
        if (assetBorrowId != null) {
            logger.info("autoRisk assetBorrowId = " + assetBorrowId);
            autoRiskService.dealRemoteCreditReport(Integer.parseInt(assetBorrowId));
        } else {
            logger.info(" assetBorrowId = " + assetBorrowId);
        }
    }

    private void dealRemoteCreditReport(Integer assetBorrowId) {
        ThreadPool.getInstance().execute(() -> {
            logger.info(" auto risk begin -- borrowId = " + assetBorrowId);
            autoRiskService.dealRemoteCreditReport(assetBorrowId);
            logger.info(" auto risk end");
        });
    }


}
