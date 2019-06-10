package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.pay.model.UserQuotaSnapshot;
import com.vxianjin.gringotts.web.pojo.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jintian
 * @date 13:51
 */
public interface UserQuotaSnapshotService {
    /**
     * 根据用户获取用户可借额度
     * @param user 用户
     * @return
     */
    List<UserQuotaSnapshot> getUserQuotaSnapshotByUser(User user);

    /**
     * 远端获取用户额度
     * @return
     */
    Map<String,String> queryUserQuotaSnapshot(int userId, String amount);


    /**
     * 根据用户提额机制来返回应该提的额度---19.05.23版本
     * @param userId 用户ID
     * @param amount 本次还款金额
     * @param orderId 订单id
     * @return
     */
    Map<String,String> newQueryUserQuotaSnapshot(int userId, String amount, int orderId, int lateDay);

    void newUpdateUserQuotaSnapshots(int userId,long applyId,String repaymentedAmount, int orderId, int lateDay);

    /**
     * 用户额度更新
     */
    void updateUserQuotaSnapshots(int userId,long applyId,String repaymentedAmount);


    /**
     * 插入或更新用户借款额度
     * @param userId 用户id
     * @param productId 最新用户可借产品
     * @return
     */
    BigDecimal addOrUpdateUserQuotaSnapShot(int userId, int productId, int orderId);

    /**
     * 更新
     * @param userId 用户id
     * @param productId 现在产品id
     * @param beforeProductId 更新前产品id
     * @return
     */
    boolean updateUserQuota(int userId,int productId,int beforeProductId,BigDecimal nowLimit);

    /**
     * 添加
     * @param userId 用户id
     * @param productId 产品id
     * @return
     */
    boolean addUserQuota(int userId,int productId ,BigDecimal nowLimit,int borrowDay);
}
