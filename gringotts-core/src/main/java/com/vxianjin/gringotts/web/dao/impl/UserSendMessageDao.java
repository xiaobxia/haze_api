package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserSendMessageDao;
import com.vxianjin.gringotts.web.pojo.UserSendMessage;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserSendMessageDao extends BaseDao implements IUserSendMessageDao {

    /**
     * 新增短信
     */
    public int saveUserSendMsg(UserSendMessage msg) {
        return this.getSqlSessionTemplate().insert("saveUserSendMsg", msg);
    }

    /**
     * 查询近期发送的短信有没有超过一分钟
     */
    public UserSendMessage lastMsg(Map<String, String> queryParams) {
        return this.getSqlSessionTemplate().selectOne("lastMsg", queryParams);
    }
}
