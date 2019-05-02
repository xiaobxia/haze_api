package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IUserInfoImageDao;
import com.vxianjin.gringotts.web.pojo.UserInfoImage;
import com.vxianjin.gringotts.web.service.IUserInfoImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoImageService implements IUserInfoImageService {

    @Autowired
    private IUserInfoImageDao userInfoImageDao;

    /**
     * 上传图片
     */
    public int saveUserImage(UserInfoImage img) {
        return this.userInfoImageDao.saveUserImage(img);
    }

    /**
     * 查询图片
     */
    public List<UserInfoImage> selectImageByUserId(int uid) {
        return this.userInfoImageDao.selectImageByUserId(uid);
    }

    /**
     * 修改图片
     */
    public int updateUserImageStatus(UserInfoImage img) {
        return this.userInfoImageDao.updateUserImageStatus(img);
    }

}
