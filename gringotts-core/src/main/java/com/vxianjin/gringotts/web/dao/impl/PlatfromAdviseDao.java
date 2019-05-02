package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IPlatfromAdviseDao;
import com.vxianjin.gringotts.web.pojo.PlatfromAdvise;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class PlatfromAdviseDao extends BaseDao implements IPlatfromAdviseDao {

    /**
     * 新增用户反馈内容
     */
    public int searchPlatfromAdvise(PlatfromAdvise plat) {
        return this.getSqlSessionTemplate().insert("searchPlatfromAdvise", plat);
    }

    /**
     * 根据id查询单个
     */
    public PlatfromAdvise selectPlatfromAdviseById(HashMap<String, Object> params) {
        return this.getSqlSessionTemplate().selectOne("selectPlatfromAdviseById", params);
    }

    /**
     * 修改反馈内容
     */
    public int updatePlatfromAdvise(PlatfromAdvise plat) {
        return this.getSqlSessionTemplate().update("updatePlatfromAdvise", plat);
    }
}
