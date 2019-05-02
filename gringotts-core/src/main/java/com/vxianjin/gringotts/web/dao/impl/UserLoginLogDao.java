package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserLoginLogDao;
import com.vxianjin.gringotts.web.pojo.UserLoginLog;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserLoginLogDao extends BaseDao implements IUserLoginLogDao {

    /***
     * 查询
     * @param params
     * @return
     */
    @Override
    public UserLoginLog selectByParams(Map<String, String> params) {
        return getSqlSessionTemplate().selectOne("selectByParams", params);
    }

    /****
     * 新增
     * @param log
     */
    @Override
    public void saveUserLoginLog(UserLoginLog log) {
        getSqlSessionTemplate().insert("saveUserLoginLog", log);
    }

    /***
     * 修改
     */
    public void updateUserLoginLog(UserLoginLog log) {
        this.getSqlSessionTemplate().update("updateUserLoginLog", log);
    }
}
