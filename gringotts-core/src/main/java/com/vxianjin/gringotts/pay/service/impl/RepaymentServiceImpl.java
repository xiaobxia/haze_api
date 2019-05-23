package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.tools.mq.producer.CommonProducer;
import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.enums.EventTypeEnum;
import com.vxianjin.gringotts.pay.common.enums.PayRecordStatus;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.publish.PublishAdapter;
import com.vxianjin.gringotts.pay.common.publish.PublishFactory;
import com.vxianjin.gringotts.pay.component.FuiouService;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.dao.IRenewalRecordDao;
import com.vxianjin.gringotts.pay.dao.IRepaymentDao;
import com.vxianjin.gringotts.pay.dao.IRepaymentDetailDao;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.model.GeTuiJson;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPRepayRecordReq;
import com.vxianjin.gringotts.pay.model.YPRepayResultModel;
import com.vxianjin.gringotts.pay.model.fuiou.FuiouRepayResultModel;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.MqInfoService;
import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.pay.service.UserQuotaSnapshotService;
import com.vxianjin.gringotts.pay.service.base.RepayService;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.dao.IIndexDao;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.service.impl.UserClientInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepaymentServiceImpl implements RepaymentService {

    private final static Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);
    @Resource
    private IRepaymentDao repaymentDao;
    @Resource
    private IRenewalRecordDao renewalRecordDao;
    @Resource
    private IBorrowOrderDao borrowOrderDao;
    @Resource
    private IRepaymentDetailDao repaymentDetailDao;
    @Autowired
    private IPaginationDao paginationDao;
    @Autowired
    private IBorrowOrderService borrowOrderService;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IIndexDao indexDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOutOrdersService outOrdersService;
    @Autowired
    private RepaymentDetailService repaymentDetailService;

    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private RepayService repayService;

    @Autowired
    private CommonProducer producer;


    @Autowired
    private FuiouService fuiouService;


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OrderLogComponent orderLogComponent;

    @Value("#{mqSettings['oss_topic']}")
    private String ossMqTopic;

    @Value("#{mqSettings['oss_target']}")
    private String ossMqTarget;

    @Value("#{mqSettings['cs_topic']}")
    private String csMqTopic;

    @Value("#{mqSettings['cs_overdue_target']}")
    private String csOverDueTarget;

    @Value("#{mqSettings['cs_renewal_target']}")
    private String csRenewalTarget;

    @Value("#{mqSettings['cs_repay_target']}")
    private String csRepayTarget;

    @Autowired
    private MqInfoService mqInfoService;

    @Autowired
    private UserClientInfoService userClientInfoService;

    @Autowired
    private UserQuotaSnapshotService userQuotaSnapshotService;




    @Override
    public List<Repayment> findAll(HashMap<String, Object> params) {
        return null;
    }

    @Override
    public List<RepaymentDetail> findDetailsByRepId(Integer id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assetRepaymentId", id);
        return repaymentDetailDao.findParams(params);
    }

    @Override
    public Repayment findOneRepayment(Map<String, Object> params) {
        List<Repayment> repayments = repaymentDao.findParams(params);
        return (null != repayments && repayments.size() > 0) ? repayments.get(0) : null;
    }

    @Override
    public List<Map<String, Object>> findMyLoan(Map<String, Object> params) {
        return repaymentDao.findMyLoan(params);
    }

    @Override
    public Repayment selectByPrimaryKey(Integer id) {
        return repaymentDao.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteByPrimaryKey(Integer id) {
        return repaymentDao.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insert(Repayment repayment) {
        return repaymentDao.insert(repayment) > 0;
    }

    @Override
    public boolean insertSelective(Repayment repayment) {
        return repaymentDao.insertSelective(repayment) > 0;
    }

    @Override
    public boolean updateByPrimaryKey(Repayment repayment) {
        return repaymentDao.updateByPrimaryKey(repayment) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(Repayment repayment) {
        return repaymentDao.updateByPrimaryKeySelective(repayment) > 0;
    }

    @Override
    public PageConfig<Repayment> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "Repayment");
        return paginationDao.findPage("findParams", "findParamsCount", params, "web");
    }

    @Override
    public List<Repayment> findParams(Map<String, Object> params) {
        return repaymentDao.findParams(params);
    }

    @Override
    public int findParamsCount(HashMap<String, Object> params) {
        return repaymentDao.findParamsCount(params);
    }

    private void pushRepayToCs(Repayment re) {
        try {
            //jedisCluster.set("REPAY_" + re.getId(), "" + re.getId());
            // TODO 问过冉阳如果全还款也已经处理过，不推送改对应内容，有待观察
            // jedisCluster.del("OVERDUE_" + re.getId());
            // 通过mq推送消息到cs系统
            mqInfoService.sendMq(csMqTopic, csRepayTarget, String.valueOf(re.getId()));
            logger.info("collection repay success YQYHK REPAY" + re.getId());
        } catch (Exception e) {
            logger.error("collection repay error YQYHK repaymentId=" + re.getId(), e);
        }
    }

    @Override
    public void repay(Repayment re, RepaymentDetail detail, String action) {
        BorrowOrder bo = new BorrowOrder();
        bo.setId(re.getAssetOrderId());
        Repayment copy = new Repayment();
        copy.setId(re.getId());
        copy.setRepaymentedAmount(re.getRepaymentedAmount() + detail.getTrueRepaymentMoney());//用户已还款金额

        User user = userDao.selectCollectionByUserId(re.getUserId());

        //订单修改日志记录
        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(user.getId());
        logModel.setBorrowId(String.valueOf(re.getAssetOrderId()));
        logModel.setOperateId(String.valueOf(detail.getId()));
        logModel.setOperateType(OperateType.REPAYL.getCode());
        logModel.setBeforeStatus(String.valueOf(re.getStatus()));
        logModel.setAction(action);
        logModel.setRemark(OrderChangeAction.valueOf(action).getMessage());

        // 判断是否全部还清
        if (copy.getRepaymentedAmount() >= re.getRepaymentAmount()) {//已还金额 > 还款总金额
            logger.info("userId:" + user.getId() + " has repay all");
            boolean overdueStatus = getIsOverdue(re.getId());
            if (re.getLateDay() > 0) {//逾期天数
                // 逾期已还款 告知催收
                //collection(user, re, detail, Repayment.REPAY_COLLECTION);
                pushRepayToCs(re);
                copy.setStatus(BorrowOrder.STATUS_YQYHK);//逾期还款状态
                bo.setStatus(BorrowOrder.STATUS_YQYHK);//逾期还款状态
                logModel.setAfterStatus(BorrowOrder.STATUS_YQYHK.toString());
            } else {
                if (overdueStatus) {
                    pushRepayToCs(re);
                }
                copy.setStatus(BorrowOrder.STATUS_YHK);
                bo.setStatus(BorrowOrder.STATUS_YHK);
                logModel.setAfterStatus(BorrowOrder.STATUS_YHK.toString());
            }
            if (User.CUSTOMER_NEW.equals(user.getCustomerType())) {
                logger.info("update user to old,userId: " + user.getId());
                // 新用户改为老用户
                userDao.updateUserToOld(user.getId());
            }
            logger.info("prepare update user borrow status,userId: " + user.getId());
            // 全部还款-更新info_user_info borrow_status 状态为不可见
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("USER_ID", user.getId());
            map.put("BORROW_STATUS", "0");
            indexDao.updateInfoUserInfoBorrowStatus(map);

            copy.setRepaymentRealTime(null != detail.getCreatedAt() ? detail.getCreatedAt() : new Date());
            // 防止多个事物问题
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    logger.info("into afterCompletion，status：" + status);
                    if (STATUS_COMMITTED == status) {
                        try {
                            // 更新用户额度
                            userQuotaSnapshotService.updateUserQuotaSnapshots(Integer.valueOf(user.getId()),-1, String.valueOf(copy.getRepaymentedAmount()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void afterCommit() {
                    logger.info("into afterCommit");
                }
            });
        } else {
            if (re.getLateDay() > 0) {
                // 逾期部分还款 告知催收
                try {
                    //jedisCluster.set("OVERDUE_" + re.getId(), "" + re.getId());
                    //  通过mq推送消息到cs系统
                    mqInfoService.sendMq(csMqTopic, csOverDueTarget, String.valueOf(re.getId()));
                    logger.info("collection repay success OVERDUE " + re.getId());
                } catch (Exception e) {
                    logger.error("collection repay error BFHK repaymentId=" + re.getId(), e);
                }
                logModel.setAfterStatus(BorrowOrder.STATUS_YYQ.toString());
            } else {
                copy.setStatus(BorrowOrder.STATUS_BFHK);//部分还款状态
                bo.setStatus(BorrowOrder.STATUS_BFHK);//部分还款状态
                logModel.setAfterStatus(BorrowOrder.STATUS_BFHK.toString());
            }
        }
        if (BorrowOrder.STATUS_BFHK.compareTo(re.getStatus()) == 0 || copy.getRepaymentedAmount() < re.getRepaymentAmount()) {
            switch (OrderChangeAction.valueOf(action)) {
                case REPAYL_ACTION:
                    logModel.setAction(OrderChangeAction.PART_REPAYL_ACTION.getCode());
                    logModel.setRemark(OrderChangeAction.PART_REPAYL_ACTION.getMessage());
                    break;
                case UNLINE_REPAYL_ACTION:
                    logModel.setAction(OrderChangeAction.UNLINE_PART_REPAYL_ACTION.getCode());
                    logModel.setRemark(OrderChangeAction.UNLINE_PART_REPAYL_ACTION.getMessage());
                    break;
                case COLLECTION_JIANMIAN_ACTION:
                    logModel.setAction(OrderChangeAction.COLLECTION_PART_JIANMIAN_ACTION.getCode());
                    logModel.setRemark(OrderChangeAction.COLLECTION_PART_JIANMIAN_ACTION.getMessage());
                    break;
                default:
                    break;
            }
        }
        // 本次还款金额
        Long money = detail.getTrueRepaymentMoney();
        // 已付的逾期费 = 所有还款金额 - 借款金额 - 手续费
        /*Long payedOver = re.getRepaymentedAmount() - (re.getRepaymentInterest() + re.getRepaymentPrincipal());
        money = payedOver > 0 ? money - payedOver : money;*/
        if ((money + re.getRepaymentedAmount()) > re.getPlanLateFee()) {
            // 把用户可借额度加上
            logger.info("prepare addAvailableAmount,money:" + money + " nowAvailableAmount" + String.valueOf(Long.valueOf(user.getAmountMax()) + re.getRepaymentedAmount() + detail.getTrueRepaymentMoney() - re.getRepaymentAmount()));
            User userCopy = new User();
            userCopy.setId(user.getId());
            // 用户剩余可借额度为 总额度 - （需还款 - 已还款总额）
            userCopy.setAmountAvailable(String.valueOf(Long.valueOf(user.getAmountMax()) + re.getRepaymentedAmount() + detail.getTrueRepaymentMoney() -re.getRepaymentAmount()));
            userDao.updateAmountAvailableByUserId(userCopy);
        } else {
            // 还款金额小于0时认为有问题(还款金额不能小于0)
            logger.info("user addAvailableAmount fail , because money < 0,money:" + money);
        }
        if (null != bo.getStatus()) {
            borrowOrderDao.updateByPrimaryKeySelective(bo);
        }
        repaymentDao.updateByPrimaryKeySelective(copy);

        orderLogComponent.addNewOrderLog(logModel);
    }

    @Override
    @Transactional(rollbackFor = PayException.class)
    public void synRepay(final Repayment re, final RepaymentDetail detail, final User user) throws PayException {
        BorrowOrder borrow = new BorrowOrder();
        borrow.setId(re.getAssetOrderId());

        Repayment repayment = new Repayment();
        repayment.setId(re.getId());
        repayment.setRepaymentedAmount(re.getRepaymentedAmount() + detail.getTrueRepaymentMoney());//用户已还款金额

        User userCopy = new User();
        userCopy.setId(user.getId());

        //订单修改日志记录
        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(user.getId());
        logModel.setBorrowId(String.valueOf(re.getAssetOrderId()));
        logModel.setOperateId(String.valueOf(detail.getId()));
        logModel.setOperateType(OperateType.REPAYL.getCode());
        logModel.setBeforeStatus(String.valueOf(re.getStatus()));

        try {
            //【1】更新借款和还款信息
            if (repayment.getRepaymentedAmount() >= re.getRepaymentAmount()) {//已还金额 > 还款总金额
                logModel.setAction(OrderChangeAction.REPAYL_ACTION.getCode());
                if (re.getLateDay() > 0) {//逾期天数
                    // 逾期已还款 告知催收
                    //pushRepayToCs(re);
                    repayment.setStatus(BorrowOrder.STATUS_YQYHK);//逾期还款状态
                    borrow.setStatus(BorrowOrder.STATUS_YQYHK);//逾期还款状态
                    logModel.setAfterStatus(BorrowOrder.STATUS_YQYHK.toString());
                    logModel.setRemark("逾期还款状态");
                } else {
                    boolean overdueStatus = getIsOverdue(re.getId());
                    if (overdueStatus) {
                        pushRepayToCs(re);
                    }
                    repayment.setStatus(BorrowOrder.STATUS_YHK);
                    borrow.setStatus(BorrowOrder.STATUS_YHK);
                    logModel.setAfterStatus(BorrowOrder.STATUS_YHK.toString());
                    logModel.setRemark("已还款");
                }
                borrowOrderDao.updateByPrimaryKeySelective(borrow);

                if (User.CUSTOMER_NEW.equals(user.getCustomerType())) {
                    userCopy.setCustomerType(User.CUSTOMER_OLD);
                }
                // 全部还款-更新info_user_info borrow_status 状态为不可见
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("USER_ID", user.getId());
                map.put("BORROW_STATUS", "0");
                indexDao.updateInfoUserInfoBorrowStatus(map);

                //更新还款信息
                repayment.setRepaymentRealTime(null != detail.getCreatedAt() ? detail.getCreatedAt() : new Date());
                repaymentDao.updateByPrimaryKeySelective(repayment);
                // 防止多个事物问题
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCompletion(int status) {
                        if (STATUS_COMMITTED == status) {
                            try {
                                // 更新用户额度
                                userQuotaSnapshotService.updateUserQuotaSnapshots(Integer.valueOf(user.getId()),-1, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                logModel.setAction(OrderChangeAction.PART_REPAYL_ACTION.getCode());
                if (re.getLateDay() > 0) {
                    // 逾期部分还款, 设置缓存， 告知催收
                    try {
                        //jedisCluster.set("OVERDUE_" + re.getId(), "" + re.getId());
                        //  通过mq推送消息到cs系统
                        mqInfoService.sendMq(csMqTopic, csOverDueTarget, String.valueOf(re.getId()));
                        logger.info("collection repay success OVERDUE" + re.getId());
                    } catch (Exception e) {
                        logger.error("collection repay error BFHK repaymentId=" + re.getId(), e);
                    }
                    logModel.setAfterStatus(BorrowOrder.STATUS_YYQ.toString());
                    logModel.setRemark("逾期部分还款");
                } else {
                    repayment.setStatus(BorrowOrder.STATUS_BFHK);//部分还款状态
                    borrow.setStatus(BorrowOrder.STATUS_BFHK);//部分还款状态
                    repaymentDao.updateByPrimaryKeySelective(repayment);
                    borrowOrderDao.updateByPrimaryKeySelective(borrow);
                    logModel.setAfterStatus(BorrowOrder.STATUS_BFHK.toString());
                    logModel.setRemark("部分还款");
                }
            }
            //【2】更新用户可贷额度信息
            // 本次还款金额
            Long money = detail.getTrueRepaymentMoney();
           /* // 已付的逾期费 = 所有还款金额 - 借款金额 - 手续费
            Long payedOver = re.getRepaymentedAmount() - (re.getRepaymentInterest() + re.getRepaymentPrincipal());
            money = payedOver > 0 ? money - payedOver : money;*/
            if ((money + re.getRepaymentedAmount()) > re.getPlanLateFee()) {
                // 用户剩余可借额度为 总额度 - （需还款 - 已还款总额）
                userCopy.setAmountAvailable(String.valueOf(Long.valueOf(user.getAmountMax()) + re.getRepaymentedAmount() + detail.getTrueRepaymentMoney() -re.getRepaymentAmount()));
                logger.info("用户额度变回成功userId=" + userCopy.getId() + ",amountAvailable=" + userCopy.getAmountAvailable());
            } else {
                logger.info("用户额度变回失败userId=" + userCopy.getId() + ",repaymentedAmount=" + re.getRepaymentedAmount() + ",trueRepaymentMoney=" + detail.getTrueRepaymentMoney());
            }
            if (StringUtils.isNotBlank(userCopy.getCustomerType()) || StringUtils.isNotBlank(userCopy.getAmountAvailable())) {
                userDao.updateAmountAvailableByUserId(userCopy);
                logger.info("用户额度保存成功userId=" + userCopy.getId() + ",amountAvailable=" + userCopy.getAmountAvailable());
            } else {
                logger.info("用户额度保存失败userId=" + userCopy.getId() + ",customerType=" + userCopy.getCustomerType() + ",amountAvailable=" + userCopy.getAmountAvailable());
                throw new PayException("synRepay 用户额度变回失败");
            }
        } catch (PayException e) {
            throw new PayException("synRepay 更新用户借款信息异常");
        }

        orderLogComponent.addNewOrderLog(logModel);
    }

    private boolean getIsOverdue(Integer repayId) {
        boolean isOverdue = false;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("assetRepaymentId", repayId);
        param.put("status", RenewalRecord.STATUS_SUCC);
        List<RenewalRecord> records = renewalRecordDao.findParams(param);
        Integer planLateFeeAgo = null;
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

    /**
     * 续期
     *
     * @param repayment
     * @param record
     * @return
     */
    @Override
    public void renewal(Repayment repayment, RenewalRecord record) {
        if (null == repayment) {
            repayment = this.selectByPrimaryKey(record.getAssetRepaymentId());
        }
        // 更新续期为成功
        record.setStatus(RenewalRecord.STATUS_SUCC);
        renewalRecordDao.updateByPrimaryKeySelective(record);

        BorrowOrder borrowOrder = new BorrowOrder();
        borrowOrder.setId(repayment.getAssetOrderId());
        borrowOrder.setStatus(BorrowOrder.STATUS_HKZ);
        borrowOrderService.updateById(borrowOrder);

        Repayment re = new Repayment();
        // 如果申请续期成功
        re.setId(repayment.getId());
        re.setRepaymentAmount(repayment.getRepaymentPrincipal() + repayment.getRepaymentInterest());
//		re.setPlanLateFee(repayment.getRepaymentAmount().intValue() - repayment.getPlanLateFee());
        re.setPlanLateFee(0);
        re.setTrueLateFee(0);
        // 还款日期 延后 （上期还款时间 + 逾期天数+续期天数）
        re.setRepaymentTime(DateUtil.addDay(DateUtil.addDay(repayment.getRepaymentTime(), repayment.getLateDay()), record.getRenewalDay()));
        re.setLateFeeStartTime(null);
        re.setInterestUpdateTime(null);
        re.setLateDay(0);
        re.setRenewalCount(repayment.getRenewalCount() + 1);
        re.setStatus(BorrowOrder.STATUS_HKZ);
        re.setCollection(Repayment.COLLECTION_NO);
        this.updateRenewalByPrimaryKey(re);

        //订单修改日志记录
        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(String.valueOf(repayment.getUserId()));
        logModel.setBorrowId(String.valueOf(repayment.getAssetOrderId()));
        logModel.setOperateId(String.valueOf(record.getId()));
        logModel.setOperateType(OperateType.RENEWAL.getCode());
        logModel.setAction(OrderChangeAction.RENEWAL_ACTION.getCode());
        logModel.setBeforeStatus(String.valueOf(repayment.getStatus()));
        logModel.setAfterStatus(String.valueOf(BorrowOrder.STATUS_HKZ));
        logModel.setRemark("续期成功");

        orderLogComponent.addNewOrderLog(logModel);

        // 如果是已逾期的续期（调用催收同步）
        boolean isOverdue = getIsOverdue(re.getId());
        Integer planLateFee = record.getPlanLateFee();
        if (planLateFee != null && planLateFee > 0 || isOverdue) {
            try {
                //jedisCluster.set("RENEWAL_" + re.getId(), "" + re.getId());
                //   通过mq推送消息到cs系统
                mqInfoService.sendMq(csMqTopic, csRenewalTarget, String.valueOf(re.getId()));
                logger.info("collection renewal success RENEWAL_" + re.getId());
            } catch (Exception e) {
                logger.error("collection renewal error repaymentId=" + re.getId(), e);
            }
        }
    }


    @Override
    public void updateRenewalByPrimaryKey(Repayment re) {
        repaymentDao.updateRenewalByPrimaryKey(re);
    }

    @Override
    public List<Repayment> findAllByBorrowId(Integer borrowId) {
        return repaymentDao.findAllByBorrowId(borrowId);
    }

    @Override
    public Map<String, Object> findBorrowLoanTerm(Integer borrowId) {
        return repaymentDao.findBorrowLoanTerm(borrowId);
    }

    @Override
    public boolean insertByBorrorOrder(BorrowOrder borrowOrder) {
        try {
            int count = repaymentDao.queryCountByAssetOrderId(String.valueOf(borrowOrder.getId()));
            if (count > 0) {
                logger.error(MessageFormat.format("还款记录repayment 已存在,assetorderid = {0}", borrowOrder.getId()));
            } else {
                Date fkDate = borrowOrder.getLoanTime();
                Repayment repayment = new Repayment();
                try {
                    repayment.setUserId(borrowOrder.getUserId());
                    repayment.setAssetOrderId(borrowOrder.getId());
                    repayment.setRepaymentAmount(Long.valueOf(borrowOrder.getMoneyAmount()));
                    repayment.setLateFeeApr(borrowOrder.getLateFeeApr());
                    repayment.setRepaymentedAmount(0L);
                    repayment.setRepaymentPrincipal(Long.valueOf(borrowOrder.getIntoMoney()));
                    repayment.setRepaymentInterest(Long.valueOf(borrowOrder.getLoanInterests()));
                    // 放款时间加上借款期限
                    repayment.setFirstRepaymentTime(borrowOrder.getLoanEndTime());
                    // 放款时间加上借款期限
                    repayment.setRepaymentTime(borrowOrder.getLoanEndTime());
                    repayment.setCreditRepaymentTime(fkDate);


                    repayment.setCreatedAt(fkDate);
                    repayment.setStatus(borrowOrder.getStatus());
                    repaymentDao.insertSelective(repayment);
                } catch (DuplicateKeyException e) {
                    logger.error(MessageFormat.format("还款记录repayment 插入重复数据,assetorderid = {0}", borrowOrder.getId()));
                } catch (Exception e) {
                    logger.error(MessageFormat.format("还款记录repayment 插入数据失败,assetorderid = {0}", borrowOrder.getId()));
                }
            }

            final String userPhone = borrowOrder.getUserPhone();
            final double intoMoney = (borrowOrder.getIntoMoney() / 100.00);

            String content = PropertiesConfigUtil.get("APP_NAME") + "##" + intoMoney;
            // 将打款消息通过mq推送给运营平台
            //producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(4, content)));

            try {
                PublishAdapter publishAdapter = PublishFactory.getPublishAdapter("PAY");
                publishAdapter.publishMsg(applicationContext, EventTypeEnum.PAY.getCode(), content, userPhone);
            } catch (PayException e) {
                logger.error(MessageFormat.format("用户{0},打款提示短信发送失败====>e : {1}", userPhone, e.getMessage()));
            }
        } catch (Exception e) {
            logger.error("create repayment insertByBorrorOrder", e);
        }
        return true;
    }


    @Override
    public PageConfig<Repayment> findWriteOffPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "Repayment");
        return paginationDao.findPage("findRepaymentParams", "findParamsCountPrecise", params, "web");
    }

    @Override
    @Transactional(rollbackFor = PayException.class)
    public void synReapymentDetailStatus(RepaymentDetail repaymentDetail) {
        logger.info(MessageFormat.format("======同步用户{0}还款订单{1}状态开始=====", repaymentDetail.getUserId(), repaymentDetail.getOrderId()));

        String orderNo = repaymentDetail.getOrderId();

        if (StringUtils.isBlank(orderNo) || repaymentDetail.getUserId() == null) {
            logger.error(MessageFormat.format("synReapymentDetailStatus  asset_repayment_detail.id = {0},订单编号为空", repaymentDetail.getId()));
            throw new PayException("订单编号为空或用户编号为空");
        }
        //【1】校验订单是否存在
        OutOrders outOrders = outOrdersService.findByOrderNo(orderNo);
        if (null == outOrders || null == outOrders.getAssetOrderId()) {
            logger.error(MessageFormat.format("synReapymentDetailStatus  asset_repayment_detail.id= {0},订单不存在", repaymentDetail.getId()));
            throw new PayException("订单不存在");
        }

        //【2】查询易宝订单支付结果
        YPRepayRecordReq repayRecordReq = new YPRepayRecordReq();
        repayRecordReq.setMerchantNo(PayConstants.MERCHANT_NO);
        repayRecordReq.setRequestNo(orderNo);

        ResultModel<FuiouRepayResultModel> resultModel = fuiouService.getFuiouRepayResult(repayRecordReq, String.valueOf(repaymentDetail.getUserId()));
        if (!resultModel.isSucc()) {
            logger.error(MessageFormat.format("synReapymentDetailStatus  asset_repayment_detail.id= {0},无法从易宝查询到该笔订单明细", repaymentDetail.getId()));
            throw new PayException("无法从富友查询到该笔订单明细，请联系易宝客服");
        }

        FuiouRepayResultModel fuiouRepayResultModel = resultModel.getData();
        /*if (!repaymentDetail.getOrderId().equals(fuiouRepayResultModel.getRequestNo())) {
            logger.error(MessageFormat.format("synReapymentDetailStatus  asset_repayment_detail.id= {0},请求易宝的订单号与捞取的订单号不一致", repaymentDetail.getId()));
            throw new PayException("请求富友的订单号与捞取的订单号不一致");
        }*/

        String status = fuiouRepayResultModel.getStatus();
        User user = userDao.selectCollectionByUserId(repaymentDetail.getUserId());
        Repayment re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());//获取还款记录
        try {
            //【2】同步订单状态
            String act = outOrders.getAct();
            switch (PayRecordStatus.getByCode(status)) {
                case PAY_SUCCESS://支付成功
                    if ((!OutOrders.STATUS_SUC.equals(outOrders.getStatus()))) {
                        if (RepaymentDetail.STATUS_SUC != repaymentDetail.getStatus()) {
                            repaymentDetail.setStatus(RepaymentDetail.STATUS_SUC);//代扣成功状态
                            repaymentDetail.setRemark("REPAY_DEBIT".equals(act) ? "易宝主动代扣回调支付成功" : "TASK_DEBIT".equals(act) ? "定时代扣回调支付成功" : "催收代扣回调支付成功");
                            repaymentDetailService.updateByPrimaryKey(repaymentDetail);
                            logger.info(MessageFormat.format("同步用户{0}还款repaymentDetail {1}成功", repaymentDetail.getUserId(), repaymentDetail.getOrderId()));
                            //同步还款操作
                            synRepay(re, repaymentDetail, user);
                        }
                        outOrders.setStatus(OutOrders.STATUS_SUC);
                        outOrdersService.updateByOrderNo(outOrders);
                        //发送还款成功通知短信
                        sendRepaySuccessSms(user.getUserPhone(), user.getRealname(), re.getRepaymentAmount(), user.getId());
                    }
                    break;
                case FAIL:
                case TIME_OUT:
                case PROCESSING:
                case TO_VALIDATE:
                case ACCEPT:
                    break;
                case PAY_FAIL:
                    String errorMsg = resultModel.getMessage();
                    re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());//获取还款记录
                    repaymentDetail.setStatus(RepaymentDetail.STATUS_OTHER);
                    repaymentDetail.setRemark("REPAY_DEBIT".equals(act) ? "主动代扣回调:" + errorMsg : "TASK_DEBIT".equals(act) ? "定时代扣回调:" + errorMsg : "催收代扣回调:" + errorMsg);
                    outOrders.setStatus(OutOrders.STATUS_OTHER);
                    repaymentDetailService.updateByPrimaryKey(repaymentDetail);
                    break;
                default:
                    break;
            }
        } catch (PayException e) {
            throw new PayException(e.getMessage());
        } finally {
            //交易成功或者失败，删除锁定的该笔还款订单，防止其他渠道不能还款
            repayService.removeRepaymentLock(re.getId() + "");
            logger.info("UcfpayController withholdCallback end");
        }
    }

    private void sendRepaySuccessSms(final String phone, final String realName, final long amount, final String userIdStr) {

        String content = realName + "##" + (amount / 100);
        String userClientId = userClientInfoService.queryClientIdByUserId(Integer.valueOf(userIdStr));
        String mqMsg = "您好，您的" + (amount / 100) + "元借款已经还款成功，该笔交易将有助于提升您的信用额。";
        logger.info("prepared send mqMsg:" + mqMsg + " userClientId:" + userClientId);
        //  通过mq推送消息给运营平台
        producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(2, userClientId, "还款成功", mqMsg, mqMsg)));

        try {
            PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.REPAY.getCode());
            publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), content, phone);
        } catch (PayException e) {
            logger.error(MessageFormat.format("用户{0},还款成功短信发送失败====>e :{1}", phone, e.getMessage()));
        }
    }


    private void sendRepayFailSms(final String phone, final String realName, final long amount, final Integer userId) {
        UserCardInfo info = userService.findUserBankCard(userId);//还款用户银行卡信息
        String content = MessageFormat.format("尊敬的{0}，您的{1}借款今日到期，请至APP还款，若到期未还款，平台将自动扣款，请确保尾号{2}银行卡资金充足。如已还款，请忽略。", realName, (amount / 100), info.getCard_no().substring(info.getCard_no().length() - 4));
        //   通过mq推送消息到运营平台，待定需不需要推送，暂定为放款成功类型
        //producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(2, content)));

        try {
            PublishAdapter publishAdapter = PublishFactory.getPublishAdapter("REPAY");
            publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), content, phone);
        } catch (PayException e) {
            logger.error(MessageFormat.format("用户{0},还款提示短信发送失败====>e : {1}", phone, e.getMessage()));
        }
    }

}
