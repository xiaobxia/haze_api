package com.vxianjin.gringotts.pay.service;

import java.math.BigDecimal;

/**
 * @Author: chenkai
 * @Date: 2018/9/11 16:55
 * @Description: 用户提额日志记录服务
 */
public interface UserQuotaLogService {


    void addUserQuotaLog(int userId, BigDecimal beforeQuota, BigDecimal afterQuota);
}
