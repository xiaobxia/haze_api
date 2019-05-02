package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.UserContacts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IUserContactsDao {

    List<UserContacts> findForDel(Integer userId);

    /**
     * 查询用户通讯录有多少联系人
     */
    int selectUserContactCount(Integer userId);

    /**
     * 新增联系人
     */
    void saveUserContacts(UserContacts contacts);

    /**
     * 根据用户id删除此用户的所有联系人
     */
    void delUserContact(Integer userId);

    /**
     * 查询联系人信息
     *
     * @param params
     * @return
     */
    List<UserContacts> selectUserContacts(Map<String, Object> params);

    int batchInsert(HashMap<String, Object> params);

    int deleteRepeat(String ids);
}
