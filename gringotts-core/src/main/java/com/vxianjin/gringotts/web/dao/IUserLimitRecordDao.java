package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.UserLimitRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IUserLimitRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLimitRecord record);

    int insertSelective(UserLimitRecord record);

    UserLimitRecord selectByPrimaryKey(Integer id);

    List<UserLimitRecord> findListBayParams(HashMap<String, Object> params);


    int updateByPrimaryKeySelective(UserLimitRecord record);

    int updateByPrimaryKey(UserLimitRecord record);

    BigDecimal countAddAmount(Integer userId);
}