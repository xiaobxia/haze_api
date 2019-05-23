package com.vxianjin.gringotts.pay.service.impl;

import com.tools.mq.producer.CommonProducer;
import com.vxianjin.gringotts.pay.common.enums.EventTypeEnum;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.publish.PublishAdapter;
import com.vxianjin.gringotts.pay.common.publish.PublishFactory;
import com.vxianjin.gringotts.pay.dao.UserQuotaApplyLogMapper;
import com.vxianjin.gringotts.pay.dao.UserQuotaSnapshotDao;
import com.vxianjin.gringotts.pay.model.BackLimit;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.pay.model.UserQuotaApplyLog;
import com.vxianjin.gringotts.pay.model.UserQuotaSnapshot;
import com.vxianjin.gringotts.pay.service.BorrowProductConfigService;
import com.vxianjin.gringotts.pay.service.UserQuotaLogService;
import com.vxianjin.gringotts.pay.service.UserQuotaSnapshotService;
import com.vxianjin.gringotts.util.TimeKey;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IBackLimitDao;
import com.vxianjin.gringotts.web.dao.impl.UserDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.impl.UserClientInfoService;
import com.vxianjin.gringotts.web.util.SendSmsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 用户可借额度信息处理类
 *
 * @author jintian
 * @date 13:53
 */
@Service
public class UserQuotaSnapshotServiceImpl implements UserQuotaSnapshotService, InitializingBean {

    private Logger log = LoggerFactory.getLogger(UserQuotaSnapshotServiceImpl.class);

    @Resource
    private UserQuotaSnapshotDao userQuotaSnapshotMapper;

    @Autowired
    private IBorrowOrderService borrowOrderService;

//    @Autowired
//    private MapiConnectionService mapiConnectionService;
//    @Autowired
//    private ICreditLineService creditLineService;

    @Autowired
    private BorrowProductConfigService borrowProductConfigService;

    @Resource
    private IBackLimitDao backLimitDao;

    @Autowired
    private UserQuotaLogService userQuotaLogService;

    @Resource
    private UserQuotaApplyLogMapper userQuotaApplyLogMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonProducer producer;

    @Autowired
    private UserClientInfoService userClientInfoService;

    @Resource
    private ApplicationContext applicationContext;

    @Value("#{mqSettings['oss_topic']}")
    private String ossMqTopic;

    @Value("#{mqSettings['oss_target']}")
    private String ossMqTarget;

    @Value("#{mapiSettings['mapi.system']}")
    private String systemName;

    @Value("#{mqSettings['quota.one.lervel']}")
    private String levelOneUrl;

    @Value("#{mqSettings['quota.second.lervel']}")
    private String levelSecondUrl;

    @Value("#{mapiSettings['mapi.start.time']}")
    private String startTime;

    private static int THREAD_NUMBER = 5;

    private Executor userIncreaseLimitExecutor;

    @Override
    public List<UserQuotaSnapshot> getUserQuotaSnapshotByUser(User user) {
        log.info("userId is " + (user == null ? "null" : user.getId()));
        if (user == null || StringUtils.isBlank(user.getId())) {
            return new ArrayList<>();
        }
        return userQuotaSnapshotMapper.queryByUserId(user.getId());
    }

    @Override
    public Map<String, String> queryUserQuotaSnapshot(int userId, String oldAmount) {
        //return null;
//        Map map = new HashMap();
//        map.put("uid", userId);
//        map.put("vest", systemName);
//        map.put("date", startTime);
        Map<String, String> resultMap = new HashMap<>();
        try{
//            String respString = creditLineService.newEvaluate(DateUtil.dateFormat(startTime,"yyyy-MM-dd HH:mm:ss"), userId,systemName);
//            JSONObject jsonObject = JSON.parseObject(respString);
//            if(jsonObject!=null){
                if (StringUtils.isNotBlank(oldAmount)) {
                    switch (oldAmount) {
                        case "160000":
                            resultMap.put("7", "180000");
                            break;
                        case "180000":
                        case "200000":
                            resultMap.put("7", "200000");
                            break;
                    }
                } else {
                    resultMap.put("7", "160000");
                }
                //String amount = "160000";
//                log.info("userQuota respString :" + respString);

//            }
            return resultMap;
        }catch (Exception e){
            log.error("parse date error:{}",e);
            return null;
        }

    }

    /**
     *  第一步：根据还款额度查询出对应产品
     *  关联查询出提额配置表数据
     *
     *  第二步：根据提额机制，先判断该产品配置的提额次数限制有没有达标
     *  达标进入下一步
     *  不达标直接返回当前产品额度与期限
     *
     *  第三步：达标后获取提额配置里面的产品ID
     *  将该产品ID对应的产品额度与产品期限返回
     * */
    @Override
    public Map<String, String> newQueryUserQuotaSnapshot(int userId, String amount, int orderId) {
        Map<String, String> resultMap = new HashMap<>();
        try{

            BorrowOrder oneBorrow = borrowOrderService.findOneBorrow(orderId);
            Integer productId = oneBorrow.getProductId();

            //该用户的成功还款次数
            int count = borrowOrderService.getRepaidCount(oneBorrow.getUserId());

            BorrowProductConfig productConfig = borrowProductConfigService.queryProductById(productId);
            Integer limitId = productConfig.getLimitId();

            BackLimit backLimit = backLimitDao.selectById(limitId);

            Integer limitCount = backLimit.getLimitCount();//提额需要成功的次数
            Integer limitProductId = backLimit.getLimitProductId();//提额至哪个产品
            BorrowProductConfig newProductConfig = borrowProductConfigService.queryProductById(limitProductId);
            if (count >= limitCount) {//还款成功笔数大于提额配置限制则返回新额度
                resultMap.put(newProductConfig.getId().toString(), newProductConfig.getBorrowAmount().toString());//
            } else {
                resultMap.put(productConfig.getId().toString(), productConfig.getBorrowAmount().toString());//不达标返回老产品的参数
            }
            return resultMap;
        }catch (Exception e){
            log.error("newQueryUserQuotaSnapshot error:{}",e);
            return null;
        }
    }

    @Override
    public void newUpdateUserQuotaSnapshots(int userId, long applyId, String repaymentedAmount, int orderId) {
        userIncreaseLimitExecutor.execute(new NewUserIncreaseLimitThread(userId, applyId, repaymentedAmount, orderId));
    }

    @Override
    @Transactional(timeout = TransactionDefinition.PROPAGATION_NOT_SUPPORTED)
    public void updateUserQuotaSnapshots(int userId, long applyId, String repaymentedAmount) {
        userIncreaseLimitExecutor.execute(new UserIncreaseLimitThread(userId, applyId, repaymentedAmount));
    }

    @Override
    public boolean addOrUpdateUserQuotaSnapShot(int userId, int productId) {
        // TODO 表设计有问题，不需要这个productId,如果去除逻辑需要修改,也不需要获取产品线，表设计有毒
        // 获取现在额度产品线
        BorrowProductConfig nowProductConfig = borrowProductConfigService.queryProductById(productId);
        String nowLimit = nowProductConfig.getBorrowAmount().toString();
        int borrowDay = nowProductConfig.getBorrowDay();


        // 判断是否存在
        UserQuotaSnapshot userQuotaSnapshot = userQuotaSnapshotMapper.queryByUserIdBorrowDay(userId, borrowDay);
        // 以前有则跟新，没有则插入
        if (userQuotaSnapshot != null) {
            log.info("update user qupta ,nowProductConfig " + nowProductConfig.getId() + " nowLimit :" + nowLimit + " agoLimit:" + userQuotaSnapshot.getUserAmountLimit() + " lastUpdateTime:" + DateUtil.formatDate(userQuotaSnapshot.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            // 是否需要跟新最大
            if (userQuotaSnapshot.getBorrowProductId().equals(nowProductConfig.getId())) {
                // 只更新用户额度
                log.info("updateUserLimit userId:" + userId + " borrowDay:" + borrowDay + " nowLimit:" + nowLimit);
                updateUserLimit(userId, borrowDay, new BigDecimal(nowLimit));
            } else {
                log.info("updateUserQuota userId:" + userId + " nowProductId:" + nowProductConfig.getId() + " borrowDay:" + borrowDay + " nowLimit:" + nowLimit);
                // 更新额度和可借产品线
                updateUserQuota(userId, nowProductConfig.getId(), borrowDay, new BigDecimal(nowLimit));
            }
            userQuotaLogService.addUserQuotaLog(userId, userQuotaSnapshot.getUserAmountLimit(), new BigDecimal(nowLimit));
        } else {
            log.info("add user qupta ,nowProductConfig " + nowProductConfig.getId());
            userQuotaLogService.addUserQuotaLog(userId, borrowProductConfigService.queryByBorrowByStatus(0).getBorrowAmount(), new BigDecimal(nowLimit));
            addUserQuota(userId, nowProductConfig.getId(), new BigDecimal(nowLimit), borrowDay);
        }
        return true;
    }

    private void updateUserLimit(int userId, int borrowDay, BigDecimal nowLimit) {
        userQuotaSnapshotMapper.updateUserLimitAmount(userId, borrowDay, nowLimit);
    }

    @Override
    public boolean updateUserQuota(int userId, int productId, int borrowDay, BigDecimal nowLimit) {
        return userQuotaSnapshotMapper.updateUserQuota(userId, productId, borrowDay, nowLimit) > 0;
    }

    @Override
    public boolean addUserQuota(int userId, int productId, BigDecimal nowLimit, int borrowDay) {
        return userQuotaSnapshotMapper.addUserQuota(userId, productId, nowLimit, borrowDay) > 0;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        userIncreaseLimitExecutor = Executors.newFixedThreadPool(THREAD_NUMBER);
    }

    class UserIncreaseLimitThread implements Runnable {

        private int userId;

        private long applyId;

        private String repaymentedAmount;

        @Override
        public void run() {
            /*TimeKey.clear();
            TimeKey.start();
            log.info("start update user quotasnapshots, userId :" + userId + " applyId:" + applyId);
            Map<String, String> userLimits = null;
            try {
                if (applyId < 0) {
                    // 睡2秒，防止数据同步问题
                    Thread.sleep(2000);
                }
                userLimits = queryUserQuotaSnapshot(userId, repaymentedAmount);
                if (userLimits == null && userLimits.size() == 0) {
                    if (applyId > 0) {
                        userQuotaApplyLogMapper.updateToFail(applyId);
                    } else {
                        userQuotaApplyLogMapper.insertSelective(new UserQuotaApplyLog(userId));
                    }
                    return;
                }
                log.info("query user limits size:" + userLimits.size());
                log.info("start update user quotasnapshot");
                String nowLimit = "160000";
                for (String key : userLimits.keySet()) {
                    // 用户额度
                    String userLimit = userLimits.get(key);
                    // 更新到用户额度表中，并将用户额度信息变更记录存入变更记录表中
                    addOrUpdateUserQuotaSnapShot(userId, userLimit, Integer.valueOf(key));

                    if (Integer.parseInt(nowLimit) < Integer.parseInt(userLimit)) {
                        nowLimit = userLimit;
                    }
                }
                log.info("end update user quotasnapshot");
                // 获取用户最高额度
                BigDecimal bigDecimal = userQuotaSnapshotMapper.queryUserMaxLimit(userId);
                if (!nowLimit.equals(bigDecimal.toString())) {
                    bigDecimal = BigDecimal.valueOf(Long.parseLong(nowLimit));
                }
                // 修改用户额度(user_info)
                if (bigDecimal == null) {
                    log.info("userId :" + userId + " not found max limit ");
                    return;
                }
                log.info("userId :" + userId + " now max limit :" + bigDecimal.intValue());
                User user = userDao.searchByUserid(userId);
                log.info("user update amount ,beforeMaxAmount:" + user.getAmountMax() + " beforeAvailableAmount:" + user.getAmountAvailable() + " nowMaxAmout:" + bigDecimal + " nowAmountAvailable:" + bigDecimal.add(new BigDecimal(user.getAmountAvailable())).subtract(new BigDecimal(user.getAmountMax())));
                // 判断是否提额，如果提额没变化认为可能存在问题,如为变更额度记录
                if (Integer.valueOf(user.getAmountMax()).equals(bigDecimal.intValue()) && !Integer.valueOf(user.getAmountMax()).equals(Integer.valueOf(PropertiesConfigUtil.get("max_user_amount")))) {
                    if (applyId > 0) {
                        userQuotaApplyLogMapper.updateToNoUpdate(applyId);
                    } else {
                        userQuotaApplyLogMapper.insertSelective(new UserQuotaApplyLog(userId, "3"));
                    }
                } else if (applyId > 0) {
                    userQuotaApplyLogMapper.updateToSuccess(applyId);
                }
                userDao.updateUserLimit(userId, bigDecimal.intValue(), bigDecimal.add(new BigDecimal(user.getAmountAvailable())).subtract(new BigDecimal(user.getAmountMax())));
                BigDecimal afterMaxAmount = bigDecimal;
                //短信用户提额至1500，2000时的消息推送
                try {
                    BigDecimal beforeMaxAmount = new BigDecimal(user.getAmountMax());
                    if (beforeMaxAmount.compareTo(new BigDecimal(150000)) == -1) {
                        if (afterMaxAmount.compareTo(new BigDecimal(150000)) >= 0) {
                            // 推送 > 1500
                            this.sendSuccessMessage(afterMaxAmount, user);
                        }
                    } else {
                        if (beforeMaxAmount.compareTo(new BigDecimal(200000)) == -1) {
                            if (afterMaxAmount.compareTo(new BigDecimal(200000)) >= 0) {
                                // 推送2000
                                this.sendSuccessMessage(afterMaxAmount, user);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("用户额度提升信息发送失败", e);
                }
                log.info("end update user quotasnapshots");
            } catch (Exception e) {
                // 必须抓住该异常，不能在线程中抛出异常会造成线程杀死
                try {
                    if (applyId > 0) {
                        userQuotaApplyLogMapper.updateToFail(applyId);
                    } else {
                        userQuotaApplyLogMapper.insertSelective(new UserQuotaApplyLog(userId));
                    }
                } catch (Exception e1) {
                    log.error("save user quota apply log unsuccess fail");
                }
                log.error("system error", e);
                return;
            } finally {
                TimeKey.clear();
            }*/
        }

        // 提额通知发送
        private void sendSuccessMessage(BigDecimal afterMaxAmount, User user) {

            /*String userClientId = userClientInfoService.queryClientIdByUserId(Integer.valueOf(user.getId()));
            //短信
            String shortMessage = user.getRealname() + "##" + afterMaxAmount.divide(new BigDecimal(100));
            //内推消息
            String message = "您好，因您的借款信用记录极好，当前最大可用额度已提升至" + afterMaxAmount.divide(new BigDecimal(100)) + "元。";
            //短信消息发送
            SendSmsUtil.sendSmsDiyCL(user.getUserPhone(), SendSmsUtil.templateld46366, shortMessage);
            try {
                PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.REPAY.getCode());
                publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), shortMessage, user.getUserPhone());
            } catch (PayException e) {
                log.error(MessageFormat.format("用户{0},还款提示短信发送失败====>e :{1}", user.getUserPhone(), e.getMessage()));
            }
            GeTuiJson geTuiJson = new GeTuiJson(0, userClientId, "提额通知", message, message);

            if (afterMaxAmount.compareTo(new BigDecimal(200000)) >= 0) {
                geTuiJson.setUrl(levelSecondUrl);
            } else if (afterMaxAmount.compareTo(new BigDecimal(150000)) >= 0) {
                geTuiJson.setUrl(levelOneUrl);
            }
            producer.sendMessage(ossMqTopic, ossMqTarget, JSON.toJSONString(geTuiJson));

            log.info(MessageFormat.format("向用户:{0}发送提额成功个推消息,提额后金额:{1}", user.getUserName(), afterMaxAmount));*/
        }


        public UserIncreaseLimitThread(int userId, long applyId, String repaymentedAmount) {
            this.userId = userId;
            this.applyId = applyId;
            this.repaymentedAmount = repaymentedAmount;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getApplyId() {
            return applyId;
        }

        public void setApplyId(long applyId) {
            this.applyId = applyId;
        }
    }


    @Data @AllArgsConstructor class NewUserIncreaseLimitThread implements Runnable {

        private int userId;

        private long applyId;

        private String repaymentedAmount;

        private Integer orderId;

        @Override
        public void run() {
            TimeKey.clear();
            TimeKey.start();
            log.info("start update user quotasnapshots, userId :" + userId + " applyId:" + applyId);
            Map<String, String> userLimits;
            try {
                if (applyId < 0) {
                    // 睡2秒，防止数据同步问题
                    Thread.sleep(2000);
                }
                userLimits = newQueryUserQuotaSnapshot(userId, repaymentedAmount, orderId);
                if (userLimits == null && userLimits.size() == 0) {
                    if (applyId > 0) {
                        userQuotaApplyLogMapper.updateToFail(applyId);
                    } else {
                        userQuotaApplyLogMapper.insertSelective(new UserQuotaApplyLog(userId));
                    }
                    return;
                }
                log.info("query user limits size:" + userLimits.size());
                log.info("start update user quotasnapshot");
                String nowLimit = borrowProductConfigService.queryByBorrowByStatus(0).getBorrowAmount().toString();
                for (String key : userLimits.keySet()) {
                    // 用户额度
                    String userLimit = userLimits.get(key);
                    // 更新到用户额度表中，并将用户额度信息变更记录存入变更记录表中
                    addOrUpdateUserQuotaSnapShot(userId, Integer.valueOf(key));

                    if (Integer.parseInt(nowLimit) < Integer.parseInt(userLimit)) {
                        nowLimit = userLimit;
                    }
                }
                log.info("end update user quotasnapshot");
                // 获取用户最高额度
                BigDecimal bigDecimal = userQuotaSnapshotMapper.queryUserMaxLimit(userId);
                if (!nowLimit.equals(bigDecimal.toString())) {
                    bigDecimal = BigDecimal.valueOf(Long.parseLong(nowLimit));
                }
                // 修改用户额度(user_info)
                if (bigDecimal == null) {
                    log.info("userId :" + userId + " not found max limit ");
                    return;
                }
                log.info("userId :" + userId + " now max limit :" + bigDecimal.intValue());
                User user = userDao.searchByUserid(userId);
                log.info("user update amount ,beforeMaxAmount:" + user.getAmountMax() + " beforeAvailableAmount:" + user.getAmountAvailable() + " nowMaxAmout:" + bigDecimal + " nowAmountAvailable:" + bigDecimal.add(new BigDecimal(user.getAmountAvailable())).subtract(new BigDecimal(user.getAmountMax())));
                // 判断是否提额，如果提额没变化认为可能存在问题,如为变更额度记录
                if (Integer.valueOf(user.getAmountMax()).equals(bigDecimal.intValue()) && !Integer.valueOf(user.getAmountMax()).equals(Integer.valueOf(PropertiesConfigUtil.get("max_user_amount")))) {
                    if (applyId > 0) {
                        userQuotaApplyLogMapper.updateToNoUpdate(applyId);
                    } else {
                        userQuotaApplyLogMapper.insertSelective(new UserQuotaApplyLog(userId, "3"));
                    }
                } else if (applyId > 0) {
                    userQuotaApplyLogMapper.updateToSuccess(applyId);
                }
                userDao.updateUserLimit(userId, bigDecimal.intValue(), bigDecimal.add(new BigDecimal(user.getAmountAvailable())).subtract(new BigDecimal(user.getAmountMax())));
                try {
                    this.sendSuccessMessage(bigDecimal, user);
                } catch (Exception e) {
                    log.error("用户额度提升信息发送失败", e);
                }
                log.info("end update user quotasnapshots");
            } catch (Exception e) {
                // 必须抓住该异常，不能在线程中抛出异常会造成线程杀死
                try {
                    if (applyId > 0) {
                        userQuotaApplyLogMapper.updateToFail(applyId);
                    } else {
                        userQuotaApplyLogMapper.insertSelective(new UserQuotaApplyLog(userId));
                    }
                } catch (Exception e1) {
                    log.error("save user quota apply log unsuccess fail");
                }
                log.error("system error", e);
                return;
            } finally {
                TimeKey.clear();
            }
        }

        // 提额通知发送
        private void sendSuccessMessage(BigDecimal afterMaxAmount, User user) {

            //短信
            String shortMessage = user.getRealname() + "##" + afterMaxAmount.divide(new BigDecimal(100));
            //短信消息发送
            SendSmsUtil.sendSmsDiyCL(user.getUserPhone(), SendSmsUtil.templateld46366, shortMessage);
            try {
                PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.REPAY.getCode());
                publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), shortMessage, user.getUserPhone());
            } catch (PayException e) {
                log.error(MessageFormat.format("用户{0},还款提示短信发送失败====>e :{1}", user.getUserPhone(), e.getMessage()));
            }

            log.info(MessageFormat.format("向用户:{0}发送提额成功个推消息,提额后金额:{1}", user.getUserName(), afterMaxAmount));
        }

    }
}
