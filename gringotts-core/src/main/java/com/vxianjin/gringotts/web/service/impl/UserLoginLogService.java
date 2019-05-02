package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IUserLoginLogDao;
import com.vxianjin.gringotts.web.pojo.UserLoginLog;
import com.vxianjin.gringotts.web.service.IUserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserLoginLogService implements IUserLoginLogService {

    @Autowired
    private IUserLoginLogDao userLoginLogDao;

    /***
     * 查询
     * @param params
     * @return
     */
    @Override
    public UserLoginLog selectByParams(Map<String, String> params) {
        return this.userLoginLogDao.selectByParams(params);
    }

    /****
     * 新增
     * @param log
     */
    @Override
    public void saveUserLoginLog(UserLoginLog log) {
        this.userLoginLogDao.saveUserLoginLog(log);
    }

    /***
     * 修改
     */
    public void updateUserLoginLog(UserLoginLog log) {
        this.userLoginLogDao.updateUserLoginLog(log);
    }
}
