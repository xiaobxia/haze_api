package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserInfoOld;

import java.util.HashMap;

/**
 * 从core中提取出来
 */
public interface IInfoIndexService {
    /**
     * 添加infoindexinfo
     *
     * @param user
     */
    void addInfoIndexInfo(User user);

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authInfo(HashMap<String, Object> map);

    /**
     * 紧急联系人认证
     *
     * @param map
     */
    void authContacts(HashMap<String, Object> map);

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authBankOld(HashMap<String, Object> map);

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authBank(HashMap<String, Object> map);

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authSesame(HashMap<String, Object> map);

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authMobile(HashMap<String, Object> map);

    /**
     * 重置用户信息
     *
     * @param userId
     */
    void sendDynamic(Integer userId);

    /**
     * 更新用户额度
     *
     * @param map
     */
    void changeUserAmount(HashMap<String, Object> map);

    /**
     * 查询老用户
     *
     * @param map
     * @return
     */
    UserInfoOld searchUserInfoOld(HashMap<String, Object> map);

    /**
     * 更新老用户
     *
     * @param map
     */
    void updateUserInfoOld(HashMap<String, Object> map);
}
