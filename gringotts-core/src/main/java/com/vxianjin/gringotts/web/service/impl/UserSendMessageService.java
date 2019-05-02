package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IUserSendMessageDao;
import com.vxianjin.gringotts.web.pojo.UserSendMessage;
import com.vxianjin.gringotts.web.service.IUserSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserSendMessageService implements IUserSendMessageService {

    @Autowired
    private IUserSendMessageDao userSendMessageDao;

    /**
     * 新增短信
     */
    public int saveUserSendMsg(UserSendMessage msg) {
        return this.userSendMessageDao.saveUserSendMsg(msg);
    }

    /**
     * 查询近期发送的短信有没有超过一分钟
     */
    public UserSendMessage lastMsg(Map<String, String> queryParams) {
        return this.userSendMessageDao.lastMsg(queryParams);
    }

}
