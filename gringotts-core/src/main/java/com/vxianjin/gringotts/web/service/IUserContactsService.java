package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.UserContacts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IUserContactsService {

    /**
     * 查询用户通讯录有多少联系人
     */
    int selectUserContactCount(Integer userId);

    /**
     * 根据用户id删除此用户的所有联系人
     */
    void delUserContact(Integer userId);

    /**
     * 查询通讯录列表
     *
     * @param params
     * @return
     */
    PageConfig<UserContacts> selectUserContactsList(HashMap<String, Object> params);

    /**
     * 查询通信录
     *
     * @param params
     * @return
     */
    List<UserContacts> selectUserContacts(Map<String, Object> params);


    /**
     * 新增联系人
     */
    int batchInsert(HashMap<String, Object> params);


    /**
     * 新增联系人
     */
    void saveUserContacts(List<UserContacts> contacts);


}
