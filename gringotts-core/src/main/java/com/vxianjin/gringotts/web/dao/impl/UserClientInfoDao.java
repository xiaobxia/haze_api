package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserClientInfoDao;
import com.vxianjin.gringotts.web.pojo.UserClientInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jintian
 * @date 17:41
 */
@Repository("userClientInfoDao")
public class UserClientInfoDao extends BaseDao implements IUserClientInfoDao {

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return this.getSqlSessionTemplate().delete("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.deleteByPrimaryKey",id);
    }

    @Override
    public int insert(UserClientInfo record) {
        return this.getSqlSessionTemplate().insert("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.insert",record);
    }

    @Override
    public int insertSelective(UserClientInfo record) {
        return this.getSqlSessionTemplate().insert("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.insertSelective",record);
    }

    @Override
    public UserClientInfo selectByPrimaryKey(Integer id) {
        return this.getSqlSessionTemplate().selectOne("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.selectByPrimaryKey",id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserClientInfo record) {
        return this.getSqlSessionTemplate().update("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.updateByPrimaryKeySelective",record);
    }

    @Override
    public int updateByPrimaryKey(UserClientInfo record) {
        return this.getSqlSessionTemplate().update("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.updateByPrimaryKey",record);
    }

    @Override
    public int updateByUserId(Integer userId,String clientId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        map.put("clientId",clientId);
        return this.getSqlSessionTemplate().update("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.updateByUserId",map);
    }

    @Override
    public String queryClientIdByUserId(int userId) {
        return this.getSqlSessionTemplate().selectOne("com.vxianjin.gringotts.web.dao.IUserClientInfoDao.queryClientIdByUserId",userId);
    }
}
