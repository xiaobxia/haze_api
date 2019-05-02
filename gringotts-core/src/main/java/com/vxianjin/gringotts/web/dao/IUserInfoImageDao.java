package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.UserInfoImage;

import java.util.HashMap;
import java.util.List;

public interface IUserInfoImageDao {

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

    /**
     * 统计图片
     *
     * @param map
     * @return
     */
    int countUserImage(HashMap<String, Object> map);
}
