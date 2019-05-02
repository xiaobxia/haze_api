package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.PublicAbout;

public interface IPublicAboutDao {

    /**
     * 新增关于
     */
    int searchPublicAbout(PublicAbout about);

    /**
     * 查询关于
     */
    PublicAbout selectPublicAbout();

    /**
     * 修改关于
     */
    int updatePublicAbout(PublicAbout about);
}
