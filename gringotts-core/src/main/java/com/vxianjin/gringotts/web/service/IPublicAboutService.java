package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.PublicAbout;

public interface IPublicAboutService {

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
