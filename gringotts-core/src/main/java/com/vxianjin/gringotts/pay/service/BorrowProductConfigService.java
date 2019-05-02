package com.vxianjin.gringotts.pay.service;

import com.alibaba.fastjson.JSONArray;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.pay.model.UserQuotaSnapshot;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jintian
 * @date 13:50
 */
public interface BorrowProductConfigService {

    /**
     * 查询所有可用借款产品线
     *
     * @return
     */
    public List<BorrowProductConfig> queryAllBorrowProductConfig();

    /**
     * 根据用户期限可借最大额度产品线获取所有该用户同期限可借产品线
     *
     * @param userQuotaSnapshot
     * @return
     */
    public List<BorrowProductConfig> queryUserBorrowProductConfig(UserQuotaSnapshot userQuotaSnapshot);

    /**
     * 提供页面展示所有可借款产品信息
     *
     * @return
     */
    public JSONArray queryIndexAmountDayList();

    /**
     * 提供页面该用户可借款产品信息
     *
     * @param userQuotaSnapshots
     * @return
     */
    public JSONArray queryIndexUserAllowAmountDayList(List<UserQuotaSnapshot> userQuotaSnapshots);

    /**
     * @param userId
     * @param day
     * @return
     */
    public BorrowProductConfig queryUserConfigByUserIdAndDay(int userId, int day);

    /**
     * 获取最大额度的产品线
     *
     * @param nowLimit  借款额度
     * @param borrowDay 借款天数
     * @return
     */
    BorrowProductConfig queryMaxLimitProduct(int nowLimit, int borrowDay);
}
