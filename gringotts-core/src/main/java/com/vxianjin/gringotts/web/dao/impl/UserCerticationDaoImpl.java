package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.UserCerticationDao;
import com.vxianjin.gringotts.web.pojo.UserCertification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("userCertication")
public class UserCerticationDaoImpl extends BaseDao implements UserCerticationDao {

    @Override
    public List<UserCertification> findCerticationList() {
        return this.getSqlSessionTemplate().selectList("userCertification.selectCertificationList");
    }

    @Override
    public Map<String, Object> checkUserCalendar(Map<String, Object> params) {
        return this.getSqlSessionTemplate().selectOne("userCertification.userCalendar", params);
    }

}
