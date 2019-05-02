package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.ObtainUserShortMessage;

import java.util.HashMap;
import java.util.List;

public interface IObtainUserShortMessageService {
    /**
     * 查询用户手机里的短信多少
     */
    int selectObtainUserShortMessageCount(Integer userId);

    /**
     * 添加用户手机里的短信入库
     */
    void saveObtainUserShortMessage(List<ObtainUserShortMessage> soft);

    /**
     * 查询通讯录列表
     *
     * @param params
     * @return
     */
    PageConfig<ObtainUserShortMessage> selectUserShortMsgList(HashMap<String, Object> params);

    /**
     * 根据用户id删除此用户的所有短信
     */
    void delObtainUserShortMessage(Integer userId);
}
