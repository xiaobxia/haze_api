package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.UserCertification;

import java.util.List;
import java.util.Map;

public interface UserCerticationDao {
    /**
     * 查询认证列表
     *
     * @return
     */
    List<UserCertification> findCerticationList();

    /**
     * 验证用户已认证的选项
     *
     * @param params
     * @return
     */
    Map<String, Object> checkUserCalendar(Map<String, Object> params);
}
