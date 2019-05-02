package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.dao.IUserContactsDao;
import com.vxianjin.gringotts.web.pojo.UserContacts;
import com.vxianjin.gringotts.web.service.IUserContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserContactsService implements IUserContactsService {

    @Autowired
    private IUserContactsDao userContactsDao;
    @Autowired
    private IPaginationDao paginationDao;

    /**
     * 查询用户通讯录有多少联系人
     */
    public int selectUserContactCount(Integer userId) {
        return this.userContactsDao.selectUserContactCount(userId);
    }

    /**
     * 新增联系人
     */
    public int batchInsert(HashMap<String, Object> params) {
        if(params == null || params.get("concatList") == null || ((List)params.get("concatList")).size() <=0 ){
            return 0;
        }
        userContactsDao.batchInsert(params);
        Integer userId = Integer.valueOf(params.get("userId") + "");
        List<UserContacts> list = userContactsDao.findForDel(userId);
        if (list != null && list.size() > 0) {
            Map<String, String> map = new HashMap<String, String>();
            StringBuffer ids = new StringBuffer();
            for (UserContacts userContacts : list) {
                String id = userContacts.getId();
                String userPhone = userContacts.getContactPhone();
                if (map.get(userPhone) == null) {
                    map.put(userPhone, id);
                } else {
                    ids.append(id + ",");
                }
            }
            if (ids.length() > 0) {
                // 去重复
                userContactsDao.deleteRepeat(ids.substring(0, ids.length() - 1));
            }
            list = null;
            map = null;
            ids = null;
        }
        // 查询最新的size
        return userContactsDao.selectUserContactCount(userId);
    }

    /**
     * 根据用户id删除此用户的所有联系人
     */
    public void delUserContact(Integer userId) {
        this.userContactsDao.delUserContact(userId);
    }

    @Override
    public PageConfig<UserContacts> selectUserContactsList(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "UserContacts");
        return paginationDao.findPage("selectUserContactsPage", "selectUserContactsCount", params, "web");
    }

    @Override
    public List<UserContacts> selectUserContacts(Map<String, Object> params) {
        return this.userContactsDao.selectUserContacts(params);
    }

    /**
     * 新增联系人
     */
    @Override
    public void saveUserContacts(List<UserContacts> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            this.userContactsDao.saveUserContacts(contacts.get(i));
        }
    }
}
