package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.UserInfoImage;

import java.util.List;

public interface IUserInfoImageService {

    /**
     * 上传图片
     */
    int saveUserImage(UserInfoImage img);

    /**
     * 查询图片
     */
    List<UserInfoImage> selectImageByUserId(int uid);

    /**
     * 修改图片
     */
    int updateUserImageStatus(UserInfoImage img);
}
