package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.UserAppSoftware;

import java.util.List;

public interface IUserAppSoftwareService {
    /**
     * 查询用户手机里的应用软件
     */
    int selectUserAppSoftwareCount(Integer userId);

    /**
     * 添加用户手机里的应用软件入库
     */
    void saveUserAppSoftware(List<UserAppSoftware> soft);

    /**
     * 根据用户id删除此用户的所有应用软件
     */
    void delUserAppSoftware(Integer userId);
}
