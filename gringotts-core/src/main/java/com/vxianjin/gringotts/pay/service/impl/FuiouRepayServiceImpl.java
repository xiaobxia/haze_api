package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.common.ServiceResult;
import com.vxianjin.gringotts.constant.CollectionConstant;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.enums.ErrorBase;
import com.vxianjin.gringotts.pay.common.enums.EventTypeEnum;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.publish.PublishAdapter;
import com.vxianjin.gringotts.pay.common.publish.PublishFactory;
import com.vxianjin.gringotts.pay.common.util.YeepayApiUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouApiUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.XMapUtil;
import com.vxianjin.gringotts.pay.component.FuiouService;
import com.vxianjin.gringotts.pay.dao.IRenewalRecordDao;
import com.vxianjin.gringotts.pay.model.*;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolOrderXmlBeanReq;
import com.vxianjin.gringotts.pay.service.*;
import com.vxianjin.gringotts.pay.service.base.RepayService;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.common.JedisClusterHelper;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 还款续期相关
 * @author zed
 * @date 2018/7/18 20:26
 */
@Service
public class FuiouRepayServiceImpl implements FuiouRepayService {
    private static final Logger logger = LoggerFactory.getLogger(FuiouRepayServiceImpl.class);

    @Resource
    private JedisCluster jedisCluster;

    @Resource
    private IUserDao userDao;

    @Resource
    private IRenewalRecordDao renewalRecordDao;

    @Resource
    private FuiouService fuiouService;

    @Resource
    private IUserService userService;

    @Resource
    private RepaymentService repaymentService;

    @Resource
    private RepaymentDetailService repaymentDetailService;

    @Resource
    private IOutOrdersService outOrdersService;

    @Resource
    private RenewalRecordService renewalRecordService;

    @Resource
    private RepayService repayService;

    @Resource
    private ApplicationContext applicationContext;

    private final String appCode = PropertiesConfigUtil.get("RISK_BUSINESS");

    // 生成手机验证码频繁标识key
    private static String WITHHOLD_SMS_CODE = "WITHHOLD_SMS_CODE_";

//    @Resource
//    private CommonProducer producer;

    @Value("#{mqSettings['oss_topic']}")
    private String ossMqTopic;

    @Value("#{mqSettings['oss_target']}")
    private String ossMqTarget;

    /**
     * 代扣回调（还款）
     *
     * @param callbackResult
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultModel payWithholdCallback(Map<String, String> callbackResult) {
        ResultModel resultModel = new ResultModel(false);

        /*logger.info("YeepayRepayServiceImpl.payWithholdCallback params:【" + JSON.toJSONString(req) + "】");
        Map<String, String> callbackResult = YeepayApiUtil.getCallBackParamMap(req);*/
        logger.info("YeepayRepayServiceImpl.payWithholdCallback callbackResult=" + (callbackResult != null ? JSON.toJSONString(callbackResult) : "null"));
        if (callbackResult == null) {
            resultModel.setMessage("数据解析失败");
            return resultModel;
        }
        // 解析数据
        //支付编号
        String orderNo = FuiouApiUtil.formatString(callbackResult.get("MCHNTORDERID"));
        String fuiouOrderId = FuiouApiUtil.formatString(callbackResult.get("MCHNTORDERID"));
        //支付结果状态
        String status = FuiouApiUtil.formatString(callbackResult.get("RESPONSECODE"));
        //支付结果信息
        String errorMsg = FuiouApiUtil.formatString(callbackResult.get("RESPONSEMSG"));
        //代扣金额
        String orderMoney = FuiouApiUtil.formatString(callbackResult.get("AMT"));
        // 获取外部订单
        OutOrders outOrders = outOrdersService.findByOrderNo(orderNo);
        logger.info("YeepayRepayService.payWithholdCallback orderNo: " + orderNo + ",outOrdersStatus=" + (outOrders != null ? outOrders.getStatus() : "null"));
        // 暂时还是以判断成功是否处理，看代码里请求代扣如果超时等情况会改订单为失败，但是可能第三方收到请求代扣成功了
        if (null != outOrders && (!OutOrders.STATUS_SUC.equals(outOrders.getStatus()))) {
            if (null != outOrders.getAssetOrderId()) {
                // 获取外部订单的类型
//                String act = outOrders.getAct();
                // 获取还款详情
                //获取还款详情记录
                RepaymentDetail detail = repaymentDetailService.selectByOrderId(outOrders.getOrderNo());
                // 获取还款信息
                //获取还款记录
                Repayment re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());
                // 获取用户信息
                User user = userDao.searchByUserid(re.getUserId());
                final String phone = user.getUserPhone();
                final String realName = user.getRealname();
                final long amount = re.getRepaymentAmount();
                //支付成功
                if ("0000".equals(status)) {
                    logger.info("YeepayRepayServiceImpl.payWithholdCallback orderNo:" + orderNo + " pay success");
                    // 还款回调处理
                    repayService.repayCallBackHandler(re, detail, outOrders, orderMoney, true, "", "易宝",user);
                    //发送扣款成功短信
                    String content = "";
                    content = MessageFormat.format("尊敬的{0}：您的{1}元借款已经还款成功，您的该笔交易将计入您的信用记录，好的记录将有助于提升您的可用额度。", realName, (amount / 100.00));

//                    String userClientId = userClientInfoService.queryClientIdByUserId(re.getUserId());
//                    String mqMsg = MessageFormat.format("您好，您的{0}元借款已经还款成功，该笔交易将有助于提升您的信用额。", (amount / 100.00));
//                    logger.info("prepared send mqMsg:" + mqMsg + " userClientId:" + userClientId);
//                    producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(2, userClientId, "还款成功", mqMsg, mqMsg)));

                    try {
                        PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.REPAY.getCode());
                        publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), content, phone);
                    } catch (PayException e) {
                        logger.error(MessageFormat.format("用户{0},还款成功短信发送失败====>e :{1}", phone, e.getMessage()));
                    }

                } else {//支付失败
                    logger.info("YeepayRepayServiceImpl.payWithholdCallback orderNo: " + orderNo + " pay fail");
                    outOrders.setNotifyParams(JSON.toJSONString(callbackResult));
                    // 还款回调处理
                    repayService.repayCallBackHandler(re, detail, outOrders, orderMoney, false, errorMsg, "易宝",user);
                    // 定时代扣失败短信注释
                    // 是定时代扣 且是未完全还款 还款日是当天 当天回调 才发送短信
                    /*if (act.equals("TASK_DEBIT")
                            && (!re.getStatus().equals("30")
                                && DateUtil.fyFormatDate(re.getRepaymentTime()).equals(DateUtil.fyFormatDate(new Date())))
                            && DateUtil.fyFormatDate(outOrders.getAddTime()).equals(DateUtil.fyFormatDate(new Date())) ) {
                        //发送扣款失败短信
                        UserCardInfo info = userService.findUserBankCard(Integer.parseInt(user.getId()));//还款用户银行卡信息

                        String content = "";
                        if ("JX".equals(appCode)) {
                            content = MessageFormat.format("尊敬的{0}，您的{1}借款今日到期，请至APP还款，若到期未还款，平台将自动扣款，请确保尾号{2}银行卡资金充足。如已还款，请忽略。", realName, (amount / 100.00), info.getCard_no().substring(info.getCard_no().length() - 4));
                        } else {
                            content = MessageFormat.format("尊敬的{0}，您的{1}借款今日到期，请至APP还款，若到期未还款，平台将自动扣款，请确保尾号{2}银行卡资金充足。", realName, (amount / 100.00), info.getCard_no().substring(info.getCard_no().length() - 4));
                        }
                        //  通过mq推送消息到运营平台,待定需不需要推送，暂时推送类型定位还款成功
                        // producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(2, content)));

                        try {
                            PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.REPAY.getCode());
                            publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), content, phone);
                        } catch (PayException e) {
                            logger.error(MessageFormat.format("用户{0},还款提示短信发送失败====>e :{1}", phone, e.getMessage()));
                        }
                    }*/
                }
            }
            resultModel.setSucc(true);
            resultModel.setCode(ErrorBase.SUCCESS.getCode());
            resultModel.setMessage(ErrorBase.SUCCESS.getMessage());
        } else {
            resultModel.setSucc(false);
            resultModel.setCode(ErrorBase.FAIL.getCode());
            resultModel.setMessage(ErrorBase.FAIL.getMessage());
        }
        logger.info("payWithholdCallback order is " + orderNo + "  result is " + JSON.toJSONString(resultModel));
        return resultModel;

    }

    /**
     * 续期回调（续期）
     * @param req req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultModel payRenewalWithholdCallback(String req) {
        ResultModel resultModel = new ResultModel(false);

        logger.info("payRenewalWithholdCallback params;【" + JSON.toJSONString(req) + "】");

        Map<String, String> callbackResult = YeepayApiUtil.getCallBackParamMap(req);
        logger.info("payRenewalWithholdCallback callbackResult=" + (callbackResult != null ? JSON.toJSONString(callbackResult) : "null"));
        if (callbackResult == null) {
            resultModel.setMessage("数据解析失败");
            return resultModel;
        }
        // 解析内容
        //支付编号
        String orderNo = callbackResult.get("requestno");
        //支付结果状态
        String status = callbackResult.get("status");
        //支付结果信息
        String errorMsg = callbackResult.get("errormsg");

        OutOrders outOrders = outOrdersService.findByOrderNo(orderNo);
        logger.info("payRenewalWithholdCallback orderNo;" + orderNo + " outOrdersStatus=" + (outOrders != null ? outOrders.getStatus() : "null"));
        if (null != outOrders && (!OutOrders.STATUS_SUC.equals(outOrders.getStatus()))) {
            if (null != outOrders.getAssetOrderId()) {
                logger.info("renewalWithholdCallback orderNo;" + orderNo + " assetOrderId is" + outOrders.getAssetOrderId());
                //获取还款记录
                Repayment re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());
                RenewalRecord renewalRecord = renewalRecordService.getRenewalRecordByOrderId(orderNo);
                //支付成功
                if ("PAY_SUCCESS".equals(status)) {
                    logger.info("renewalWithholdCallback success order_no=" + outOrders.getOrderNo());
                    repayService.continuCallBackHandler(outOrders, renewalRecord, re, true, "", "易宝");
                } else {//支付失败
                    logger.info("renewalWithholdCallback false no_order=" + orderNo);
                    repayService.continuCallBackHandler(outOrders, renewalRecord, re, false, errorMsg, "易宝");
                }
            }
            resultModel.setSucc(true);
            resultModel.setCode(ErrorBase.SUCCESS.getCode());
            resultModel.setMessage(ErrorBase.SUCCESS.getMessage());
        } else {
            resultModel.setSucc(false);
            resultModel.setCode(ErrorBase.FAIL.getCode());
            resultModel.setMessage(ErrorBase.FAIL.getMessage());
        }
        logger.info("renewalWithholdCallback end,order " + orderNo + " result : " + JSON.toJSONString(resultModel));
        return resultModel;
    }

    /**
     * 主动支付确认（还款）
     * @param id id
     * @param payPwd pwd
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent repaymentWithholdConfirm(Integer id, String payPwd, String bankIdStr) throws Exception {
        logger.info("repaymentWithholdConfirm params:【id:" + id + " payPwd:" + payPwd + "】");
        ResponseContent result = null;
        // 获取还款相关信息
        NeedRepayInfo needRepayInfo = repayService.getNeedRepayInfo(id);

//        AESUtil aesEncrypt = new AESUtil();
        //验证用户是否设置了交易密码
        if (StringUtils.isBlank(needRepayInfo.getUser().getPayPassword())) {
            return new ResponseContent("-101", "请从个人中心完善支付密码");
        }
        if (!needRepayInfo.getUser().getPayPassword().equals(MD5Util.MD5(AESUtil.encrypt(payPwd, "")))) {
            return new ResponseContent("-103", "支付密码输入错误");
        }

        Integer bankId;
        //如果没传则使用默认卡
        if (StringUtils.isEmpty(bankIdStr)) {
            UserCardInfo userBankCard = userService.findUserBankCard(needRepayInfo.getBorrowOrder().getUserId());
            bankId = userBankCard.getId();
            logger.info("params bankIdStr is null,BankId :" + bankId);
        } else {
            bankId = Integer.valueOf(bankIdStr);
        }
        logger.info("repaymentWithholdRequest bankCardId:" + bankId);

        //生成还款编号请求号（唯一，且整个还款过程中保持不变）
        String requestNo = GenerateNo.nextOrdId();
        try {
            logger.info("prepare send to yeepay , repaymentId " + needRepayInfo.getRepayment().getId());
            // 占锁
            repayService.addRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            Map<String, String> paramMap = new HashMap<>();
            //支付请求编号
            paramMap.put("requestNo", requestNo.trim());
            //用户id
            paramMap.put("userId", needRepayInfo.getBorrowOrder().getUserId() + "");

            //发起主动代扣请求，并获取结果。（参数：还款编号、还款信息、还款用户信息、还款额度、还款操作）
            ResponseContent serviceResult = newRecharge(requestNo, needRepayInfo.getRepayment(), needRepayInfo.getUser(), needRepayInfo.getMoney(), "主动支付", bankId);


            logger.info("repaymentWithholdConfirm userId=" + needRepayInfo.getUser().getId() + " serviceResult=" + JSON.toJSONString(serviceResult));
            if ("0000".equals(serviceResult.getCode()) || "P000".equals(serviceResult.getCode()) ) {
                result = new ResponseContent("0", requestNo);
            } else {
                //支付失败状态下，解除该笔订单锁定状态
                repayService.removeRepaymentLock(needRepayInfo.getRepayment().getId() + "");
                result = new ResponseContent(serviceResult.getCode(), serviceResult.getMsg());
            }
        } catch (Exception e) {
            repayService.removeRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            throw e;
        }
        return result;
    }

    /**
     * 主动支付短信重发（还款）
     *
     * @param req 请求内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public YeepaySmsReSendResp repaymentWithholdResendSmscode(YeepayRepaySmsReq req) {
        logger.info("repaymentWithholdResendSmscode params:【req:" + JSON.toJSONString(req) + "】");
        //用户id
        String userId = req.getUserId();
        logger.info("repaymentWithholdResendSmscode userId=" + userId);
        String code = ResponseStatus.FAILD.getName();
        String msg = "支付失败";
        String time = "";
        String respRequestNo = "";
        try {
            if (StringUtils.isBlank(userId)) {
                logger.info("userId is null");
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
                return new YeepaySmsReSendResp(code, msg, time, respRequestNo);
            }
            User user = userService.searchByUserid(Integer.parseInt(userId));
            if (null == user) {
                logger.info("userId is" + userId + " is not login");
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
                return new YeepaySmsReSendResp(code, msg, time, respRequestNo);
            }
            //绑卡请求编号
            String requestNo = req.getRequest_no() == null ? "" : req.getRequest_no();
            logger.info(MessageFormat.format("repaymentWithholdResendSmscode userId={0},userPhone={1},requestNo={2}",userId, user.getUserPhone(),requestNo));

            if (StringUtils.isBlank(requestNo)) {
                code = ResponseStatus.FAILD.getName();
                msg = "请求参数异常，请确认后重试";
                return new YeepaySmsReSendResp(code, msg, time, respRequestNo);
            }

            //短信冷却剩余时间（单位：s）
            Long remainTime = checkForFront(WITHHOLD_SMS_CODE, requestNo + user.getUserPhone(), 60);
            if (remainTime > 0) {
                logger.info("requestNo " + requestNo + " remainTime " + remainTime);
                code = ResponseStatus.FREQUENT.getName();
                msg = ResponseStatus.FREQUENT.getValue();
                time = remainTime.toString();
                return new YeepaySmsReSendResp(code, msg, time, respRequestNo);
            }
            logger.info("repaymentWithholdResendSmscode userId=" + userId + " remainTime success " + remainTime);


            Map<String, String> paramMap = new HashMap<>();
            //支付请求编号
            paramMap.put("requestNo", requestNo.trim());
            //用户id
            paramMap.put("userId", userId);

            //发送银行卡绑定验证码
            Map<String, Object> resultMap = fuiouService.getRechargeSmsCode(paramMap);
            logger.info("repaymentWithholdResendSmscode userId=" + userId + " resultMap=" + JSON.toJSONString(resultMap));
            if ("0000".equals(String.valueOf(resultMap.get("code")))) {
                code = ResponseStatus.SUCCESS.getName();
                msg = ResponseStatus.SUCCESS.getValue();
                //短信冷却时间
                time = "60";
                respRequestNo = requestNo;
                //存入redis里，避免发送过于频繁
                checkForFront(WITHHOLD_SMS_CODE, requestNo + user.getUserPhone(), 60);
            } else {
                code = ResponseStatus.FAILD.getName();
                msg = resultMap.get("msg") + "";
            }


        } catch (Exception e) {
            logger.error("repaymentWithholdResendSmscode userId=" + userId + " error", e);
        } finally {
            logger.info("repaymentWithholdResendSmscode userId=" + userId + " message=" + msg);
        }
        return new YeepaySmsReSendResp(code, msg, time, respRequestNo);
    }

    /**
     * 主动代扣（还款）
     *
     * @param id id
     * @param payPwd pwd
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent repaymentWithhold(Integer id, String payPwd) throws Exception {
        logger.info("repaymentWithhold params:【id:" + id + " payPwd:" + payPwd + "】");
        ResponseContent result = null;
        // 获取需要还款相关信息
        NeedRepayInfo needRepayInfo = repayService.getNeedRepayInfo(id);

        //验证用户是否设置了交易密码
        if (StringUtils.isBlank(needRepayInfo.getUser().getPayPassword())) {
            return new ResponseContent("-101", "请从个人中心完善支付密码");
        }
        if (!needRepayInfo.getUser().getPayPassword().equals(MD5Util.MD5(AESUtil.encrypt(payPwd, "")))) {
            return new ResponseContent("-103", "支付密码输入错误");
        }
        try {
            logger.info("prepare send to yeepay repaymentId is " + needRepayInfo.getRepayment().getId());
            //占锁
            repayService.addRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            //发起主动代扣请求，并获取结果。（参数：还款信息、还款用户信息、主动代扣、还款额度、还款操作）
            result = yeepayWithhold(needRepayInfo.getRepayment(), needRepayInfo.getUser(), Repayment.CARD_WITHHOLD, needRepayInfo.getMoney(), OutOrders.ACT_REPAY_DEBIT, "主动代扣");
        } catch (Exception e) {
            repayService.removeRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            logger.error("repaymentWithhold error ", e);
            throw e;
        }
        return result;
    }


    /**
     * 续期代扣（一般充值）
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent renewalWithhold(Integer id, String payPwd, Long money, String bankIdStr, String sgd) throws Exception {
        logger.info("renewalWithhold borrowId=" + id + " payPwd=" + payPwd + " money=" + money);

        ResponseContent result = null;
        // 获取续期相关信息
        NeedRenewalInfo needRenewalInfo = repayService.getNeedRenewalInfo(id);

        // 银行卡获取及检验

        Integer bankId;
        //如果没传则使用默认卡
        if (StringUtils.isEmpty(bankIdStr)) {
            UserCardInfo userBankCard = userService.findUserBankCard(needRenewalInfo.getBorrowOrder().getUserId());
            bankId = userBankCard.getId();
        } else {
            bankId = Integer.valueOf(bankIdStr);
        }
        if (null == needRenewalInfo.getUser().getPayPassword()) {
            return new ResponseContent("-103", "请从个人中心完善支付密码");
        }
        // 判断费用是否更新
        if (!needRenewalInfo.getAllCount().equals(money)) {
            return new ResponseContent("-101", "您的费用已更新，请刷新当前页面");
        }
        // 支付密码校验
//        AESUtil aesEncrypt = new AESUtil();
//        boolean isPass = false;
        if (StringUtils.isNotBlank(sgd)) {
            Map<String, String> params = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
            sgd = AESUtil.decrypt(sgd, params.get("TG_SERVER_KEY"));
            if (!String.valueOf(id).equals(sgd)) {
                return new ResponseContent("-101", "参数非法！");
            }
        } else {
            if (!needRenewalInfo.getUser().getPayPassword().equals(MD5Util.MD5(AESUtil.encrypt(payPwd, "")))) {
                return new ResponseContent("-103", "支付密码输入错误");
            }
        }
        //支付密码验证失败
//        if (!isPass) {
//            return new ResponseContent("-103", "支付密码输入错误");
//        }
        // 生成续期单号
        //续期订单号
        String orderNo = GenerateNo.payRecordNo(needRenewalInfo.getUser().getId());
        RenewalRecord renewalRecord = repayService.beforeRenewalHandler(needRenewalInfo, orderNo);
        //发送请求
        try {
            //锁定该笔续期，防止其他渠道重复续期，锁定时间：30分钟
            repayService.addRenewalLock(needRenewalInfo.getRepayment().getId() + "");

            ResponseContent serviceResult = yeepayRenewalWithhold(renewalRecord, needRenewalInfo.getUser(), needRenewalInfo.getAllCount(), bankId);
            logger.info("renewalWithhold userId=" + needRenewalInfo.getUser().getId() + " serviceResult=" + JSON.toJSONString(serviceResult));
            if (serviceResult.getCode().equals(ServiceResult.SUCCESS)) {
                result = new ResponseContent("0", serviceResult.getMsg());
            } else {
                //异常状态下，解除该笔订单锁定状态
                repayService.removeRenewalLock(needRenewalInfo.getRepayment().getId() + "");
                result = new ResponseContent(serviceResult.getCode(), serviceResult.getMsg());
            }

        } catch (Exception e) {
            repayService.removeRenewalLock(needRenewalInfo.getRepayment().getId() + "");
            logger.error("renewalWithhold error ", e);
            throw e;
        }
        return result;
    }


    /**
     * 定时代扣（还款）
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent autoWithhold(Integer id) throws Exception {
        logger.info("autoWithhold start id=" + (id != null ? id : "null"));
        logger.info("autoWithhold repaymentId=" + id);
        ResponseContent result = null;
        // 获取还款相关信息
        NeedRepayInfo needRepayInfo = repayService.getNeedRepayInfoByPaymentId(id);

        try {
            logger.info("prepare send to yeepay repaymentId is " + needRepayInfo.getRepayment().getId());
            // 先占锁
            repayService.addRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            //发起定时代扣请求，并获取结果。（参数：还款信息、还款用户信息、定时代扣、还款额度、还款操作）
            result = yeepayWithhold(needRepayInfo.getRepayment(), needRepayInfo.getUser(), Repayment.TASK_WITHHOLD, needRepayInfo.getMoney(), OutOrders.ACT_TASK_DEBIT, "定时代扣");
        } catch (Exception e) {
            //异常状态下，解除该笔订单锁定状态
            repayService.removeRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            logger.error("autoWithhold error ", e);
            throw e;
        }
        return result;
    }

    /**
     * 查询代扣支付结果（还款）
     * (只读事物)
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseContent queryWithhold(Integer id, String orderNo) {
        logger.info("queryWithhold borrowId=" + id + " orderNo=" + orderNo);

        ResponseContent result;
        if (id == null || orderNo == null) {
            return new ResponseContent("-101", "");
        }

        //获取支付订单
        //获取还款详情记录
        RepaymentDetail detail = repaymentDetailService.selectByOrderId(orderNo);

        logger.info("queryWithhold detail=" + JSON.toJSONString(detail));

        if (detail == null || detail.getStatus() == null) {
            return new ResponseContent("-101", "订单不存在或者有误");
        }

        switch (detail.getStatus()) {
            case RepaymentDetail.STATUS_SUC:
                result = new ResponseContent("0", "还款成功");
                break;
            //支付处理中
            case RepaymentDetail.STATUS_WAIT:
                result = new ResponseContent("-101", "支付处理中");
                break;
            default:
                result = new ResponseContent("400", detail.getRemark());
                break;
        }
        logger.info("queryWithhold result=" + JSON.toJSONString(result));
        return result;
    }

    /**
     * 查询代扣支付结果（还款）
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseContent queryRenewalWithhold(Integer id, String orderNo) {
        logger.info("queryRenewalWithhold borrowId=" + id + " orderNo=" + orderNo);

        ResponseContent result;
        if (id == null || orderNo == null) {
            return new ResponseContent("-101", "请求参数非法");
        }

        //获取支付订单
        RenewalRecord renewalRecord = null;
        try {
            renewalRecord = renewalRecordService.getRenewalRecordByOrderId(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("queryRenewalWithhold renewalRecord=" + JSON.toJSONString(renewalRecord));
        if (RenewalRecord.STATUS_SUCC.equals(renewalRecord.getStatus())) {
            result = new ResponseContent("0", "支付成功");
            //支付处理中
        } else if (RenewalRecord.STATUS_PAYING.equals(renewalRecord.getStatus())) {
            result = new ResponseContent("-101", "支付处理中");
        } else {//支付异常
            result = new ResponseContent("400", renewalRecord.getRemark());
        }
        logger.info("queryWithhold result=" + JSON.toJSONString(result));
        return result;
    }

    /**
     * 催收代扣（还款）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseContent collectionWithhold(String userId, String repaymentId, Long money,
                                              String withholdId, String sign) throws Exception {
        logger.info("collectionWithhold userId=" + userId + " repaymentId=" + repaymentId + " money=" + money + " withholdId=" + withholdId + " sign=" + sign);

        ResponseContent result = null;

        //验证签名
        if (!MD5Util.MD5(AESUtil.encrypt("" + userId + repaymentId + money + withholdId, CollectionConstant.getCollectionSign())).equals(sign)) {
            return new ResponseContent("-101", "代付失败,验证签名失败");
        }

        String key = "REPAYMENT_WITHHOLD_TIME_" + repaymentId;
        String lastTime = jedisCluster.get(key);
        long nowTime = System.currentTimeMillis();
        jedisCluster.set(key, String.valueOf(nowTime));
        if (null != lastTime && (nowTime - Long.parseLong(lastTime)) < (5 * 60 * 1000)) {
            return new ResponseContent("-101", "距离上次代扣时间不足5分钟，请稍后再试");
        }
        // 判断缓存中是否有正在处理的该订单
        String flag = repayService.getRepaymentLock(repaymentId);
        logger.info("collectionWithhold flag=" + flag);
        if ("true".equals(flag)) {
            return new ResponseContent("-101", "该笔还款订单正在处理中");
        }

        Repayment re = repaymentService.selectByPrimaryKey(Integer.parseInt(repaymentId));
        re.setWithholdId(withholdId);
        User user = userService.searchByUserid(Integer.parseInt(userId));
        if (money <= 0) {
            return new ResponseContent("-101", "代扣失败,催收金额为不能为0");
        }

        if (money > re.getRepaymentAmount() - re.getRepaymentedAmount()) {
            return new ResponseContent("-101", "代扣失败,催收金额大于剩余待付金额");
        }

        try {
            logger.info("prepare send to yeepay repaymentId is " + re.getId());
            //锁定该笔还款，防止其他渠道重复扣款，锁定时间：5分钟
            repayService.addRepaymentLock(re.getId() + "");

            //发起催收代扣请求，并获取结果。（参数：还款信息、还款用户信息、催收代扣、还款额度、还款操作）
            result = yeepayWithhold(re, user, Repayment.COLLECTION_WITHHOLD, money, OutOrders.ACT_COLLECTION_DEBIT, "催收代扣");
        } catch (Exception e) {
            //异常状态下，解除该笔订单锁定状态
            repayService.removeRepaymentLock(re.getId() + "");
            logger.error("collectionWithhold error ", e);
            throw e;
        }
        return result;
    }


    private Long checkForFront(String key, String flag, int time) {
        return JedisClusterHelper.checkForFront(jedisCluster, key, flag, time);
    }

    /**
     * 易宝代扣请求处理
     *
     * @param repayment    还款信息
     * @param user         用户
     * @param withholdType 代扣类型(定时，主动，催收)
     * @param money        金额
     * @param debitType type
     * @param remark remark
     * @return res
     */
    @Override
    public ResponseContent yeepayWithhold(Repayment repayment, User user, int withholdType, Long money, String debitType, String remark) {
        logger.info("newWithhold userId=" + user.getId() + " repaymentId=" + repayment.getId() + " money=" + money);

        ResponseContent serviceResult = new ResponseContent("-101", "支付失败");
        //还款用户银行卡信息
        UserCardInfo info = userService.findUserBankCard(Integer.parseInt(user.getId()));
        // 生成请求编号
        String orderNo = GenerateNo.payRecordNo(user.getId());
        // 生成外部订单
        OutOrders outOrders = getRepayOutOrders(user, repayment.getId(), orderNo, debitType);
        //生成待还信息记录
        RepaymentDetail detail = getRepaymentDetail(repayment, money, orderNo, withholdType, remark, new Date());

        // 组装发送到第三方请求
        Map<String, String> paramMap = preparePayParams(orderNo, user, info, money, "/yeepay/withholdCallback/");
        //发送代扣请求
//        Map<String, Object> resultMap = YeepayApiUtil.httpExecuteResult(paramMap, user.getId(), PayConstants.UNSENDBIND_PAY_REQUEST_URL, "getWithholdRequest");
        Map<String, Object> resultMap = new HashMap<>();
        try{
            resultMap = YeepayApiUtil.yeepayYOP(paramMap, PayConstants.UNSENDBIND_PAY_REQUEST_URL);

        }catch (Exception e){
            logger.error("yeepayWithhold post yeepay error:{}",e);
        }

        outOrders.setReqParams(JSON.toJSONString(paramMap));
        outOrders.setReturnParams(JSON.toJSONString(resultMap));

        // 代扣前处理
        repayService.beforeRepayHandler(outOrders, detail);
        logger.info("orderNo " + orderNo + " status:" + (resultMap == null ? "null" : resultMap.get("status")));
        if (resultMap != null && "PROCESSING".equals(resultMap.get("status"))) {

            return new ResponseContent(ResponseContent.SUCCESS, orderNo);
        } else {
            logger.info("orderNo " + orderNo + " fail");
            serviceResult = new ResponseContent("400", "");
            if (resultMap == null) {
                serviceResult.setMsg("服务器请求失败");
            } else if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
                logger.info("orderNo :" + orderNo + " errorcode is " + resultMap.get("errorcode") + " errormsg is " + resultMap.get("errormsg"));
                if ("TZ0200002".equals(resultMap.get("errorcode"))) {
                    serviceResult.setMsg("银行卡已失效，请重新绑定");
                } else if ("TZ2010032".equals(resultMap.get("errorcode"))) {
                    serviceResult.setMsg("卡内余额不足");
                }
            } else {
                logger.info("orderNo :" + orderNo + " errormsg is " + resultMap.get("errormsg"));
                serviceResult.setMsg(resultMap.containsKey("errormsg") ? resultMap.get("errormsg").toString() : "支付失败，请重试");
            }
            //支付失败
            // 更新外部订单状态为失败
            outOrdersService.updateOrderStatus(orderNo, OutOrders.STATUS_OTHER);
            // 更新还款详情为失败
            repaymentDetailService.updateDetailStatusAndRemark(detail.getId(), RepaymentDetail.STATUS_OTHER, serviceResult.getMsg());
            // 解除锁占用
            repayService.removeRepaymentLock(repayment.getId() + "");
            return serviceResult;
        }

    }

    /**
     * 组装用户代付发送到易宝请求
     *
     * @param orderNo 订单号
     * @param user user
     * @param info cardInfo
     * @param money money
     * @param callBackUrl callBackUrl
     * @return map
     */
    private Map<String, String> preparePayParams(String orderNo, User user, UserCardInfo info, Long money, String callBackUrl) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", PayConstants.MERCHANT_NO);
        dataMap.put("requestno", orderNo);
        dataMap.put("issms", "false");
        dataMap.put("identityid", PropertiesConfigUtil.get("RISK_BUSINESS")+user.getId());
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("requesttime", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dataMap.put("amount", new DecimalFormat("######0.00").format(money / 100.00) + "");
        dataMap.put("productname", PropertiesConfigUtil.get("APP_NAME") + "还款");
        dataMap.put("cardtop", info.getCard_no().substring(0, 6));
        dataMap.put("cardlast", info.getCard_no().substring(info.getCard_no().length() - 4));
        dataMap.put("callbackurl", PropertiesConfigUtil.get("APP_HOST_API") + callBackUrl + user.getId());
        dataMap.put("terminalno", "SQKKSCENEKJ010");
        return dataMap;
    }

    /**
     * 易宝续期处理
     *
     * @param renewalRecord 续期订单
     * @param user user
     * @param money money
     * @param bankId bankId
     * @return res
     * @throws Exception ex
     */
    @Override
    public ResponseContent yeepayRenewalWithhold(RenewalRecord renewalRecord, User user, Long money, Integer bankId) throws Exception {
        logger.info("newRenewalWithhold userId=" + user.getId() + " renewalRecord=" + renewalRecord.getId() + " money=" + money);
        ResponseContent serviceResult = new ResponseContent("-101", "支付失败");

        UserCardInfo info = userService.findBankCardByCardId(bankId);
        //请求编号
        String orderNo = renewalRecord.getOrderId();

        // 生成订单
        OutOrders outOrders = getRepayOutOrders(user, renewalRecord.getAssetRepaymentId(), orderNo, OutOrders.ACT_RENEWAL);
        //构造请求信息
        Map<String, String> paramMap = prepareYeepayReqParams(orderNo, user, info, money, "/yeepay/renewalWithholdCallback/");
        // 设置请求内容
        outOrders.setReqParams(JSON.toJSONString(paramMap));
        //发送代扣请求
        Map<String, Object> resultMap = null;
        try {
//            resultMap = YeepayApiUtil.httpExecuteResult(paramMap, user.getId(), PayConstants.UNSENDBIND_PAY_REQUEST_URL, "getWithholdRequest");
            resultMap = YeepayApiUtil.yeepayYOP(paramMap,PayConstants.UNSENDBIND_PAY_REQUEST_URL);
            outOrders.setReturnParams(JSON.toJSONString(resultMap));
            // 判断是否成功
            if (resultMap != null && "PROCESSING".equals(resultMap.get("status"))) {
                serviceResult = new ResponseContent(ServiceResult.SUCCESS, orderNo);
            } else {
                logger.info("renewalRecord is " + renewalRecord.getId() + " fail");
                serviceResult.setCode("400");
                outOrders.setStatus(OutOrders.STATUS_OTHER);
                if (resultMap == null) {
                    serviceResult.setMsg("服务器请求失败");
                } else if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
                    logger.info("renewalRecord is " + renewalRecord.getId() + " errorcode " + resultMap.get("errorcode"));
                    if ("TZ0200002".equals(resultMap.get("errorcode"))) {
                        serviceResult.setMsg("银行卡已失效，请重新绑定");
                    } else if ("TZ2010032".equals(resultMap.get("errorcode"))) {
                        serviceResult.setMsg("卡内余额不足");
                    } else {
                        serviceResult.setMsg(resultMap.get("errormsg").toString());
                    }
                } else {
                    logger.info("renewalRecord is " + renewalRecord.getId() + " errormsg " + resultMap.get("errormsg"));
                    serviceResult.setMsg(resultMap.containsKey("errormsg") ? resultMap.get("errormsg").toString() : "支付失败，请重试");
                }
                renewalRecordDao.updateStatusAndRemark(renewalRecord.getId(), RenewalRecord.STATUS_FAIL, serviceResult.getMsg());
            }
            outOrdersService.insert(outOrders);
        } catch (Exception e) {
            logger.error("newRenewalWithhold userId=" + user.getId() + " error", e);
            e.printStackTrace();
            throw e;
        }
        return serviceResult;
    }


    /**
     * 易宝主动请求支付处理
     *
     * @param requestNo 请求订单号
     * @param repayment 还款信息
     * @param user      用户
     * @param money     还款金额
     * @param remark    备注
     * @param bankId    银行卡编号
     * @return res
     * @throws Exception ex
     */
    @Override
    public ResponseContent newRecharge(String requestNo, Repayment repayment, User user, Long money, String remark, Integer bankId) throws Exception {
        logger.info("newRecharge userId=" + user.getId() + " repaymentId=" + repayment.getId() + " money=" + money);
        ResponseContent serviceResult = new ResponseContent();
        //还款用户银行卡信息
        UserCardInfo info = userService.findBankCardByCardId(bankId);
        if (!String.valueOf(info.getUserId()).equals(user.getId())) {
            throw new Exception("非本人银行卡");
        }
        // 生成外部订单
        OutOrders outOrders = getRepayOutOrders(user, repayment.getId(), requestNo, "WITHHOLD_CONFIRM");
        //生成待还信息记录
        RepaymentDetail detail = getRepaymentDetail(repayment, money, requestNo, Repayment.CARD_WITHHOLD, remark, new Date());

        // 组织发送第三方内容
        String paramMap = prepareParams(requestNo, user, info, money, "/fuiou/withholdCallback/");
        // 外部订单记录
        outOrders.setReqParams(paramMap);
        //发送支付请求
        Map<String, Object> result = FuiouApiUtil.FuiouOD(paramMap,FuiouConstants.NEW_PROTOCOL_ORDER_URL);
        outOrders.setReturnParams(JSON.toJSONString(result));
        // 发送还款请求到第三方后处理
        repayService.beforeRepayHandler(outOrders, detail);

        if (result != null && "P000".equals(result.get("status"))) {
            serviceResult = new ResponseContent("P000", "收到请求");
        } else if (result != null && "0000".equals(result.get("status"))) {
            //支付成功，提前处理回调
            serviceResult = new ResponseContent("0000", "收到请求");
        } else {
            logger.info("repaymentId " + repayment.getId() + "fail ");
            serviceResult.setCode("400");
            if (result != null && result.containsKey("errorcode") && !"".equals(result.get("errorcode"))) {
                logger.info("repaymentId " + repayment.getId() + " fail ,errorcode is " + result.get("errorcode") + " errormsg is " + result.get("errormsg"));
                serviceResult.setMsg(result.get("errormsg").toString());
            } else {
                serviceResult.setMsg("请求失败，请重试");
            }
            // 更新订单状态为失败
            outOrdersService.updateOrderStatus(requestNo, OutOrders.STATUS_OTHER);
        }
        return serviceResult;
    }


    /**
     * 准备组装易宝代扣请求
     *
     * @return map
     */
    private Map<String, String> prepareYeepayReqParams(String orderNo, User user, UserCardInfo info,
                                                           long money, String callBackUrl) {
        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        Map<String, String> dataMap = new TreeMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", orderNo);
        dataMap.put("issms", "false");
        dataMap.put("identityid", PropertiesConfigUtil.get("RISK_BUSINESS")+user.getId());
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("requesttime", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dataMap.put("amount", new DecimalFormat("######0.00").format(money / 100.00) + "");
        dataMap.put("productname", PropertiesConfigUtil.get("APP_NAME") + "还款");
        dataMap.put("cardtop", info.getCard_no().substring(0, 6));
        dataMap.put("cardlast", info.getCard_no().substring(info.getCard_no().length() - 4));
        dataMap.put("callbackurl", PropertiesConfigUtil.get("APP_HOST_API") + callBackUrl + user.getId());
        dataMap.put("terminalno", "SQKKSCENEKJ010");
//        dataMap.put("terminalid", user.getUserPhone());
//        dataMap.put("registtime", DateUtil.getDateFormat(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
//        dataMap.put("lastloginterminalid", user.getUserPhone());
//        dataMap.put("issetpaypwd", "0");

//        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//        dataMap.put("sign", sign);

        return dataMap;
    }

    /**
     * 生成还款详情信息
     *
     * @param repayment    还款信息
     * @param money        还款金额
     * @param orderNo      订单号
     * @param withholdType 还款类型
     * @param remark       备注
     * @param currentDate  生成时间
     * @return re
     */
    private RepaymentDetail getRepaymentDetail(Repayment repayment, long money, String orderNo,
                                               int withholdType, String remark, Date currentDate) {
        RepaymentDetail detail = new RepaymentDetail();
        //还款用户ID
        detail.setUserId(repayment.getUserId());
        //总还款记录ID
        detail.setAssetRepaymentId(repayment.getId());
        //还款金额
        detail.setTrueRepaymentMoney(money);
        //还款编号
        detail.setOrderId(orderNo);
        //还款方式
        detail.setRepaymentType(withholdType);
        detail.setRemark(remark);
        //还款初始状态
        detail.setStatus(RepaymentDetail.STATUS_WAIT);
        //借款记录ID
        detail.setAssetOrderId(repayment.getAssetOrderId());
        detail.setCreatedAt(currentDate);
        return detail;
    }

    /**
     * 生成还款外部订单
     *
     * @param user        用户
     * @param repaymentId 还款信息
     * @param orderNo     订单号
     * @param debitType type
     * @return orders
     */
    private OutOrders getRepayOutOrders(User user, int repaymentId, String orderNo, String debitType) {
        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(user.getId());
        outOrders.setAssetOrderId(repaymentId);
        outOrders.setOrderType(OutOrders.TYPE_YEEPAY);
        outOrders.setOrderNo(orderNo);
        outOrders.setAct(debitType);
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        return outOrders;
    }

    /**
     * 组装对易宝的支付请求
     *
     * @param orderNo orderNo
     * @param user user
     * @param info info
     * @param money money
     * @param callBackUrl url
     * @return treeMap
     */
    private String prepareParams(String orderNo, User user, UserCardInfo info,
                                                  long money, String callBackUrl) throws Exception {
        NewProtocolOrderXmlBeanReq beanReq = new NewProtocolOrderXmlBeanReq();
        beanReq.setVersion("1.0");
        beanReq.setUserIp(user.getCreateIp());
        beanReq.setMchntCd(FuiouConstants.API_MCHNT_CD);
        beanReq.setType("03");
        beanReq.setMchntOrderId(orderNo);
        beanReq.setUserId(PropertiesConfigUtil.get("RISK_BUSINESS")+user.getId());
        beanReq.setAmt(String.valueOf(money));
        beanReq.setProtocolNo(info.getAgreeno());
        beanReq.setNeedSendMsg("0");
        beanReq.setBackUrl(PropertiesConfigUtil.get("APP_HOST_API") + callBackUrl + user.getId());
        beanReq.setSignTp("MD5");
        beanReq.setSign(FuiouUtil.getSign(beanReq.signStr(FuiouConstants.API_MCHNT_KEY), "MD5", FuiouConstants.privatekey));

        String APIFMS = XMapUtil.toXML(beanReq, FuiouConstants.charset);
        APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

        return APIFMS;
    }
}
