package com.vxianjin.gringotts.attach.service;


import com.vxianjin.gringotts.attach.dao.IUserInfoTudeDao;
import com.vxianjin.gringotts.attach.pojo.UserInfoTude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 借款用户坐标信息
 *
 * @author zzc
 */
@Service
public class UserInfoTudeService implements IUserInfoTudeService {

    @Autowired
    private IUserInfoTudeDao userInfoTudeDao;

    @Override
    public int insertUserInfoTude(UserInfoTude params) {
        return userInfoTudeDao.insertUserInfoTude(params);
    }

}
