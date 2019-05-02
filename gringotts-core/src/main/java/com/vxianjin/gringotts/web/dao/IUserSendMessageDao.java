package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.UserSendMessage;

import java.util.Map;

public interface IUserSendMessageDao {
    /**
     * 新增短信
     */
    int saveUserSendMsg(UserSendMessage msg);

    /**
     * 查询近期发送的短信有没有超过一分钟
     */
    UserSendMessage lastMsg(Map<String, String> queryParams);
}
