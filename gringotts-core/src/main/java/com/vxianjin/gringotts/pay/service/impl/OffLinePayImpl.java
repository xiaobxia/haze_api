package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.pay.common.enums.ErrorCode;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.model.OffPayResponse;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.*;
import com.vxianjin.gringotts.util.TimeKey;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.RenewalRecord;
import com.vxianjin.gringotts.web.pojo.Repayment;
import com.vxianjin.gringotts.web.pojo.RepaymentDetail;
import com.vxianjin.gringotts.web.service.impl.BorrowOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jintian
 * @date 10:20
 */
@Service("offLinePay")
public class OffLinePayImpl implements OffLinePay {

    private Logger log = LoggerFactory.getLogger(OffLinePayImpl.class);

    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private BorrowOrderService borrowOrderService;

    @Autowired
    private RenewalRecordService renewalRecordService;

    @Autowired
    private RepaymentDetailService repaymentDetailService;

    @Autowired
    private OrderLogService orderLogService;


    @Autowired
    private OrderLogComponent orderLogComponent;

    @Value("#{mqSettings['cs_topic']}")
    private String csTopic;

    @Value("#{mqSettings['cs_renewal_target']}")
    private String csRenewalTarget;

    @Autowired
    private MqInfoService mqInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OffPayResponse offLineRenewal(String renewalRecordJsonString, String serverUser) {
        TimeKey.clear();
        TimeKey.start();
        log.info("renewalRencord:" + renewalRecordJsonString + " serverUser:" + serverUser);
        if (StringUtils.isBlank(renewalRecordJsonString) || StringUtils.isBlank(serverUser)) {
            TimeKey.clear();
            return new OffPayResponse(false, ErrorCode.ERROR_600001.getCode(), ErrorCode.ERROR_600001.getMsg());
        }
        JSONObject renewalRecordJson = JSON.parseObject(renewalRecordJsonString);
        String realRenewalDate = renewalRecordJson.getString("realRenewalDate");

        RenewalRecord rr = JSON.parseObject(renewalRecordJsonString, RenewalRecord.class);

        log.info(MessageFormat.format("renewal repaymentId:{0}", rr.getAssetRepaymentId()));
        Repayment repayment = repaymentService.selectByPrimaryKey(rr.getAssetRepaymentId());


        BorrowOrder bo = borrowOrderService.findOneBorrow(repayment.getAssetOrderId());
        // 待还总金额
        Long waitRepay = repayment.getRepaymentAmount() - repayment.getRepaymentedAmount();
        // 待还滞纳金
        Long waitLate = Long.parseLong(String.valueOf(repayment.getPlanLateFee() - repayment.getTrueLateFee()));
        // 待还本金
        Long waitAmount = waitRepay - waitLate;
        log.info(MessageFormat.format("renewal waitAmount=: {0}waitRepay=: {1}", waitAmount, waitRepay));
        log.info(MessageFormat.format("renewal waitLate=: {0}", waitLate));
        // 续期信息
        rr.setUserId(repayment.getUserId());
        rr.setRepaymentPrincipal(waitAmount);
        rr.setOldRepaymentTime(repayment.getRepaymentTime());
        rr.setRenewalDay(bo.getLoanTerm());
        rr.setStatus(RenewalRecord.STATUS_SUCC);
        rr.setRemark(rr.getRemark() + "  操作员:" + serverUser);
        rr.setMoneyAmount(repayment.getRepaymentPrincipal() + repayment.getRepaymentInterest());

        // 还款日期 延后 （逾期天数+续期天数）
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        log.info("realRenewalDate:" + realRenewalDate);
        if (StringUtils.isNotBlank(realRenewalDate)) {
            try {
                Date repayDate1 = dateFormat1.parse(realRenewalDate);
                //实际还款时间精确到日
                Date repayDate2 = dateFormat2.parse(realRenewalDate);
                //应还款时间 精确到日
                String shouldRepayDateStr = dateFormat2.format(repayment.getRepaymentTime());
                Date shouldRepayDate = dateFormat2.parse(shouldRepayDateStr);

                //用户实际还款时间早于应还款时间 应还款时间上续期
                if (repayDate1.before(repayment.getRepaymentTime())) {
                    rr.setRepaymentTime(DateUtil.addDay(repayment.getRepaymentTime(), bo.getLoanTerm()));
                }
                //用户逾期还款或者应还款当天还款 则按照还款日续期
                if (repayDate2.after(shouldRepayDate) || repayDate2.equals(shouldRepayDate)) {
                    rr.setRepaymentTime(DateUtil.addDay(repayDate1, bo.getLoanTerm()));
                }

            } catch (ParseException e) {
                log.error("createdAtStr happened error:{}", e);
            }
        } else {
            rr.setRepaymentTime(DateUtil.addDay(repayment.getRepaymentTime(), repayment.getLateDay() + bo.getLoanTerm()));
        }
        renewalRecordService.insertSelective(rr);
        log.info(MessageFormat.format("renewal record=:{0}", JSON.toJSONString(rr)));
        BorrowOrder borrowOrder = new BorrowOrder();
        borrowOrder.setId(repayment.getAssetOrderId());
        borrowOrder.setStatus(BorrowOrder.STATUS_HKZ);

        borrowOrderService.updateById(borrowOrder);

        //【2】添加日志流转记录
        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(repayment.getUserId().toString());
        logModel.setOperateId(rr.getId() + "");
        logModel.setBorrowId(repayment.getAssetOrderId() + "");
        logModel.setOperateType(OperateType.BORROW.getCode());
        logModel.setAction(OrderChangeAction.UNLINE_RENEWAL_ACTION.getCode());
        logModel.setBeforeStatus(repayment.getStatus().toString());
        logModel.setAfterStatus(BorrowOrder.STATUS_HKZ.toString());
        logModel.setRemark(OrderChangeAction.UNLINE_RENEWAL_ACTION.getMessage());

        orderLogComponent.addNewOrderLog(logModel);

        Repayment re = new Repayment();
        // 如果申请续期成功
        re.setId(repayment.getId());
        log.info(MessageFormat.format("renewal repaymentAmout=:{0}", (repayment.getRepaymentPrincipal() + repayment.getRepaymentInterest())));
        re.setRepaymentAmount(repayment.getRepaymentPrincipal() + repayment.getRepaymentInterest());//重置待还金额
//				re.setRepaymentAmount(waitAmount);//重置待还金额
        re.setPlanLateFee(0);
        re.setTrueLateFee(0);
        //还款日期
        re.setRepaymentTime(rr.getRepaymentTime());
        re.setLateFeeStartTime(null);
        re.setInterestUpdateTime(null);
        re.setLateDay(0);
        re.setRenewalCount(repayment.getRenewalCount() + 1);
        re.setStatus(BorrowOrder.STATUS_HKZ);
        re.setCollection(Repayment.COLLECTION_NO);
        // TODO 插入流水记录
        //orderLogService.addNewOrderChangeLog(new OrderLogModel(repayment.getUserId() + "", borrowOrder.getId() + "", rr.getId() + "", OrderChangeAction.UNLINE_RENEWAL_ACTION.getCode(), OrderChangeAction.UNLINE_RENEWAL_ACTION.getMessage(), repayment.getStatus().toString(), BorrowOrder.STATUS_HKZ + "", new Date(), "线下续期借款订单变化"));
        repaymentService.updateRenewalByPrimaryKey(re);

        // 原本是和催收系统交互的，和催收系统确认过，该段已不用
        //  tsOrdersService.sendBorrowStatus(repayment.getAssetOrderId());

        // 如果是已逾期的续期（调用催收同步）
        boolean isOverdueRenewal = getIsOverdue(re.getId());
        if (repayment.getStatus().equals(BorrowOrder.STATUS_YYQ) || isOverdueRenewal) {
            // 改成mq推送形式 推送内容 re.getId() RENEWAL_
            mqInfoService.sendMq(csTopic, csRenewalTarget, re.getId() + "");
        }
        TimeKey.clear();
        return new OffPayResponse(true, ErrorCode.ERROR_0000.getCode(), ErrorCode.ERROR_0000.getMsg());
    }

    /**
     * 判断还款是否逾期
     *
     * @param repayId 还款编号
     * @return
     */
    private boolean getIsOverdue(Integer repayId) {
        boolean isOverdue = false;
        Map<String, Object> param = new HashMap<>();
        param.put("assetRepaymentId", repayId);
        param.put("status", RenewalRecord.STATUS_SUCC);
        List<RenewalRecord> records = renewalRecordService.findParams(param);
        Integer planLateFeeAgo;
        if (CollectionUtils.isNotEmpty(records)) {
            for (RenewalRecord renewalRecord : records) {
                planLateFeeAgo = renewalRecord.getPlanLateFee();
                if (planLateFeeAgo != null && planLateFeeAgo > 0) {
                    isOverdue = true;
                    break;
                }
            }
        }
        return isOverdue;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OffPayResponse offLineRepay(String repaymentDetailJsonStr, String serverUser) {
        TimeKey.clear();
        TimeKey.start();
        log.info("repaymentDetail :" + repaymentDetailJsonStr + " serverUser:" + serverUser);
        if (StringUtils.isBlank(repaymentDetailJsonStr) || StringUtils.isBlank(serverUser)) {
            TimeKey.clear();
            return new OffPayResponse(false, ErrorCode.ERROR_600001.getCode(), ErrorCode.ERROR_600001.getMsg());
        }
        RepaymentDetail detail = JSON.parseObject(repaymentDetailJsonStr, RepaymentDetail.class);
        String erroMsg = "";
        if (null == detail.getAssetRepaymentId()) {
            erroMsg = "请选择一条还款数据！";
        } else {
            Repayment repayment = repaymentService.selectByPrimaryKey(detail.getAssetRepaymentId());
            log.info("repay repayment=:{}", (repayment != null ? JSON.toJSONString(repayment) : "null"));
            if (null == repayment) {
                erroMsg = "该还款数据无效！";
            } else if (!BorrowOrder.STATUS_HKZ.equals(repayment.getStatus()) && !BorrowOrder.STATUS_BFHK.equals(repayment.getStatus()) && !BorrowOrder.STATUS_YYQ.equals(repayment.getStatus()) && !BorrowOrder.STATUS_YHZ.equals(repayment.getStatus())) {
                erroMsg = "本条数据不能还款！";
            } else {
                detail.setAdminUsername(serverUser);
                detail.setUserId(repayment.getUserId());
                detail.setUpdatedAt(new Date());
                detail.setStatus(RepaymentDetail.STATUS_SUC);
                detail.setAssetOrderId(repayment.getAssetOrderId());
                // TODO 插入流水记录信息，可以放到repay里面添加
                repaymentDetailService.insertSelective(detail);
                repaymentService.repay(repayment, detail, OrderChangeAction.UNLINE_REPAYL_ACTION.getCode());
                //tsOrdersService.sendBorrowStatus(repayment.getAssetOrderId());
                // 如果是未逾期的还款，调用提额
                if (repayment.getLateDay() == 0) {
//                            User user = userService.searchByUserid(repayment.getUserId());
                    log.info("repay to Mention Money ing ----------------->");
//							borrowOrderService.addUserLimit(user);
                }
            }
        }
        if (StringUtils.isNotBlank(erroMsg)) {
            log.info("erroMsg:" + erroMsg);
            TimeKey.clear();
            return new OffPayResponse(false, "", erroMsg);
        }
        TimeKey.clear();
        return new OffPayResponse(true);
    }
}
