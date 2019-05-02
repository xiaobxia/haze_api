package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.dao.impl.ObtainUserShortMessageDao;
import com.vxianjin.gringotts.web.pojo.ObtainUserShortMessage;
import com.vxianjin.gringotts.web.service.IObtainUserShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ObtainUserShortMessageService implements IObtainUserShortMessageService {

    @Autowired
    private ObtainUserShortMessageDao userShortMessageDao;
    @Autowired
    private IPaginationDao paginationDao;

    /**
     * 查询用户手机里的短信多少
     */
    public int selectObtainUserShortMessageCount(Integer userId) {
        return this.userShortMessageDao.selectObtainUserShortMessageCount(userId);
    }

    /**
     * 添加用户手机里的短信入库
     */
    public void saveObtainUserShortMessage(List<ObtainUserShortMessage> soft) {
        for (int i = 0; i < soft.size(); i++) {
            try {
                this.userShortMessageDao.saveObtainUserShortMessage(soft.get(i));
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * 查询通讯录列表
     *
     * @param params
     * @return
     */
    public PageConfig<ObtainUserShortMessage> selectUserShortMsgList(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "ObtainUserShortMessage");
        return paginationDao.findPage("selectUserShortMsgPage", "selectUserShortMsgCount", params, "web");
    }

    /**
     * 根据用户id删除此用户的所有短信
     */
    public void delObtainUserShortMessage(Integer userId) {
        this.userShortMessageDao.delObtainUserShortMessage(userId);
    }
}
