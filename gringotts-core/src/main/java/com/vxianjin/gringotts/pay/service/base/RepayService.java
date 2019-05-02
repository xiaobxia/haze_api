package com.vxianjin.gringotts.pay.service.base;

import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.model.NeedRenewalInfo;
import com.vxianjin.gringotts.pay.model.NeedRepayInfo;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.service.impl.RepaymentServiceImpl;
import com.vxianjin.gringotts.risk.service.OutOrdersService;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.*;

/**
 * 还款业务处理类
 * Created by jintian on 2018/7/17.
 */
@Service
public class RepayService {

    private final static Logger logger = LoggerFactory.getLogger(RepayService.class);

    @Autowired
    private OutOrdersService outOrdersService;

    @Autowired
    private RepaymentServiceImpl repaymentService;

    @Autowired
    private RepaymentDetailService repaymentDetailService;

    @Autowired
    private IBorrowOrderService borrowOrderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RenewalRecordService renewalRecordService;

    @Autowired
    private OrderLogComponent orderLogComponent;

    /**
     * 还款缓存key
     */
    private static final String WITHHOLD_KEYS = "REPAYMENT_REPAY_WITHHOLD";

    /**
     * 续期缓存key
     * 和还款key的前缀相同，防止续期和代扣同时触发，先不合并到同一个字段中
     */
    private static final String RENEWAL_KEYS = "REPAYMENT_REPAY_WITHHOLD";
    //private static final String RENEWAL_KEYS = "RENEWAL_WITHHOLD_KEY";

    @Autowired
    private JedisCluster jedisCluster;


    /**
     * 代扣回调处理
     *
     * @param repayment       还款情况
     * @param repaymentDetail 换款详情
     * @param outOrders       外部订单
     * @param payMoney        支付金额
     * @param status          状态
     * @param type            支付类型
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void repayCallBackHandler(Repayment repayment, RepaymentDetail repaymentDetail, OutOrders outOrders, String payMoney, boolean status, String errorMsg, String type,User user) {
        String act = outOrders.getAct();
        if (status) {
            // 如果订单详情不存在则创建一条订单详情
            if (repaymentDetail != null) {
                repaymentDetail.setStatus(RepaymentDetail.STATUS_SUC);
                if ("支付宝".equals(type)) {
                    repaymentDetail.setRemark(type + "还款回调：支付成功");
                } else {
                    repaymentDetail.setRemark(type + (act.equals("REPAY_DEBIT") ? "主动代扣回调支付成功" : act.equals("TASK_DEBIT") ? "定时代扣回调支付成功" : "催收代扣回调支付成功"));
                }
                // 记录还款信息，并销账0
                repaymentDetailService.updateByPrimaryKey(repaymentDetail);
            } else {
                repaymentDetail = new RepaymentDetail();
                repaymentDetail.setUserId(repayment.getUserId());
                repaymentDetail.setAssetRepaymentId(repayment.getId());
                repaymentDetail.setTrueRepaymentMoney(Long.valueOf(payMoney) * 100);
                repaymentDetail.setCreatedAt(outOrders.getAddTime());
                repaymentDetail.setOrderId(outOrders.getOrderNo());
                if ("支付宝".equals(type)) {
                    repaymentDetail.setRepaymentType(RepaymentDetail.TYPE_ALIPAY);
                    repaymentDetail.setRemark(type + "还款回调：支付成功");
                } else {
                    repaymentDetail.setRepaymentType(act.equals("REPAY_DEBIT") ? RepaymentDetail.TYPE_BANK_CARD_AUTO : RepaymentDetail.TYPE_BANK_CARD_AUTO);
                    repaymentDetail.setRemark(type + (act.equals("REPAY_DEBIT") ? "主动代扣回调支付成功" : act.equals("TASK_DEBIT") ? "定时代扣回调支付成功" : "催收代扣回调支付成功"));
                }

                repaymentDetail.setUserId(repayment.getUserId());
                repaymentDetail.setUpdatedAt(new Date());
                repaymentDetail.setStatus(RepaymentDetail.STATUS_SUC);
                repaymentDetail.setAssetOrderId(repayment.getAssetOrderId());
                repaymentDetailService.insertSelective(repaymentDetail);
            }
            outOrders.setStatus(OutOrders.STATUS_SUC);
            // 更新用户的额度，未还清则推送催收系统
            repaymentService.repay(repayment, repaymentDetail,OrderChangeAction.REPAYL_ACTION.getCode());
        } else {
            repaymentDetail.setStatus(RepaymentDetail.STATUS_OTHER);
            if (!"支付宝".equals(type)) {
                repaymentDetail.setRemark(act.equals("REPAY_DEBIT") ? "主动代扣回调:" + errorMsg : act.equals("TASK_DEBIT") ? "定时代扣回调:" + errorMsg : "催收代扣回调:" + errorMsg);
            }
            outOrders.setStatus(OutOrders.STATUS_OTHER);
            repaymentDetailService.updateByPrimaryKey(repaymentDetail);
        }

        outOrdersService.updateByOrderNo(outOrders);
        // 解除订单锁定
        jedisCluster.del(WITHHOLD_KEYS + "_" + repayment.getId());
    }

    /**
     * 续期回调处理(错误的时候设置错误完整报文要在外面设置)
     *
     * @param outOrders     外部订单
     * @param renewalRecord
     * @param repayment     还款情况
     * @param status        还款状态
     * @param type          类型（也就是渠道：支付宝，易宝等）
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void continuCallBackHandler(OutOrders outOrders, RenewalRecord renewalRecord, Repayment repayment, boolean status, String errorMsg, String type) {

        if (status) {
            //续期处理
            if (outOrders != null && renewalRecord != null && repayment != null
                    && !RenewalRecord.STATUS_SUCC.equals(renewalRecord.getStatus())) {
                OutOrders outOrderNew = new OutOrders();
                outOrderNew.setOrderNo(outOrders.getOrderNo());
                outOrderNew.setStatus(OutOrders.STATUS_SUC);
                outOrdersService.updateByOrderNo(outOrderNew);
                renewalRecord.setStatus(RenewalRecord.STATUS_SUCC);
                renewalRecord.setRemark(type + "续期回调支付：支付成功");
                repaymentService.renewal(repayment, renewalRecord);
            }
        } else {
            if (!RenewalRecord.STATUS_SUCC.equals(renewalRecord.getStatus())) {
                OutOrders outOrderNew = new OutOrders();
                outOrderNew.setOrderNo(outOrders.getOrderNo());
                outOrderNew.setStatus(OutOrders.STATUS_OTHER);
                outOrdersService.updateByOrderNo(outOrderNew);
                renewalRecord.setStatus(RenewalRecord.STATUS_FAIL);
                renewalRecord.setRemark(errorMsg);
                renewalRecordService.updateByPrimaryKeySelective(renewalRecord);

                //订单状态修改日志记录更新
                OrderLogModel logModel = new OrderLogModel();
                logModel.setUserId(outOrders.getUserId());
                logModel.setBorrowId(repayment.getAssetOrderId().toString());
                logModel.setOperateType(OperateType.RENEWAL.getCode());
                logModel.setAction(OrderChangeAction.RENEWAL_ACTION.getCode());
                logModel.setOperateId(renewalRecord.getId().toString());
                logModel.setBeforeStatus(String.valueOf(repayment.getStatus()));
                logModel.setAfterStatus(String.valueOf(repayment.getStatus()));
                logModel.setRemark(type + "续期回调支付：付款失败");
                orderLogComponent.addNewOrderLog(logModel);
            }
        }
        jedisCluster.del(WITHHOLD_KEYS + "_" + repayment.getId());

    }

    /**
     * 获取主动还款相关信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public NeedRepayInfo getNeedRepayInfo(int id) throws BizException {
        NeedRepayInfo needRepayInfo = new NeedRepayInfo();

        //获取借款订单
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        if (bo == null) {
            throw new BizException("-101", "没有找到该笔订单");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        //获取还款记录
        Repayment re = repaymentService.findOneRepayment(map);
        if (re == null) {
            throw new BizException("-101", "没有找到该笔订单还款信息");
        }
        //获取还款用户信息
        User user = userService.searchByUserid(bo.getUserId());
        String flag = jedisCluster.get(WITHHOLD_KEYS + "_" + re.getId());
        if ("true".equals(flag)) {//验证该笔订单是否正在处理中
            throw new BizException("-101", "交易正在处理中，请勿频繁操作");
        }
        //非已放款、非部分还款、非已逾期、非已坏账状态条件下不能还款
        if (!STATUS_HKZ.equals(re.getStatus()) && !STATUS_BFHK.equals(re.getStatus()) && !STATUS_YYQ.equals(re.getStatus())
                && !STATUS_YHZ.equals(re.getStatus())) {
            throw new BizException("-101", "本条还款不支持还款");
        }
        //实际还款金额（还款总额-已还总额）
        Long money = re.getRepaymentAmount() - re.getRepaymentedAmount();
        if (money > 0) {
            needRepayInfo.setNeedRepay(true);
        } else {
            needRepayInfo.setNeedRepay(false);
            throw new BizException("-101", "本条还款不支持还款");
        }

        needRepayInfo.setBorrowOrder(bo);
        needRepayInfo.setMoney(money);
        needRepayInfo.setRepayment(re);
        needRepayInfo.setUser(user);
        return needRepayInfo;
    }

    /**
     * 获取主动还款相关信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public NeedRepayInfo getNeedRepayInfoByPaymentId(int id) throws BizException {
        NeedRepayInfo needRepayInfo = new NeedRepayInfo();
        //获取还款记录
        Repayment re = repaymentService.selectByPrimaryKey(id);
        if (re == null) {
            throw new BizException("-101", "没有找到该笔订单还款信息");
        }
        //获取还款用户信息
        User user = userService.searchByUserid(re.getUserId());
        String flag = jedisCluster.get(WITHHOLD_KEYS + "_" + re.getId());
        if ("true".equals(flag)) {//验证该笔订单是否正在处理中
            throw new BizException("-101", "交易正在处理中，请勿频繁操作");
        }
        //非已放款、非部分还款、非已逾期、非已坏账状态条件下不能还款
        if (!STATUS_HKZ.equals(re.getStatus()) && !STATUS_BFHK.equals(re.getStatus()) && !STATUS_YYQ.equals(re.getStatus())
                && !STATUS_YHZ.equals(re.getStatus())) {
            throw new BizException("-101", "本条还款不支持还款");
        }
        //实际还款金额（还款总额-已还总额）
        Long money = re.getRepaymentAmount() - re.getRepaymentedAmount();
        if (money > 0) {
            needRepayInfo.setNeedRepay(true);
        } else {
            needRepayInfo.setNeedRepay(false);
            throw new BizException("-101", "本条还款不支持还款");
        }

        needRepayInfo.setMoney(money);
        needRepayInfo.setRepayment(re);
        needRepayInfo.setUser(user);
        return needRepayInfo;
    }

    /**
     * 获取续期相关信息
     *
     * @return
     * @throws BizException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public NeedRenewalInfo getNeedRenewalInfo(int id) throws BizException {
        NeedRenewalInfo needRenewalInfo = new NeedRenewalInfo();

        BorrowOrder bo = borrowOrderService.findOneBorrow(id);//获取借款订单信息

        // 续期手续费 分为单位
        BigDecimal renewalFee = bo.getRenewalPoundage();
        if (bo == null) {
            throw new BizException("-101", "没有找到该笔订单");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment re = repaymentService.findOneRepayment(map);//获取还款订单
        if (re == null) {
            throw new BizException("-101", "没有找到该笔订单的还款信息");
        }
        // 待还总金额（总还款金额 - 已还款金额）
        Long waitRepay = re.getRepaymentAmount() - re.getRepaymentedAmount();
        // 待还滞纳金（总滞纳金 - 已还滞纳金）
        Long waitLate = Long.parseLong(String.valueOf(re.getPlanLateFee() - re.getTrueLateFee()));
        // 待还本金
        Long waitAmount = waitRepay - waitLate;
        // 续期费 分为单位
        Integer loanApr = bo.getRenewalFee().intValue();

        //总服务费（待还总金额 + 待还滞纳金 + 服务费）
        Long allCount = waitLate + loanApr + renewalFee.longValue();

        User user = userService.searchByUserid(bo.getUserId());//借款用户信息

        //锁定该笔还款，防止其他渠道重复续期，锁定时间：30分钟
        String flag = jedisCluster.get(WITHHOLD_KEYS + "_" + re.getId());
        if ("true".equals(flag)) {//验证该笔订单是否正在处理中
            throw new BizException("-101", "正在续期处理中，请勿频繁操作");
        }

        //非已放款 && 非部分还款 && 非已逾期 && 非已坏账
        if (!STATUS_HKZ.equals(re.getStatus()) && !STATUS_BFHK.equals(re.getStatus()) && !STATUS_YYQ.equals(re.getStatus())
                && !STATUS_YHZ.equals(re.getStatus())) {
            throw new BizException("-101", "本条还款不支持续期");
        }

        needRenewalInfo.setUser(user);
        needRenewalInfo.setRepayment(re);
        needRenewalInfo.setBorrowOrder(bo);
        needRenewalInfo.setAllCount(allCount);
        needRenewalInfo.setLoanApr(loanApr);
        needRenewalInfo.setRenewalFee(String.valueOf(renewalFee.intValue()));
        needRenewalInfo.setWaitAmount(waitAmount);
        needRenewalInfo.setWaitLate(waitLate);
        needRenewalInfo.setWaitRepay(waitRepay);
        return needRenewalInfo;
    }

    /**
     * 添加订单锁定
     * 默认5分钟
     *
     * @param repaymentId
     */
    public void addRepaymentLock(String repaymentId) {
        jedisCluster.setex(WITHHOLD_KEYS + "_" + repaymentId, 5 * 60, "true");
    }

    /**
     * 获取订单锁定值
     *
     * @param repaymentId
     * @return
     */
    public String getRepaymentLock(String repaymentId) {
        return jedisCluster.get(WITHHOLD_KEYS + "_" + repaymentId);
    }

    /**
     * 解除订单锁定(涉及到key锁定解锁地方放一个类中，便于修改)
     */
    public void removeRepaymentLock(String repaymentId) {
        jedisCluster.del(WITHHOLD_KEYS + "_" + repaymentId);
    }

    /**
     * 添加续期锁定
     * 默认10分钟
     */
    public void addRenewalLock(String key) {
        jedisCluster.setex(RENEWAL_KEYS + "_" + key, 10 * 60, "true");
    }

    /**
     * 获取续期锁内容
     *
     * @param key
     * @return
     */
    public String getRenewalLock(String key) {
        return jedisCluster.get(RENEWAL_KEYS + "_" + key);
    }

    /**
     * 解除续期锁
     *
     * @param key
     */
    public void removeRenewalLock(String key) {
        jedisCluster.del(RENEWAL_KEYS + "_" + key);
    }

    /**
     * 还款请求发送给第三方前业务处理
     * 没必要细化到订单生成和还款详情，只需关注业务流程，这里只是个流程处理
     *
     * @param outOrders 外部订单
     * @param detail    还款明细
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void beforeRepayHandler(OutOrders outOrders, RepaymentDetail detail) {
        // 外部订单入库
        outOrdersService.insert(outOrders);
        // 还款明细入库
        repaymentDetailService.insertSelective(detail);
    }

    /**
     * 续期请求发送到第三方前处理
     * 生成续期订单入库
     *
     * @param needRenewalInfo
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public RenewalRecord beforeRenewalHandler(NeedRenewalInfo needRenewalInfo, String orderNo) {
        // 续期信息记录
        RenewalRecord renewalRecord = new RenewalRecord();
        renewalRecord.setOrderId(orderNo);
        renewalRecord.setUserId(needRenewalInfo.getRepayment().getUserId());//续期用户ID
        renewalRecord.setAssetRepaymentId(needRenewalInfo.getRepayment().getId());//还款记录ID
        renewalRecord.setRepaymentPrincipal(needRenewalInfo.getWaitAmount());//待还本金
        renewalRecord.setSumFee(needRenewalInfo.getAllCount());//总服务费
        renewalRecord.setRepaymentInterest((long) needRenewalInfo.getLoanApr());//服务率
        renewalRecord.setPlanLateFee(needRenewalInfo.getWaitLate().intValue());//滞纳金
        renewalRecord.setRenewalFee(Integer.valueOf(needRenewalInfo.getRenewalFee()));//续期费
        renewalRecord.setOldRepaymentTime(needRenewalInfo.getRepayment().getRepaymentTime());//续期前预期还款时间
        renewalRecord.setRenewalDay(needRenewalInfo.getBorrowOrder().getLoanTerm());//续期天数
        renewalRecord.setStatus(RenewalRecord.STATUS_PAYING);//付款中状态
        renewalRecord.setMoneyAmount(needRenewalInfo.getRepayment().getRepaymentPrincipal() + needRenewalInfo.getRepayment().getRepaymentInterest());//借款总金额
//        renewalRecord.setRepaymentTime(DateUtil.addDay(re.getRepaymentTime(), bo.getLoanTerm()));//续期后预期还款时间
        renewalRecord.setRepaymentTime(DateUtil.addDay(DateUtil.addDay(needRenewalInfo.getRepayment().getRepaymentTime(), needRenewalInfo.getRepayment().getLateDay()), renewalRecord.getRenewalDay()));//续期后预期还款时间
        renewalRecordService.insertSelective(renewalRecord);
        return renewalRecord;
    }

    /**
     * 续期业务处理
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void renewalHandler() {

    }
}
