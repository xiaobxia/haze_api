package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.PlatfromAdvise;

import java.util.HashMap;

public interface IPlatfromAdviseDao {

    /**
     * 新增用户反馈内容
     */
    int searchPlatfromAdvise(PlatfromAdvise plat);

    /**
     * 根据id查询单个
     */
    PlatfromAdvise selectPlatfromAdviseById(HashMap<String, Object> params);

    /**
     * 修改反馈内容
     */
    int updatePlatfromAdvise(PlatfromAdvise plat);
}
