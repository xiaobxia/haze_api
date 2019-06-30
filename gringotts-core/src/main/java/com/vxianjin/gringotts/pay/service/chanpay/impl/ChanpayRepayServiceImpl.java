package com.vxianjin.gringotts.pay.service.chanpay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ServiceResult;
import com.vxianjin.gringotts.constant.CollectionConstant;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.enums.EventTypeEnum;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.publish.PublishAdapter;
import com.vxianjin.gringotts.pay.common.publish.PublishFactory;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseConstant;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseParameter;
import com.vxianjin.gringotts.pay.common.util.chanpay.ChanPayUtil;
import com.vxianjin.gringotts.pay.component.ChanpayService;
import com.vxianjin.gringotts.pay.dao.IRenewalRecordDao;
import com.vxianjin.gringotts.pay.model.NeedRenewalInfo;
import com.vxianjin.gringotts.pay.model.NeedRepayInfo;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.chanpay.ChanpayResult;
import com.vxianjin.gringotts.pay.model.chanpay.TradeNotify;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.pay.service.base.RepayService;
import com.vxianjin.gringotts.pay.service.chanpay.ChanpayRepayService;
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
import com.vxianjin.gringotts.web.utils.GsonUtil;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 还款续期相关
 * @author zed
 * @date 2018/7/18 20:26
 */
@Service
public class ChanpayRepayServiceImpl implements ChanpayRepayService {
    private static final Logger logger = LoggerFactory.getLogger(ChanpayRepayServiceImpl.class);

    @Resource
    private JedisCluster jedisCluster;

    @Resource
    private IUserDao userDao;

    @Resource
    private IRenewalRecordDao renewalRecordDao;

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

    @Resource
    private ChanpayService chanpayService;

    private final String appCode = PropertiesConfigUtil.get("RISK_BUSINESS");

    // 生成手机验证码频繁标识key
    private static String WITHHOLD_SMS_CODE = "WITHHOLD_SMS_CODE_";

    @Value("#{mqSettings['oss_topic']}")
    private String ossMqTopic;

    @Value("#{mqSettings['oss_target']}")
    private String ossMqTarget;

    /**
     * 代扣回调（主动支付）
     *
     * @param tradeNotify
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payWithholdCallback(TradeNotify tradeNotify) throws Exception {
        logger.info("ChanpayRepayServiceImpl.payWithholdCallback callbackResult=" + (tradeNotify != null ? JSON.toJSONString(tradeNotify) : "null"));
        if (ChanPayUtil.verify(GsonUtil.toJson(tradeNotify), BaseConstant.MERCHANT_PUBLIC_KEY)) {//验签
            String orderNo = tradeNotify.getOuter_trade_no();
            String chanpayOrderId = tradeNotify.getInner_trade_no();
            String orderMoney = tradeNotify.getTrade_amount();
            // 获取外部订单
            OutOrders outOrders = outOrdersService.findByOrderNo(orderNo);
            if (outOrders == null) throw new Exception("系统异常");

            logger.info("ChanpayRepayService.payWithholdCallback orderNo: " + orderNo + ",outOrdersStatus=" + (outOrders != null ? outOrders.getStatus() : "null"));
            // 暂时还是以判断成功是否处理，看代码里请求代扣如果超时等情况会改订单为失败，但是可能第三方收到请求代扣成功了
            if (null != outOrders && (!OutOrders.STATUS_SUC.equals(outOrders.getStatus()))) {
                if (null != outOrders.getAssetOrderId()) {
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
                    outOrders.setFuiouOrderId(chanpayOrderId);
                    if ("trade_status_sync".equals(tradeNotify.getNotify_type()) && "TRADE_SUCCESS".equals(tradeNotify.getTrade_status())) {//交易成功
                        logger.info("ChanpayRepayServiceImpl.payWithholdCallback orderNo:" + orderNo + " pay success");
                        // 还款回调处理
                        repayService.repayCallBackHandler(re, detail, outOrders, orderMoney, true, "", "畅捷通",user);
                        //发送扣款成功短信
                        String content = realName + "##" + (amount / 100.00);

                        String key = "LOAN_"+ outOrders.getUserId();
                        jedisCluster.del(key);

                        try {
                            PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.REPAY.getCode());
                            publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), content, phone);
                        } catch (PayException e) {
                            logger.error(MessageFormat.format("用户{0},还款成功短信发送失败====>e :{1}", phone, e.getMessage()));
                        }
                    } else if ("pay_status_sync".equals(tradeNotify.getNotify_type()) && "PAY_FAIL".equals(tradeNotify.getStatus())) {//支付失败
                        logger.info("ChanpayRepayServiceImpl.payWithholdCallback orderNo: " + orderNo + " pay fail");
                        outOrders.setNotifyParams(JSON.toJSONString(tradeNotify));
                        // 还款回调处理
                        repayService.repayCallBackHandler(re, detail, outOrders, orderMoney, false, tradeNotify.getPay_msg(), "畅捷通",user);
                    }
                }
            } else {
                throw new Exception("该笔订单已经处理");
            }
            logger.info("payWithholdCallback order is " + orderNo );
        }
    }

    /**
     * 续期回调（主动支付）
     * @param tradeNotify req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payRenewalWithholdCallback(TradeNotify tradeNotify) throws Exception {
        ResultModel resultModel = new ResultModel(false);
        logger.info("payRenewalWithholdCallback callbackResult=" + (tradeNotify != null ? JSON.toJSONString(tradeNotify) : "null"));
        if (ChanPayUtil.verify(GsonUtil.toJson(tradeNotify), BaseConstant.MERCHANT_PUBLIC_KEY)) {//验签
            //支付编号
            String orderNo = tradeNotify.getOuter_trade_no();
            //畅捷通订单号
            String chanpayOrderId = tradeNotify.getInner_trade_no();

            OutOrders outOrders = outOrdersService.findByOrderNo(orderNo);
            if (outOrders == null) throw new Exception("系统异常");
            logger.info("payRenewalWithholdCallback orderNo;" + orderNo + " outOrdersStatus=" + (outOrders != null ? outOrders.getStatus() : "null"));

            if (null != outOrders && (!OutOrders.STATUS_SUC.equals(outOrders.getStatus()))) {
                outOrders.setFuiouOrderId(chanpayOrderId);
                if (null != outOrders.getAssetOrderId()) {
                    logger.info("renewalWithholdCallback orderNo;" + orderNo + " assetOrderId is" + outOrders.getAssetOrderId());
                    //获取还款记录
                    Repayment re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());
                    RenewalRecord renewalRecord = renewalRecordService.getRenewalRecordByOrderId(orderNo);

                    if ("trade_status_sync".equals(tradeNotify.getNotify_type()) && "TRADE_SUCCESS".equals(tradeNotify.getTrade_status())) {//交易成功
                        logger.info("renewalWithholdCallback success order_no=" + outOrders.getOrderNo());
                        repayService.continuCallBackHandler(outOrders, renewalRecord, re, true, "", "畅捷通");
                    } else if ("pay_status_sync".equals(tradeNotify.getNotify_type()) && "PAY_FAIL".equals(tradeNotify.getStatus())) {//支付失败
                        logger.info("renewalWithholdCallback false no_order=" + orderNo);
                        repayService.continuCallBackHandler(outOrders, renewalRecord, re, false, tradeNotify.getPay_msg(), "畅捷通");
                    }
                }
            } else {
                throw new Exception("该笔订单已经处理");
            }
            logger.info("renewalWithholdCallback end,order " + orderNo + " result : " + JSON.toJSONString(resultModel));
        }
    }

    /**
     * 主动支付
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
            logger.info("prepare send to chanpay , repaymentId " + needRepayInfo.getRepayment().getId());
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
            if ("0000".equals(serviceResult.getCode())) {
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
     * 续期-主动支付
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent renewalWithhold(Integer id, String payPwd, Long money, String bankIdStr, String sgd) throws Exception {
        logger.info("renewalWithhold borrowId=" + id + " payPwd=" + payPwd + " money=" + money);

        ResponseContent result;
        // 获取续期相关信息
        NeedRenewalInfo needRenewalInfo = repayService.getNeedRenewalInfo(id);

        Integer bankId;
        //如果没传则使用默认卡
        if (StringUtils.isEmpty(bankIdStr)) {
            UserCardInfo userBankCard = userService.findUserBankCard(needRenewalInfo.getBorrowOrder().getUserId());
            bankId = userBankCard.getId();
        } else {
            bankId = Integer.valueOf(bankIdStr);
        }
        // 判断费用是否更新
        if (!needRenewalInfo.getAllCount().equals(money)) {
            return new ResponseContent("-101", "您的费用已更新，请刷新当前页面");
        }
        if (StringUtils.isNotBlank(sgd)) {
            Map<String, String> params = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
            sgd = AESUtil.decrypt(sgd, params.get("TG_SERVER_KEY"));
            if (!String.valueOf(id).equals(sgd)) {
                return new ResponseContent("-101", "参数非法！");
            }
        }
        // 生成续期单号
        //续期订单号
        String orderNo = GenerateNo.payRecordNo(needRenewalInfo.getUser().getId());
        RenewalRecord renewalRecord = repayService.beforeRenewalHandler(needRenewalInfo, orderNo);
        //发送请求
        try {
            //锁定该笔续期，防止其他渠道重复续期，锁定时间：30分钟
            repayService.addRenewalLock(needRenewalInfo.getRepayment().getId() + "");

            ResponseContent serviceResult = chanpayRenewalWithhold(renewalRecord, needRenewalInfo.getUser(), needRenewalInfo.getAllCount(), bankId);
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
     * 定时代扣（主动支付）
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent autoWithhold(Integer id) throws Exception {
        logger.info("autoWithhold start id=" + (id != null ? id : "null"));
        logger.info("autoWithhold repaymentId=" + id);
        ResponseContent result;
        // 获取还款相关信息
        NeedRepayInfo needRepayInfo = repayService.getNeedRepayInfoByPaymentId(id);

        try {
            logger.info("prepare send to chanpay repaymentId is " + needRepayInfo.getRepayment().getId());
            // 先占锁
            repayService.addRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            //发起定时代扣请求，并获取结果。（参数：还款信息、还款用户信息、定时代扣、还款额度、还款操作）
            result = chanpayWithhold(needRepayInfo.getRepayment(), needRepayInfo.getUser(), Repayment.TASK_WITHHOLD, needRepayInfo.getMoney(), OutOrders.ACT_TASK_DEBIT, "定时代扣");
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
                repaymentService.synReapymentDetailStatus(detail);
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
            return new ResponseContent("-101", "代扣失败,验证签名失败");
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
            logger.info("prepare send to chanpay repaymentId is " + re.getId());
            //锁定该笔还款，防止其他渠道重复扣款，锁定时间：5分钟
            repayService.addRepaymentLock(re.getId() + "");

            //发起催收代扣请求，并获取结果。（参数：还款信息、还款用户信息、催收代扣、还款额度、还款操作）
            result = chanpayWithhold(re, user, Repayment.COLLECTION_WITHHOLD, money, OutOrders.ACT_COLLECTION_DEBIT, "催收代扣");
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
     * 畅捷通代扣请求处理
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
    public ResponseContent chanpayWithhold(Repayment repayment, User user, int withholdType, Long money, String debitType, String remark) throws Exception {
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
        Map<String, String> paramMap = prepareParams(orderNo, user, info, money, "/chanpay/withholdCallback/");
        //发送代扣请求
        try{
            serviceResult = new ResponseContent("400", "");
            String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);
            if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {
                ChanpayResult chanpayResult = JSONObject.parseObject(result, ChanpayResult.class);

                outOrders.setReqParams(JSON.toJSONString(paramMap));
                outOrders.setReturnParams(result);

                // 代扣前处理
                repayService.beforeRepayHandler(outOrders, detail);

                logger.info("orderNo " + orderNo + " status:" + (chanpayResult == null ? "null" : chanpayResult.getStatus()));
                if (chanpayResult != null && ("S".equals(chanpayResult.getStatus()) || "P".equals(chanpayResult.getStatus())) && ChanPayUtil.SUCCESSCODE.contains(chanpayResult.getAppRetcode())) {
                    return new ResponseContent(ResponseContent.SUCCESS, orderNo);
                } else {
                    logger.info("orderNo " + orderNo + " fail");
                    if (result == null || chanpayResult == null) {
                        serviceResult.setMsg("服务器请求失败");
                    } else {
                        logger.info("orderNo :" + orderNo + " errormsg is " + chanpayResult.getAppRetMsg());
                        serviceResult.setMsg(chanpayResult.getAppRetMsg() != null ? chanpayResult.getAppRetMsg() : "支付失败，请重试");
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
        }catch (Exception e){
            logger.error("chanpayWithhold post chanpay error:{}",e);
        }
        return serviceResult;
    }

    /**
     * 畅捷通续期处理
     *
     * @param renewalRecord 续期订单
     * @param user user
     * @param money money
     * @param bankId bankId
     * @return res
     * @throws Exception ex
     */
    @Override
    public ResponseContent chanpayRenewalWithhold(RenewalRecord renewalRecord, User user, Long money, Integer bankId) throws Exception {
        logger.info("newRenewalWithhold userId=" + user.getId() + " renewalRecord=" + renewalRecord.getId() + " money=" + money);
        ResponseContent serviceResult = new ResponseContent("-101", "支付失败");

        UserCardInfo info = userService.findBankCardByCardId(bankId);
        //请求编号
        String orderNo = renewalRecord.getOrderId();

        // 生成订单
        OutOrders outOrders = getRepayOutOrders(user, renewalRecord.getAssetRepaymentId(), orderNo, OutOrders.ACT_RENEWAL);
        //构造请求信息
        Map<String, String> paramMap = prepareParams(orderNo, user, info, money, "/chanpay/renewalWithholdCallback/");
        // 设置请求内容
        outOrders.setReqParams(JSON.toJSONString(paramMap));
        //发送代扣请求
        try {
            String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);
            //Map<String, Object> resultMap = FuiouApiUtil.FuiouOD(paramMap,FuiouConstants.NEW_PROTOCOL_ORDER_URL);
            outOrders.setReturnParams(result);
            if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {

                ChanpayResult chanpayResult = JSONObject.parseObject(result, ChanpayResult.class);

                // 判断是否成功
                if (chanpayResult != null && ("S".equals(chanpayResult.getStatus()) || "P".equals(chanpayResult.getStatus())) && ChanPayUtil.SUCCESSCODE.contains(chanpayResult.getAppRetcode())) {
                    serviceResult = new ResponseContent(ServiceResult.SUCCESS, orderNo);
                } else {
                    serviceResult = new ResponseContent("400", "");
                    logger.info("renewalRecord is " + renewalRecord.getId() + " fail");
                    outOrders.setStatus(OutOrders.STATUS_OTHER);
                    if (result == null || chanpayResult == null) {
                        serviceResult.setMsg("服务器请求失败");
                    } else {
                        logger.info("renewalRecord is " + renewalRecord.getId() + " errormsg " + chanpayResult.getAppRetMsg());
                        serviceResult.setMsg(chanpayResult.getAppRetMsg() != null ? chanpayResult.getAppRetMsg() : "支付失败，请重试");
                    }
                    renewalRecordDao.updateStatusAndRemark(renewalRecord.getId(), RenewalRecord.STATUS_FAIL, serviceResult.getMsg());
                }
                outOrdersService.insert(outOrders);
            }
        } catch (Exception e) {
            logger.error("newRenewalWithhold userId=" + user.getId() + " error", e);
            e.printStackTrace();
            throw e;
        }
        return serviceResult;
    }


    /**
     * 畅捷通主动请求支付处理 主动支付
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
        Map<String, String> paramMap = prepareParams(requestNo, user, info, money, "/chanpay/withholdCallback/");
        // 外部订单记录
        outOrders.setReqParams(JSON.toJSONString(paramMap));

        //请求第三方之前，数据先存入库
        repayService.beforeRepayHandler(outOrders, detail);

        String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);

        // 发送还款请求到第三方后处理
        OutOrders afterOutOrders = new OutOrders();
        afterOutOrders.setOrderNo(outOrders.getOrderNo());
        afterOutOrders.setReturnParams(JSON.toJSONString(result));
        outOrdersService.updateByOrderNo(afterOutOrders);

        if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {

            ChanpayResult chanpayResult = JSONObject.parseObject(result, ChanpayResult.class);

            // 判断是否成功
            if (chanpayResult != null && ("S".equals(chanpayResult.getStatus()) || "P".equals(chanpayResult.getStatus())) && ChanPayUtil.SUCCESSCODE.contains(chanpayResult.getAppRetcode())) {
                serviceResult = new ResponseContent("0000", "收到请求");
            } else {
                serviceResult.setCode("400");
                logger.info("renewalRecord is " + repayment.getId() + " fail");
                outOrders.setStatus(OutOrders.STATUS_OTHER);
                if (result == null || chanpayResult == null) {
                    serviceResult.setMsg("服务器请求失败，请重试");
                } else {
                    logger.info("repaymentId is " + repayment.getId() + "errorcode is " + chanpayResult.getAppRetcode() + " errormsg " + chanpayResult.getAppRetMsg());
                    serviceResult.setMsg(chanpayResult.getAppRetMsg() != null ? chanpayResult.getAppRetMsg() : "支付失败，请重试");
                }
                // 更新订单状态为失败
                outOrdersService.updateOrderStatus(requestNo, OutOrders.STATUS_OTHER);
            }
        }

        return serviceResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent directRepaymentWithholdRequest(Integer id, String bankIdStr) throws Exception {
        logger.info("directRepaymentWithholdRequest params:【borrowId：{}, bankId：{}】", id, bankIdStr);
        ResponseContent result = null;
        //绑卡请求编号
        if (id == null || StringUtils.isBlank(id.toString())) {
            return new ResponseContent("-101", "请求参数非法");
        }
        // 获取还款相关信息
        NeedRepayInfo needRepayInfo = repayService.getNeedRepayInfo(id);

        Integer bankId;
        //如果没传则使用默认卡
        if (StringUtils.isEmpty(bankIdStr)) {
            UserCardInfo userBankCard = userService.findUserBankCard(needRepayInfo.getBorrowOrder().getUserId());
            bankId = userBankCard.getId();
            logger.info("params bankIdStr is null,BankId :" + bankId);
        } else {
            bankId = Integer.valueOf(bankIdStr);
        }
        logger.info("directRepaymentWithholdRequest bankCardId:" + bankId);
        //生成还款编号请求号（唯一，且整个还款过程中保持不变）
        String requestNo = GenerateNo.nextOrdId();
        //发起主动代扣请求，并获取结果。（参数：还款编号、还款信息、还款用户信息、还款额度、还款操作）
        ResponseContent serviceResult = newRecharge(requestNo, needRepayInfo.getRepayment(), needRepayInfo.getUser(), needRepayInfo.getMoney(), "主动支付", bankId);

        logger.info("directRepaymentWithholdRequest userId=" + needRepayInfo.getUser().getId() + " serviceResult=" + JSON.toJSONString(serviceResult));
        if ("0000".equals(serviceResult.getCode())) {
            result = new ResponseContent("0", requestNo);
            //存入redis里，避免发送过于频繁
            checkForFront(WITHHOLD_SMS_CODE, requestNo + needRepayInfo.getUser().getUserPhone(), 60);
        } else {
            logger.info("directRepaymentWithholdRequest borrowId:" + id + " fail to request");
            result = new ResponseContent(serviceResult.getCode(), serviceResult.getMsg());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent directRepaymentWithholdConfirm(Integer id, String smsCode, String requestNo) throws Exception {
        logger.info("repaymentWithholdConfirm params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + "】");
        ResponseContent serviceResult = null;
        // 获取还款相关信息
        NeedRepayInfo needRepayInfo = repayService.getNeedRepayInfo(id);

        try {
            logger.info("prepare send to yeepay , repaymentId " + needRepayInfo.getRepayment().getId());
            // 占锁
            repayService.addRepaymentLock(needRepayInfo.getRepayment().getId() + "");

            String orderNo = GenerateNo.nextOrdId();
            Map<String, String> paramMap = BaseParameter.requestBaseParameter(BaseParameter.NMG_API_QUICK_PAYMENT_SMSCONFIRM);
            paramMap.put("TrxId", orderNo);
            paramMap.put("OriPayTrxId", requestNo);
            paramMap.put("SmsCode", smsCode);

            OutOrders outOrders = new OutOrders();
            outOrders.setUserId(String.valueOf(needRepayInfo.getBorrowOrder().getUserId()));
            outOrders.setOrderType("CHANPAY");
            outOrders.setOrderNo(GenerateNo.nextOrdId());
            outOrders.setAct("WITHHOLD_CONFIRM");
            outOrders.setReqParams(JSON.toJSONString(paramMap));
            outOrders.setStatus(OutOrders.STATUS_WAIT);
            outOrdersService.insert(outOrders);

            //发起主动支付确认，并获取结果
            String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);

            logger.info("repaymentWithholdConfirm userId=" + needRepayInfo.getUser().getId() + " serviceResult=" + result);

            if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {

                ChanpayResult chanpayResult = JSONObject.parseObject(result, ChanpayResult.class);

                // 判断是否成功
                if (chanpayResult != null && ("S".equals(chanpayResult.getStatus()) || "P".equals(chanpayResult.getStatus())) && ChanPayUtil.SUCCESSCODE.contains(chanpayResult.getAppRetcode())) {
                    serviceResult = new ResponseContent("0", orderNo);
                } else {
                    serviceResult.setCode("400");
                    if (result == null || chanpayResult == null) {
                        serviceResult.setMsg("服务器请求失败，请重试");
                    } else {
                        serviceResult.setMsg(chanpayResult.getAppRetMsg() != null ? chanpayResult.getAppRetMsg() : "支付失败，请重试");
                    }
                    //支付失败状态下，解除该笔订单锁定状态
                    repayService.removeRepaymentLock(needRepayInfo.getRepayment().getId() + "");
                    // 更新订单状态为失败
                    outOrdersService.updateOrderStatus(orderNo, OutOrders.STATUS_OTHER);
                }
            }
        } catch (Exception e) {
            repayService.removeRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            throw e;
        }
        return serviceResult;
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
        outOrders.setOrderType(OutOrders.TYPE_FUYOU);
        outOrders.setOrderNo(orderNo);
        outOrders.setAct(debitType);
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        return outOrders;
    }

    /**
     * 组装对畅捷通的支付请求
     *
     * @param orderNo orderNo
     * @param user user
     * @param info info
     * @param money money
     * @param callBackUrl url
     * @return treeMap
     */
    private Map<String, String> prepareParams(String orderNo, User user, UserCardInfo info,
                                                  long money, String callBackUrl) throws Exception {
        //Map<String, String> paramsMap;
        //判断银行卡是不是畅捷通的商户签约卡
        /*if (!"CHANPAY_CARD".equals(info.getAgreeno())) {
            paramsMap = BaseParameter.requestBaseParameter(BaseParameter.NMG_ZFT_API_QUICK_PAYMENT);
            paramsMap.put("BkAcctTp", "01");
            paramsMap.put("IDTp", "01");
            paramsMap.put("BkAcctNo", ChanPayUtil.encrypt(info.getCard_no(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            paramsMap.put("IDNo", ChanPayUtil.encrypt(user.getIdNumber(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            paramsMap.put("CstmrNm", ChanPayUtil.encrypt(user.getRealname(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
            paramsMap.put("MobNo", ChanPayUtil.encrypt(user.getUserName(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
        }*/
        Map<String, String> paramsMap = BaseParameter.requestBaseParameter(BaseParameter.NMG_BIZ_API_QUICK_PAYMENT);
        paramsMap.put("CardBegin", info.getCard_no().substring(0, 6));// 卡号前6位
        paramsMap.put("CardEnd", info.getCard_no().substring(info.getCard_no().length() - 4));// 卡号后4位
        paramsMap.put("SmsFlag", "0");
        paramsMap.put("TrxId", orderNo);// 订单号
        paramsMap.put("OrdrName", PropertiesConfigUtil.get("APP_NAME") + "还款");// 商品名称
        paramsMap.put("MerUserId", appCode + user.getId());// 用户标识（测试时需要替换一个新的meruserid）
        paramsMap.put("SellerId", PropertiesConfigUtil.get("PARTNER_ID"));// 子账户号
        paramsMap.put("ExpiredTime", "30m");// 订单有效期
        if (!"online".equals(PropertiesConfigUtil.get("profile"))) {
            paramsMap.put("TrxAmt", "0.01");
        } else {
            paramsMap.put("TrxAmt", BigDecimal.valueOf(money).divide(BigDecimal.valueOf(100)).setScale(2).toString());
        }
        paramsMap.put("TradeType", "11");// 交易类型
        paramsMap.put("NotifyUrl", PropertiesConfigUtil.get("APP_HOST_API") + callBackUrl + user.getId());
        return paramsMap;
    }

}
