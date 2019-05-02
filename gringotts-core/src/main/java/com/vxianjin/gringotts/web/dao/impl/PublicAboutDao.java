package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IPublicAboutDao;
import com.vxianjin.gringotts.web.pojo.PublicAbout;
import org.springframework.stereotype.Repository;

@Repository
public class PublicAboutDao extends BaseDao implements IPublicAboutDao {

    /**
     * 新增关于
     */
    public int searchPublicAbout(PublicAbout about) {
        return this.getSqlSessionTemplate().insert("searchPublicAbout", about);
    }

    /**
     * 查询关于
     */
    public PublicAbout selectPublicAbout() {
        return this.getSqlSessionTemplate().selectOne("selectPublicAbout");
    }

    /**
     * 修改关于
     */
    public int updatePublicAbout(PublicAbout about) {
        return this.getSqlSessionTemplate().update("updatePublicAbout", about);
    }

}
