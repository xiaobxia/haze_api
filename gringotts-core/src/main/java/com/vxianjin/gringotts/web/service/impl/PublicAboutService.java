package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IPublicAboutDao;
import com.vxianjin.gringotts.web.pojo.PublicAbout;
import com.vxianjin.gringotts.web.service.IPublicAboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicAboutService implements IPublicAboutService {

    @Autowired
    private IPublicAboutDao publicAboutDao;

    /**
     * 新增关于
     */
    public int searchPublicAbout(PublicAbout about) {
        return this.publicAboutDao.searchPublicAbout(about);
    }

    /**
     * 查询关于
     */
    public PublicAbout selectPublicAbout() {
        return this.publicAboutDao.selectPublicAbout();
    }

    /**
     * 修改关于
     */
    public int updatePublicAbout(PublicAbout about) {
        return this.publicAboutDao.updatePublicAbout(about);
    }

}
