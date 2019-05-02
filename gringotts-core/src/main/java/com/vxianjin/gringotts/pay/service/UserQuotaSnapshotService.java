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
    Map<String,String> queryUserQuotaSnapshot(int userId);

    /**
     * 用户额度更新
     */
    void updateUserQuotaSnapshots(int userId,long applyId);

    /**
     * 插入或更新用户借款额度
     * @param userId 用户id
     * @param nowLimit 最新用户借款额度
     * @param borrowDay 借款天数
     * @return
     */
    boolean addOrUpdateUserQuotaSnapShot(int userId,String nowLimit,int borrowDay);

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
