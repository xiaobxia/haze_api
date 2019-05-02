package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.impl.UserAppSoftwareDao;
import com.vxianjin.gringotts.web.pojo.UserAppSoftware;
import com.vxianjin.gringotts.web.service.IUserAppSoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAppSoftwareService implements IUserAppSoftwareService {

    @Autowired
    private UserAppSoftwareDao userAppSoftwareDao;

    /**
     * 查询用户手机里的应用软件
     */
    public int selectUserAppSoftwareCount(Integer userId) {
        return this.userAppSoftwareDao.selectUserAppSoftwareCount(userId);
    }

    /**
     * 添加用户手机里的应用软件入库
     */
    public void saveUserAppSoftware(List<UserAppSoftware> soft) {
        for (int i = 0; i < soft.size(); i++) {
            this.userAppSoftwareDao.saveUserAppSoftware(soft.get(i));
        }
    }

    /**
     * 根据用户id删除此用户的所有应用软件
     */
    public void delUserAppSoftware(Integer userId) {
        this.userAppSoftwareDao.delUserAppSoftware(userId);
    }
}
