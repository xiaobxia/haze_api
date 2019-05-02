package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserInfoImageDao;
import com.vxianjin.gringotts.web.pojo.UserInfoImage;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class UserInfoImageDao extends BaseDao implements IUserInfoImageDao {

    /**
     * 上传图片
     */
    public int saveUserImage(UserInfoImage img) {
        return this.getSqlSessionTemplate().insert("user_info_image.saveUserImage", img);
    }

    /**
     * 查询图片
     */
    public List<UserInfoImage> selectImageByUserId(int uid) {
        return this.getSqlSessionTemplate().selectList("selectImageByUserId", uid);
    }

    /**
     * 修改图片
     */
    public int updateUserImageStatus(UserInfoImage img) {
        return this.getSqlSessionTemplate().update("updateUserImageStatus", img);
    }

    @Override
    public int countUserImage(HashMap<String, Object> map) {
        return this.getSqlSessionTemplate().selectOne("selectImageByUserId", map);
    }
}
