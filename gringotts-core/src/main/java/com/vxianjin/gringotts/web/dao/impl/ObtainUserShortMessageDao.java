package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserAppSoftwareDao;
import com.vxianjin.gringotts.web.pojo.ObtainUserShortMessage;
import org.springframework.stereotype.Repository;

@Repository
public class ObtainUserShortMessageDao extends BaseDao implements IUserAppSoftwareDao {

    /**
     * 查询用户手机里的短信多少
     */
    public int selectObtainUserShortMessageCount(Integer userId) {
        return this.getSqlSessionTemplate().selectOne("selectObtainUserShortMessage", userId);
    }

    /**
     * 添加用户手机里的短信入库
     */
    public void saveObtainUserShortMessage(ObtainUserShortMessage soft) {
        this.getSqlSessionTemplate().insert("saveObtainUserShortMessage", soft);
    }

    /**
     * 根据用户id删除此用户的所有短信
     */
    public void delObtainUserShortMessage(Integer userId) {
        this.getSqlSessionTemplate().delete("delObtainUserShortMessage", userId);
    }
}
