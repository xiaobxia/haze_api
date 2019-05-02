package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.pay.dao.UserCreditquotaLogDao;
import com.vxianjin.gringotts.pay.model.UserCreditquotaLog;
import com.vxianjin.gringotts.pay.service.UserQuotaLogService;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * @Author: chenkai
 * @Date: 2018/9/11 16:56
 * @Description:
 */
@Service
public class UserQuotaLogServiceImpl implements UserQuotaLogService {

    private final static Logger logger = LoggerFactory.getLogger(UserQuotaLogServiceImpl.class);


    @Autowired
    private UserCreditquotaLogDao userCreditquotaLogDao;

    @Override
    public void addUserQuotaLog(int userId, BigDecimal beforeQuota, BigDecimal afterQuota) {

        if (beforeQuota == null || (beforeQuota.compareTo(new BigDecimal("0")) < 0)) {
            logger.error(MessageFormat.format("用户{0} 变化后额度值有误", userId));
            return;
        }

        if (afterQuota == null || (afterQuota.compareTo(new BigDecimal("0")) < 0)) {
            logger.error(MessageFormat.format("用户{0} 变化后额度值有误", userId));
            return;
        }
        UserCreditquotaLog userCreditquotaLog = new UserCreditquotaLog();
        userCreditquotaLog.setUserId(userId);
        if (beforeQuota.compareTo(afterQuota) > 0) {
            userCreditquotaLog.setQuotaType("逾期下降额度");
        } else if (beforeQuota.compareTo(afterQuota) == 0) {
            userCreditquotaLog.setQuotaType("额度未提升");
        } else {
            userCreditquotaLog.setQuotaType("还款良好提额");
        }
        userCreditquotaLog.setAction("");
        userCreditquotaLog.setBeforeQuota(beforeQuota);
        userCreditquotaLog.setAfterQuota(afterQuota);
        userCreditquotaLog.setRemark("");
        userCreditquotaLog.setProjectName(PropertiesConfigUtil.get("RISK_BUSINESS"));
        userCreditquotaLogDao.addNewUserQuotaLog(userCreditquotaLog);
    }
}
