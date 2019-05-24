package com.vxianjin.gringotts.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.dao.IRepaymentDao;
import com.vxianjin.gringotts.pay.model.BackExtend;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.ThreadPool;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.*;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IInfoIndexService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.util.SendSmsUtil;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangjb
 * @version V1.0
 * @ClassName: BorrowService.java
 * @Description: TODO
 * @Date 2016年12月12日 下午7:22:00
 */
@Service
public class BorrowOrderService implements IBorrowOrderService {
    private static Logger logger = LoggerFactory.getLogger(IBorrowOrderService.class);

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    IBackConfigParamsDao backConfigParamsDao;

    @Autowired
    @Qualifier("userBankDaoImpl")
    private IUserBankDao userBankDao;

    @Autowired
    private IBorrowOrderDao borrowOrderDao;
    @Autowired
    private IPaginationDao paginationDao;

    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserLimitRecordDao userLimitRecordDao;

    @Autowired
    private IRepaymentDao repaymentDao;
    @Autowired
    private IInfoIndexService infoIndexService;

    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private IUserService userService;

//    @Autowired
//    private CommonProducer producer;

//    @Value("#{mqSettings['oss_topic']}")
//    private String ossMqTopic;
//
//    @Value("#{mqSettings['oss_target']}")
//    private String ossMqTarget;

    @Autowired
    private UserClientInfoService userClientInfoService;

    @Autowired
    private BorrowProductConfigDao borrowProductConfigDao;

    @Autowired
    private OrderLogComponent orderLogComponent;

    @Autowired
    private IBackExtendDao backExtendDao;

    @Override
    public List<BorrowOrder> findAll(HashMap<String, Object> params) {
        return borrowOrderDao.findParams(params);
    }

    @Override
    public BorrowOrder findOneBorrow(Integer id) {
        return borrowOrderDao.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public void insert(BorrowOrder borrowOrder) {
    }

    @Override
    public void updateById(BorrowOrder borrowOrder) {
        borrowOrderDao.updateByPrimaryKeySelective(borrowOrder);
    }

    @Override
    public PageConfig<BorrowOrder> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "BorrowOrder");
        return paginationDao.findPage("findParams", "findParamsCount", params, "web");
    }

    @Override
    public int findParamsCount(HashMap<String, Object> params) {
        return borrowOrderDao.findParamsCount(params);
    }

    @Override
    public void updateRiskCreditUserById(RiskCreditUser riskCreditUser) {
        borrowOrderDao.updateRiskCreditUserById(riskCreditUser);
    }

    /*@Override
    public Map<String, Object> saveLoan(Map<String, String> params, User user) {
        Map<String, Object> result = new HashMap<>();

        //校验用户是否存在借款订单
        if(checkUserBorrowOrder(user.getId())){
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "您当前有处理中的借款, 请不要重复提交");
            return result;
        }
        logger.info("saveLoan userId=" + user.getId() + " params=" + JSON.toJSONString(params));

//        HashMap<String, Object> param = new HashMap<>();
//        param.put("userId", user.getId()); //
//        param.put("type", "2"); // 借记卡
        String bankId = params.get("bankId");

        //获取用户银行卡信息
        UserCardInfo cardInfo = getUserCardInfo(bankId,user.getId());
        if (cardInfo == null) {
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "请先绑定银行卡");
            return result;
        }

        //获取滞纳金/借款服务费率走向，用户拆分服务费
        Map<String, String> intervalMap = SysCacheUtils.getConfigMap(BackConfigParams.SYS_FEE);
        Integer lateApr = Integer.valueOf(intervalMap.get("fee_lateapr"));
        // 查询银行信息
        Map<String, String> bankInfo = userBankDao.selectByPrimaryKey(cardInfo.getBank_id());
        Integer bank_iscmb = 2; // 是否是招商银行 ，默认：1：是，2：否
        if (bankInfo != null && "0308".equals(bankInfo.get("bankCode"))) {
            bank_iscmb = 1;
        }

        //设备类型1、Android 2、iOS 3、wap
        String clientType = params.get("clientType");
        if (StringUtils.isBlank(clientType)) {
            clientType = "1";
        } else {
            if ("android".equals(clientType)) {
                clientType = "1";
            } else if ("ios".equals(clientType)) {
                clientType = "2";
            } else if ("wap".equals(clientType)) {
                clientType = "3";
            } else {
                clientType = "1";
            }
        }
        logger.info("saveLoan clientType = " + clientType);

        //合同ID
        String contractId = StringUtils.killnull(params.get("contract_id"));

        // 计算服务费率
        Integer money = Integer.parseInt(params.get("money")) * 100; // 借款金额,单位：分
        //查询当前用户的借款和期限对应的费率配置
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("borrowAmount",String.valueOf(money));
        query.put("borrowDay",params.get("period"));
        BorrowProductConfig config =  borrowProductConfigDao.selectByBorrowMoneyAndPeriod(query);

        if (config == null){
            logger.error(MessageFormat.format("产品线配置不存在，借款金额{0},期限{1}",money,params.get("period")));
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "产品线配置不存在");
            return result;
        }
        if (config.getTotalFeeRate() == null || config.getBorrowAmount() == null){
            logger.error(MessageFormat.format("产品线ID：{0}配置异常",config.getId()));
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "产品线配置异常，请联系管理员");
            return result;
        }


        Integer loanInterest = config.getTotalFeeRate().intValue();
        //借款利率,万分之一
        Integer borrowRate = config.getTotalFeeRate().divide(config.getBorrowAmount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("10000")).intValue();
        Integer intoMoney = money - config.getTotalFeeRate().intValue();//借款金额 - 服务费金额

        Date date = new Date();
        BorrowOrder bo = new BorrowOrder();
        bo.setUserId(Integer.parseInt(user.getId()));//用户ID
        bo.setMoneyAmount(money);//借款金额
        bo.setApr(borrowRate);//借款利率
        bo.setLoanInterests(loanInterest);//借款利息
        bo.setIntoMoney(intoMoney);//实际打款金额
        bo.setLoanTerm(Integer.parseInt(params.get("period")));//借款期限

        //asset_borrow_order新增续期费、续期手续费字段,老版本默认195 20
        bo.setRenewalFee(new BigDecimal("19500"));
        bo.setRenewalPoundage(new BigDecimal("2000"));

        bo.setCreatedAt(date);//订单创建时间
        bo.setUpdatedAt(date);
        bo.setOrderTime(date);//下单时间
        bo.setLateFeeApr(lateApr);//滞纳金利率，单位为万分之几
        bo.setReceiveCardId(cardInfo.getBank_id());//打款银行卡ID
        bo.setUserPhone(user.getUserPhone());//放款用户手机
        bo.setRealname(user.getRealname());//放款用户姓名
        bo.setBankNumber(bankInfo.get("bankNumber"));//银行卡代号
        bo.setCardNo(cardInfo.getCard_no());//银行卡号
        bo.setBankIscmb(bank_iscmb);//是否是招行卡
        bo.setYurref(GenerateNo.payRecordNo("A"));//订单
        bo.setCustomerType(Integer.parseInt(user.getCustomerType()));//是否是老用户：0、新用户；1；老用户
        bo.setIdNumber(user.getIdNumber());//放款用户身份证号
        bo.setSerialNo(GenerateNo.generateShortUuid(10));//订单编号
        bo.setClientType(clientType);
        bo.setCfcaContractId(contractId);
        borrowOrderDao.insertSelective(bo);

        //设备指纹
        BorrowOrderDevice borrowOrderDevice = new BorrowOrderDevice();
        borrowOrderDevice.setAssetBorrowOrderId(bo.getId());
        borrowOrderDevice.setUserId(Integer.parseInt(user.getId()));
        borrowOrderDevice.setCreateAt(date);
        borrowOrderDevice.setUpdateAt(date);
        borrowOrderDao.insertBorrowOrderDevice(borrowOrderDevice);

        //同盾反欺诈设备指纹
        String tdDeviceString = params.get("td_device");
        logger.info("saveLoan 同盾反欺诈设备指纹 tdDeviceString = " + tdDeviceString);
        BorrowOrderTdDevice borrowOrderTdDevice = new BorrowOrderTdDevice();
        if (StringUtils.isNotBlank(tdDeviceString)) {
            borrowOrderTdDevice.setDeviceContent(tdDeviceString);
        }
        borrowOrderTdDevice.setAssetBorrowOrderId(bo.getId());
        borrowOrderTdDevice.setUserId(Integer.parseInt(user.getId()));
        borrowOrderTdDevice.setCreateAt(date);
        borrowOrderTdDevice.setUpdateAt(date);
        logger.info("saveLoan borrowOrderTdDevice = " + JSON.toJSONString(borrowOrderTdDevice));
        borrowOrderDao.insertBorrowOrderTdDevice(borrowOrderTdDevice);


        //老版本，剩余可借额度，调整为取用户最多可借额度为1000 - 用户借款额度
        Integer amountAvailable = Integer.parseInt(user.getAmountAvailable());
        amountAvailable =  amountAvailable.compareTo(new Integer("1000")) > 0? 1000:amountAvailable;
        User upd = new User();
        upd.setAmountAvailable(String.valueOf(amountAvailable - money/100));//更新可用借款额度
        upd.setId(user.getId());
        userDao.updateAmountAvailableByUserId(upd);

        result.put("code", ResponseStatus.SUCCESS.getName());
        result.put("msg", ResponseStatus.SUCCESS.getValue());
        result.put("orderId", bo.getId());
        return result;
    }*/


    //校验用户是否有已存在的借款订单
    private boolean checkUserBorrowOrder(String userId){
        List<BorrowOrder> boList = borrowOrderDao.findByUserId(Integer.valueOf(userId));
        List<Integer> paying = new ArrayList<>();
        paying.add(0);
        paying.add(22);
        paying.add(25);
        for (BorrowOrder bo : boList) {
            if (paying.contains(bo.getStatus())) {
                logger.info("saveLoan userId=" + userId + "已存在借款订单");
                return true;
            }
        }

        String key = "LOAN_"+ userId;
        String flag = jedisCluster.get(key);
        logger.info("saveLoan userId=" + userId + " flag=" + flag);
        if ("true".equals(flag)) {
            logger.info("saveLoan userId=" + userId + " 重复提交");
            return true;
        }
        jedisCluster.setex(key, 60 * 60, "true");
        return false;
    }


    private UserCardInfo getUserCardInfo(String bankId,String userId){
        //如果传了bankId则使用默认卡
        if (StringUtils.isEmpty(bankId)) {
            return userBankDao.findUserBankCard(Integer.valueOf(userId));
        } else {
            //否则使用其他银行卡
            return userBankDao.findBankCardByCardId(Integer.valueOf(bankId));
        }
    }

    @Override
    public Map<String, Object> saveLoanV2(Map<String, String> params, User user) {
        Map<String, Object> result = new HashMap<String, Object>();

        //校验用户是否存在借款订单
        if(checkUserBorrowOrder(user.getId())){
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "您当前有处理中的借款, 请不要重复提交");
            return result;
        }
        logger.info("saveLoanV2 userId=" + user.getId() + " params=" + JSON.toJSONString(params));

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("userId", user.getId()); //
        param.put("type", "2"); // 借记卡
        String bankId = params.get("bankId");

        //获取用户银行卡信息
        UserCardInfo cardInfo = getUserCardInfo(bankId,user.getId());
        if (cardInfo == null) {
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "请先绑定银行卡");
            return result;
        }

        // 查询银行信息
        Map<String, String> bankInfo = userBankDao.selectByPrimaryKey(cardInfo.getBank_id());
        Integer bank_iscmb = 2; // 是否是招商银行 ，默认：1：是，2：否
        if (bankInfo != null && "0308".equals(bankInfo.get("bankCode"))) {
            bank_iscmb = 1;
        }

        //设备类型1、Android 2、iOS 3、wap
        String clientType = params.get("clientType");
        if (StringUtils.isBlank(clientType)) {
            clientType = "1";
        } else {
            if ("android".equals(clientType)) {
                clientType = "1";
            } else if ("ios".equals(clientType)) {
                clientType = "2";
            } else if ("wap".equals(clientType)) {
                clientType = "3";
            } else {
                clientType = "1";
            }
        }
        logger.info("saveLoanV2 clientType = " + clientType);

        //合同ID
        String contractId = StringUtils.killnull(params.get("contract_id"));

        //【1】初始化借款订单
        // 计算服务费率
        Integer money = Integer.parseInt(params.get("money")) * 100; // 借款金额,单位：分
        //查询当前用户的借款和期限对应的费率配置
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("borrowAmount",String.valueOf(money));
        query.put("borrowDay",params.get("period"));
        //
        BorrowProductConfig config;
        if (params.get("productId") == null) {//灵活配置
            config = borrowProductConfigDao.selectByBorrowMoneyAndPeriod(query);
        } else {
            config =  borrowProductConfigDao.selectByPrimaryKey(Integer.parseInt(params.get("productId")));
        }


        if (config == null){
            logger.error(MessageFormat.format("产品线配置不存在，借款金额{0},期限{1}",money,params.get("period")));
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "产品线配置不存在");
            return result;
        }
        if (config.getTotalFeeRate() == null || config.getBorrowAmount() == null){
            logger.error(MessageFormat.format("产品线ID：{0}配置异常",config.getId()));
            result.put("code", ResponseStatus.FAILD.getName());
            result.put("msg", "产品线配置异常，请联系管理员");
            return result;
        }


        Integer loanInterest = config.getTotalFeeRate().intValue();
        //借款利率,万分之一
        Integer borrowRate = config.getTotalFeeRate().divide(config.getBorrowAmount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("10000")).intValue();
        Integer intoMoney = money - config.getTotalFeeRate().intValue();//借款金额 - 服务费金额

        Date date = new Date();
        BorrowOrder bo = new BorrowOrder();
        bo.setUserId(Integer.parseInt(user.getId()));//用户ID
        bo.setMoneyAmount(money);//借款金额
        bo.setApr(borrowRate);//借款利率
        bo.setLoanInterests(loanInterest);//借款利息

        bo.setProductId(config.getId());//产品ID
        bo.setIntoMoney(intoMoney);//实际打款金额

        bo.setLoanTerm(Integer.parseInt(params.get("period")));//借款期限
        bo.setCreatedAt(date);//订单创建时间
        bo.setUpdatedAt(date);
        bo.setOrderTime(date);//下单时间
        //asset_borrow_order新增续期费、续期手续费字段
        bo.setRenewalFee(config.getRenewalFee());
        bo.setRenewalPoundage(config.getRenewalPoundage());

        //滞纳金利率，单位为万分之几
        Integer lateApr = config.getLateFee().divide(config.getBorrowAmount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("10000")).intValue();
        bo.setLateFeeApr(lateApr);//滞纳金利率，单位为万分之几

        bo.setReceiveCardId(cardInfo.getBank_id());//打款银行卡ID
        bo.setUserPhone(user.getUserPhone());//放款用户手机
        bo.setRealname(user.getRealname());//放款用户姓名
        bo.setBankNumber(bankInfo.get("bankNumber"));//银行卡代号
        bo.setCardNo(cardInfo.getCard_no());//银行卡号
        bo.setBankIscmb(bank_iscmb);//是否是招行卡
        bo.setYurref(GenerateNo.payRecordNo("A"));//订单
        bo.setCustomerType(Integer.parseInt(user.getCustomerType()));//是否是老用户：0、新用户；1；老用户
        bo.setIdNumber(user.getIdNumber());//放款用户身份证号
        bo.setSerialNo(GenerateNo.generateShortUuid(10));//订单编号
        bo.setClientType(clientType);
        bo.setCfcaContractId(contractId);
        borrowOrderDao.insertSelective(bo);

        //【2】初始化订单状态变化日志
        //订单修改日志记录
        OrderLogModel logModel = new OrderLogModel();

        logModel.setUserId(user.getId());
        logModel.setBorrowId(String.valueOf(bo.getId()));
        logModel.setOperateType(OperateType.BORROW.getCode());
        logModel.setAction(OrderChangeAction.BORROW_ACTION.getCode());
        logModel.setAfterStatus("0");
        orderLogComponent.addNewOrderLog(logModel);

        //设备指纹
        BorrowOrderDevice borrowOrderDevice = new BorrowOrderDevice();
        borrowOrderDevice.setAssetBorrowOrderId(bo.getId());
        borrowOrderDevice.setUserId(Integer.parseInt(user.getId()));
        borrowOrderDevice.setCreateAt(date);
        borrowOrderDevice.setUpdateAt(date);
        borrowOrderDao.insertBorrowOrderDevice(borrowOrderDevice);

        //同盾反欺诈设备指纹
        String tdDeviceString = params.get("td_device");
        logger.info("saveLoanV2 同盾反欺诈设备指纹 tdDeviceString = " + tdDeviceString);
        BorrowOrderTdDevice borrowOrderTdDevice = new BorrowOrderTdDevice();
        if (StringUtils.isNotBlank(tdDeviceString)) {
            borrowOrderTdDevice.setDeviceContent(tdDeviceString);
        }
        borrowOrderTdDevice.setAssetBorrowOrderId(bo.getId());
        borrowOrderTdDevice.setUserId(Integer.parseInt(user.getId()));
        borrowOrderTdDevice.setCreateAt(date);
        borrowOrderTdDevice.setUpdateAt(date);
        logger.info("saveLoanV2 borrowOrderTdDevice = " + JSON.toJSONString(borrowOrderTdDevice));
        borrowOrderDao.insertBorrowOrderTdDevice(borrowOrderTdDevice);

        //剩余可借额度，调整为取用户最多可借额度 - 用户借款额度
        Integer amountAvailable = Integer.parseInt(user.getAmountMax());
        User upd = new User();
        upd.setAmountAvailable(String.valueOf(amountAvailable - money));//更新可用借款额度
        upd.setId(user.getId());
        userDao.updateAmountAvailableByUserId(upd);

        result.put("code", ResponseStatus.SUCCESS.getName());
        result.put("msg", ResponseStatus.SUCCESS.getValue());
        result.put("orderId", bo.getId());
        return result;
    }


//    /**
//     * 计算服务费 money 用户借款额度 单位：分
//     *
//     * @param period
//     * @param money
//     * @param apr
//     * @return
//     */
//    @Override
//    public Map<String, Integer> calculateApr(Integer money, Integer period,BigDecimal apr) {
//
//        Map<String, Integer> result = new HashMap<>();
//        if (apr == null || apr.compareTo(new BigDecimal("0")) == 0){
//            if (7 == period) {
//                apr = new BigDecimal(String.valueOf(Constant.RATE_MIN));
//            } else if (14 == period) {
//                apr = new BigDecimal(String.valueOf(Constant.RATE_MAX));
//            }
//        }
//        //服务费
//        Integer loanAprRate = apr.multiply(new BigDecimal(String.valueOf(money))).intValue();
//        result.put("loanApr", loanApr);
//        result.put("apr", apr.divide().multiply(new BigDecimal("1000")).intValue()); // 费率由百分比转换成万分比
//        return result;
//    }

    @Override
    public void authBorrowState(BorrowOrder borrowOrder) {

        BorrowOrder borrowOrderR = borrowOrderDao.selectByPrimaryKey(borrowOrder.getId());

        // ======================================以下状态需要第三方回调通知放款结果调用，见下方方法creditReCallBack=========================================
        if (borrowOrder.getStatus().intValue() == BorrowOrder.STATUS_HKZ.intValue()) {// 放款成功
            Date fkDate = new Date();
            borrowOrder.setLoanTime(fkDate);
            borrowOrder.setLoanEndTime(DateUtil.addDay(fkDate, borrowOrderR.getLoanTerm()));// 放款时间加上借款期限
            Repayment repayment = new Repayment();
            repayment.setUserId(borrowOrderR.getUserId());
            repayment.setAssetOrderId(borrowOrderR.getId());
            repayment.setRepaymentAmount(Long.valueOf(borrowOrderR.getMoneyAmount()));
            repayment.setLateFeeApr(borrowOrderR.getLateFeeApr());
            repayment.setRepaymentedAmount(0L);
            repayment.setRepaymentPrincipal(Long.valueOf(borrowOrderR.getIntoMoney()));
            repayment.setRepaymentInterest(Long.valueOf(borrowOrderR.getLoanInterests()));

            repayment.setRepaymentTime(DateUtil.addDay(fkDate, borrowOrderR.getLoanTerm()));// 放款时间加上借款期限
            repayment.setCreatedAt(fkDate);
            repayment.setUpdatedAt(fkDate);
            repayment.setStatus(borrowOrder.getStatus());
            repaymentDao.insert(repayment);

        }

        // ===============================================================================

        // borrowOrderService.updateById(borrowOrder);
        borrowOrderDao.updateByPrimaryKeySelective(borrowOrder);
        // 放款审核通过，需要完善判断，防止前端修改状态
        if (borrowOrder.getStatus().intValue() == BorrowOrder.STATUS_FKZ) {

            logger.info("调用第三方放款接口");
            // 调用第三方去放款

        }

    }

    @Override
    public void addUserLimit(User user) {
        // user=new User();
        // user.setId("28");
        logger.info("begin add userLimit userId:" + user.getId());
        Integer tgApr = 0;// 本次提额百分比，7天（sevenAmountaddFee%）、14天(fourteenAmountaddFee%)
        Integer tgJs = 0;// 提额计算提额金额的基数
        Integer tgMoneyType = 0;// 提额等级
        Integer addAmount = 0;// 本次提额金额( (tgJs * tgApr / 100))
        Date lastTeDate = null;// 上次提额时间
        Integer sucCount = 0;
        Integer sucAmount = 0;
        Integer normCount = 0;//累计还款次数
        Integer normAmount = 0;// 累计还款金额
        try {
            if (user != null) {

                Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
                // 7天借款额度增加比例
                Integer sevenAmountaddFee = Integer.parseInt(keys.get("seven_amountadd_fee"));
                // 14天借款额度增加比例
                Integer fourteenAmountaddFee = Integer.parseInt(keys.get("fourteen_amountadd_fee"));
                // 系统最大额度
                Integer sysAmountMax = Integer.parseInt(keys.get("max_amount_sys"));
                HashMap<String, Object> params = new HashMap<String, Object>();

                params.put("userId", user.getId());
                params.put("statusList", Arrays.asList(BorrowOrder.STATUS_YHK));
                //获取用户正常已还款的总借款记录
                List<BorrowOrder> normOrders = borrowOrderDao.findParams(params);
                if (normOrders != null && normOrders.size() > 0) {
                    // tgApr = normOrders.get(0).getLoanTerm() == 7 ? 3 : 5;
                    //最新一期成功借款的借款期限，并获取借款额度增加比例
                    tgApr = normOrders.get(0).getLoanTerm() == 7 ? sevenAmountaddFee : fourteenAmountaddFee;// 2017-02-15
                    // zjb
                    for (BorrowOrder bor : normOrders) {
                        normAmount += bor.getMoneyAmount();//成功借款额度累加
                    }

                    normCount = normOrders.size();//成功借款次数
                }
                if (!(normAmount < 160000)) {//总的成功借款额度 > 100000
                    tgMoneyType = normAmount / 160000;// 获取本次提额等级

                    params.clear();
                    params.put("userId", user.getId());
                    params.put("status", UserLimitRecord.STATUS_PASS_SUCC);
                    //获取用户历史成功提额等级记录
                    List<UserLimitRecord> userlimits = userLimitRecordDao.findListBayParams(params);
                    // 已经有过的提额等级
                    ArrayList<Integer> hasTe = new ArrayList<Integer>();
                    if (userlimits != null && userlimits.size() > 0) {
                        lastTeDate = userlimits.get(0).getUpdatedAt();//最新提额审核时间
                        for (UserLimitRecord bor : userlimits) {
                            hasTe.add(bor.getAddReasonType());//提额原因类型
                        }
                    }
                    // 历史提额等级中不包含本次提额等级 且 本次提额百分比 > 0 且 本次提额等级 > 0 然后进行提额操作
                    if (!hasTe.contains(tgMoneyType) && tgApr > 0 && tgMoneyType > 0) {
                        sucAmount = normAmount;//累计成功借款金额
                        sucCount = normCount;//累计成功借款次数

                        params.clear();
                        params.put("userId", user.getId());
                        params.put("statusList", Arrays.asList(BorrowOrder.STATUS_YQYHK));
                        //获取用户历史逾期已还款记录
                        List<BorrowOrder> yqOrders = borrowOrderDao.findParams(params);
                        if (yqOrders != null && yqOrders.size() > 0) {
                            for (BorrowOrder bor : yqOrders) {
                                sucAmount += bor.getMoneyAmount();//累加借款金额
                            }
                            sucCount += yqOrders.size();//累加借款次数
                        }
                        tgJs = tgMoneyType * 160000;//计算提额金额的基数
                        addAmount = (tgJs * tgApr / 100);//计算本次提升的借款额度
                        Integer oldAmountMax = Integer.parseInt(user.getAmountMax());//获取用户当前最大借款额度
                        Integer newAmountMax = oldAmountMax + addAmount;//更新用户最大借款额度

                        if (newAmountMax > sysAmountMax) {//判读更新后的最大借款额度 > 系统最大借款额度
                            logger.info("addUserLimit  param old:" + newAmountMax + ",more then sysAmountMax:" + sysAmountMax
                                    + ",change to sysAmountMax");
                            newAmountMax = sysAmountMax;//用户最大借款额度 = 系统最大借款额度
                        }
                        if (newAmountMax > oldAmountMax) {//判断本次用户最大借款额度 > 上次最大借款额度
                            Date nowDate = new Date();
                            //新增用户借款额度提额记录
                            UserLimitRecord record = new UserLimitRecord();
                            record.setUserId(Integer.parseInt(user.getId()));
                            record.setCreateAt(nowDate);
                            record.setUpdatedAt(nowDate);
                            record.setAddAmount(addAmount);
                            record.setAddReasonType(tgMoneyType);
                            record.setLastApplyAt(lastTeDate);
                            record.setStatus(UserLimitRecord.STATUS_PASS_SUCC);
                            record.setOldAmountMax(oldAmountMax);
                            record.setAuditUser("系统");
                            record.setNewAmountMax(newAmountMax);
                            record.setRemark("累计正常还款" + normAmount / 100 + "元,系统自动提额" + addAmount / 100 + "元");

                            record.setRepaymentSuccCount(sucCount);
                            record.setRepaymentSuccAmount(sucAmount);
                            record.setRepaymentNormCount(normCount);
                            record.setRepaymentNormAmount(normAmount);
                            //record.setRealname(user.getRealname());
                            //record.setUserPhone(user.getUserPhone());

                            userLimitRecordDao.insertSelective(record);

                            //更新用户信息
                            User newUser = new User();
                            newUser.setId(user.getId());
                            newUser.setAmountMax(String.valueOf(record.getNewAmountMax()));//用户最大借款额度
                            newUser.setAmountAvailable(String.valueOf(Integer.valueOf(user.getAmountAvailable()) + addAmount));//用户剩余可借款额度
                            newUser.setAmountAddsum(String.valueOf(Integer.valueOf(user.getAmountAddsum()) + addAmount));//用户累计增加额度
                            newUser.setUpdateTime(nowDate);//更新时间
//							userDao.updateByPrimaryKeyUser(newUser);
                            userDao.updateAmountByUserId(newUser);

                            logger.info("addUserLimit sucess 提额成功！");
                            // 更新个人信息缓存
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("userId", user.getId());
                            infoIndexService.changeUserAmount(map);

                            final String userPhone = user.getUserPhone();
                            final Integer normAmountT = normAmount / 100;
                            final Integer addAmountT = addAmount / 100;
                            // 发送提额短信
                            ThreadPool.getInstance().run(() -> SendSmsUtil.sendSmsDiyCL(userPhone, SendSmsUtil.templateld44634, normAmountT + "##" + addAmountT));

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("addUserLimit error 计算提额错误！", e);
        }

    }

    @Override
    public void changeUserLimit(HashMap<String, Object> mapParam) {
        Integer newAmountMax = Integer.parseInt(String.valueOf(mapParam.get("newAmountMax")));
        // 系统最大额度
        Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
        Integer sysAmountMax = Integer.parseInt(keys.get("max_amount_sys"));
        if (newAmountMax > sysAmountMax) {
            logger.info("changeUserLimit  param old:" + newAmountMax + ",more then sysAmountMax:" + sysAmountMax + ",change to sysAmountMax");
            newAmountMax = sysAmountMax;
        }

        logger.info("begin change userLimit userId:" + mapParam.get("userId"));
        UserLimitRecord record = new UserLimitRecord();
        record.setUserId(Integer.parseInt(String.valueOf(mapParam.get("userId"))));
        record.setNewAmountMax(newAmountMax);
        logger.info(mapParam.get("userId") + "  risk send amount " + newAmountMax);
        User user = userDao.searchByUserid(record.getUserId());
        logger.info(mapParam.get("userId") + " read db amount " + user.getAmountMax());
        // 提额等级0为风控提额
        Integer tgMoneyType = 0;
        // 本次提额金额( (tgJs * tgApr / 100))
        int addAmount = 0;

        try {
            if (user != null) {

                addAmount = record.getNewAmountMax() - Integer.parseInt(user.getAmountMax());
                logger.info("changeUserLimit addAmount = " + addAmount);
                if (addAmount != 0) {
                    // 上次提额时间
                    Date lastTeDate = null;
                    HashMap<String, Object> params = new HashMap<>();
                    params.clear();
                    params.put("userId", user.getId());
                    params.put("status", UserLimitRecord.STATUS_PASS_SUCC);
                    List<UserLimitRecord> userlimits = userLimitRecordDao.findListBayParams(params);
                    // 已经有过的提额等级
//                    ArrayList<Integer> hasTe = new ArrayList<>();
                    if (userlimits != null && userlimits.size() > 0) {
                        lastTeDate = userlimits.get(0).getUpdatedAt();

                    }

                    Date nowDate = new Date();
                    record = new UserLimitRecord();
                    record.setUserId(Integer.parseInt(user.getId()));
                    record.setCreateAt(nowDate);
                    record.setUpdatedAt(nowDate);
                    record.setAddAmount(addAmount);
                    record.setAddReasonType(tgMoneyType);
                    record.setLastApplyAt(lastTeDate);
                    record.setStatus(UserLimitRecord.STATUS_PASS_SUCC);
                    record.setOldAmountMax(Integer.parseInt(user.getAmountMax()));
                    record.setAuditUser("风控");
                    record.setNewAmountMax(Integer.parseInt(user.getAmountMax()) + addAmount);
                    record.setRemark("风控运行,系统自动提额" + addAmount / 100 + "元");
                    record.setRealname(user.getRealname());
                    record.setUserPhone(user.getUserPhone());

                    userLimitRecordDao.insertSelective(record);

                    User newUser = new User();
                    newUser.setId(user.getId());
                    newUser.setAmountMax(String.valueOf(record.getNewAmountMax()));
                    newUser.setAmountAvailable(String.valueOf(Integer.valueOf(user.getAmountAvailable()) + addAmount));
                    newUser.setAmountAddsum(String.valueOf(Integer.valueOf(user.getAmountAddsum()) + addAmount));
                    newUser.setUpdateTime(nowDate);
//					userDao.updateByPrimaryKeyUser(newUser);
                    userDao.updateAmountByUserId(newUser);
                    logger.info("changeUserLimit sucess 额度修改成功！");
                    // 更新个人信息缓存
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());
                    infoIndexService.changeUserAmount(map);
//                    final String userPhone = user.getUserPhone();
                    final Integer amountMax = Integer.parseInt(newUser.getAmountMax()) / 100;
                    /*if (amountMax > 0) {
                        ThreadPool.getInstance().run(() -> {
                            // 发送提额短信
//								SendSmsUtil.sendSmsDiyCL(userPhone, "您的可用数额已经提升至" + amountMax + "元，请保持良好的还款习惯！");
                        });
                    }*/
                }

            } else {

                logger.error("begin change userLimit user not  exsit:" + mapParam.get("userId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeUserLimit error 额度修改错误！");
        }

    }

    @Override
    public PageConfig<UserLimitRecord> finduserLimitPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "UserLimitRecord");
        return paginationDao.findPage("findListBayParams", "findParamsCount", params, "web");
    }

    @Override
    public BorrowOrder selectBorrowOrderUseId(Integer userId) {
        return borrowOrderDao.selectBorrowOrderUseId(userId);
    }

    /**
     * 检查当前用户是否存在未还款完成的订单
     *
     * @param userId
     * @return 1：是；0：否
     */
    @Override
    public Integer checkBorrow(Integer userId) {
        Integer result = 0;
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("statusList", Arrays.asList(BorrowOrder.STATUS_DCS,
                // BorrowOrder.STATUS_JSJJ,
                BorrowOrder.STATUS_CSTG, BorrowOrder.STATUS_FSTG, BorrowOrder.STATUS_HKZ, BorrowOrder.STATUS_FKZ,
                BorrowOrder.STATUS_BFHK, BorrowOrder.STATUS_YYQ, BorrowOrder.STATUS_YHZ));
        List<BorrowOrder> normOrders = borrowOrderDao.findParams(params);
        if (normOrders != null && normOrders.size() > 0) {
            result = 1;
        }
        return result;
    }

//    @Override
//    public List<BorrowOrder> findByUserId(Integer userId) {
//        return borrowOrderDao.findByUserId(userId);
//    }

    @Override
    public Map<String, String> findAuditFailureOrderByUserId(String userId) {
        Map<String, String> result = new HashMap<String, String>();
        Integer code = 0;
        String msg = "";
        int nextLoanDay = 0;// 剩余可借款天数
        Integer interval_day = 0; // 申请失败距当前时间的间隔天数

        Map<String, String> intervalMap = SysCacheUtils.getConfigMap(BackConfigParams.INTERVAL);
        int interval = Integer.valueOf(intervalMap.get("INTERVAL_BORROW_AGAIN"));
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("statusList", Arrays.asList(BorrowOrder.STATUS_CSBH, BorrowOrder.STATUS_FSBH, BorrowOrder.STATUS_FKBH));
        BorrowOrder bo = borrowOrderDao.findAuditFailureOrderByUserId(params);
        if (bo != null) {
            Date date = new Date();
            Date smDate = new Date();
            if (bo.getVerifyLoanTime() != null) {
                smDate = bo.getVerifyLoanTime();
            } else if (bo.getVerifyReviewTime() != null) {
                smDate = bo.getVerifyReviewTime();
            } else if (bo.getVerifyTrialTime() != null) {
                smDate = bo.getVerifyTrialTime();
            }
            interval_day = DateUtil.daysBetween(smDate, date);
            code = interval_day < interval ? -1 : 0;

            if (code == -1) {
                msg = "距离上次审核失败不足" + interval + "天，请" + (interval - interval_day) + "天后重新申请。";
                nextLoanDay = (interval - interval_day);
                result.put("canLoan", new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.addDay(smDate, interval - 1)));
            }
        }
        result.put("code", code + "");
        result.put("msg", msg);
        result.put("nextLoanDay", String.valueOf(nextLoanDay));
        logger.info("主页面显示标识符nextLoanDay为{}",nextLoanDay);
        return result;
    }

    @Override
    public BorrowOrder selectBorrowOrderNowUseId(Integer userId) {
        return borrowOrderDao.selectBorrowOrderNowUseId(userId);
    }

    /**
     * 银行卡原因导致的放款失败
     *
     * @param desc
     * @return
     */
    public void sendFangkuanFailNotice(final User user, String desc) {
        boolean flag = false;
        //拦截放款失败原因
        for (Integer in : BorrowOrder.borrowFailCardRemarkMap.keySet()) {
            if (desc.contains(BorrowOrder.borrowFailCardRemarkMap.get(in))) {
                flag = true;
                break;
            }
        }
        //拦截成功
        if (flag) {
            logger.info("sendFangkuanFailNotice success userId=" + user.getId());
            final String msg = user.getRealname();

            ThreadPool pool = ThreadPool.getInstance();
            pool.run(() -> SendSmsUtil.sendSmsDiyCL(user.getUserPhone(), SendSmsUtil.templateld45236, msg));
        }
    }

    @Override
    public void updateLoanNew(BorrowOrder borrowOrder, String code, String desc) {
        Date nowDate = new Date();

        BorrowOrder borrowOrderNew = new BorrowOrder();
        borrowOrderNew.setId(borrowOrder.getId());
        borrowOrderNew.setUpdatedAt(nowDate);
        // 真正的放款时间
        Date zhfkDate;
        final User user = userService.searchByUserid(borrowOrder.getUserId());

        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(user.getId());
        logModel.setBorrowId(String.valueOf(borrowOrder.getId()));
        logModel.setOperateType(OperateType.BORROW.getCode());
        logModel.setBeforeStatus(String.valueOf(BorrowOrder.STATUS_FKZ));
        //借款订单
        if (borrowOrder.getYurref().startsWith("A")) {
            //放款成功
            if ("SUCCESS".equals(code)) {
                zhfkDate = new Date();
                borrowOrderNew.setOutTradeNo(borrowOrder.getOutTradeNo());
                borrowOrderNew.setPaystatus(BorrowOrder.SUB_PAY_SUCC);
                borrowOrderNew.setLoanTime(zhfkDate);
                //已放款
                borrowOrderNew.setStatus(BorrowOrder.STATUS_HKZ);
                borrowOrderNew.setPayRemark("支付成功");
//				borrowOrderNew.setLoanEndTime(DateUtil.addDay(zhfkDate, borrowOrder.getLoanTerm()));// 放款时间加上借款期限，T+7或T+14
                // 放款时间加上借款期限，T+6
                //改为 6天后23：59：59
                borrowOrderNew.setLoanEndTime(DateUtil.getLoanEndTime(borrowOrder.getLoanTerm()-1));
                // 放款成功插入还款记录
                borrowOrder.setPaystatus("0000");
                borrowOrder.setStatus(BorrowOrder.STATUS_HKZ);
                borrowOrder.setLoanTime(zhfkDate);
                // 放款时间加上借款期限
                borrowOrder.setLoanEndTime(borrowOrderNew.getLoanEndTime());

                repaymentService.insertByBorrorOrder(borrowOrder);
                //【2】放款日志记录
                logModel.setAfterStatus(String.valueOf(BorrowOrder.STATUS_HKZ));
                logModel.setRemark("已放款");
                orderLogComponent.addNewOrderLog(logModel);


                String cardNo = borrowOrder.getCardNo();
                final String msg = user.getRealname() + "##" + PropertiesConfigUtil.get("APP_NAME") + "##" + (borrowOrder.getMoneyAmount() / 100) + "##" +
                        cardNo.substring(cardNo.length() - 4);
                String userClientId = userClientInfoService.queryClientIdByUserId(borrowOrder.getUserId());

                String mqMsg = "您好，您的" + (borrowOrder.getMoneyAmount() / 100) + ".00元借款已经放款成功，祝您用款愉快！";
                logger.info("prepared send mqMsg:" + mqMsg + " userClientId:" + userClientId);
                // 通过mq推送放款成功消息到oss系统
//                producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(4, userClientId, "到账成功", mqMsg, mqMsg)));
                ThreadPool pool = ThreadPool.getInstance();
                pool.run(() -> SendSmsUtil.sendSmsDiyCL(user.getUserPhone(), SendSmsUtil.templateld44641, msg));

            } else {
                borrowOrderNew.setPayRemark("支付失败:" + desc);
                borrowOrderNew.setPaystatus(BorrowOrder.PAY_PAY_FAIL);
                borrowOrderNew.setStatus(BorrowOrder.STATUS_FKSB);

                //【2】放款失败日志记录
                logModel.setAfterStatus(String.valueOf(BorrowOrder.STATUS_FKSB));
                logModel.setRemark("支付失败:" + desc);
                orderLogComponent.addNewOrderLog(logModel);

                String userClientId = userClientInfoService.queryClientIdByUserId(Integer.valueOf(user.getId()));
                String mqMsg = "您好，您的" + (borrowOrder.getMoneyAmount() / 100) + ".00元借款放款失败，请及时至APP内联系客服处理。";
                logger.info("prepared send mqMsg:" + mqMsg + " userClientId:" + userClientId);
                // 通过mq推送放款失败信息到oss
//                producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(new GeTuiJson(3, userClientId, "放款失败", mqMsg, mqMsg)));

                //短信通知用户切换默认卡
                sendFangkuanFailNotice(user, desc);
            }
            borrowOrderDao.updateByPrimaryKeySelective(borrowOrderNew);
        }
    }

    @Override
    public void updateBorrowStatus(String id, String status) {
        borrowOrderDao.updateBorrowStatus(id,status);
    }

    @Override
    public List<BorrowOrder> getWaitAiList() {
        return borrowOrderDao.getWaitAiList();
    }

    @Override
    public int getRepaidCount(int userId) {
        return 0;
    }

    @Override
    public boolean getExtendStatus(int userId) {
        Integer status = backExtendDao.selectStatusByExtendId(userId);
        return status != null && status == 0;
    }

    @Override
    public Map<String, Object> extend(Integer orderId) {

        BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(orderId);
        Integer productId = borrowOrder.getProductId();

        BorrowProductConfig borrowProductConfig = borrowProductConfigDao.selectByPrimaryKey(productId);

        Integer extendId = borrowProductConfig.getExtendId();

        BackExtend backExtend = backExtendDao.selectById(extendId);

        Integer extendCount = backExtend.getExtendCount();
        Integer extendMoney = backExtend.getExtendMoney();



        return null;
    }
}
