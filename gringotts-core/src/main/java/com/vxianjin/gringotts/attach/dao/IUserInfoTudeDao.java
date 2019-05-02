package com.vxianjin.gringotts.attach.dao;


import com.vxianjin.gringotts.attach.pojo.UserInfoTude;

public interface IUserInfoTudeDao {
    /**
     * 保存借款用户经纬度
     *
     * @param params
     * @return
     */
    int insertUserInfoTude(UserInfoTude params);
}