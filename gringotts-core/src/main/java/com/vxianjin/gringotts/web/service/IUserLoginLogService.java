package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.UserLoginLog;

import java.util.Map;


public interface IUserLoginLogService {
    /***
     * 查询
     * @param params
     * @return
     */
    UserLoginLog selectByParams(Map<String, String> params);

    /****
     * 新增
     * @param log
     */
    void saveUserLoginLog(UserLoginLog log);

    /***
     * 修改
     */
    void updateUserLoginLog(UserLoginLog log);
}
