package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.PlatfromAdvise;

import java.util.HashMap;

public interface IPlatfromAdviseService {

    /**
     * 新增用户反馈内容
     */
    int searchPlatfromAdvise(PlatfromAdvise plat);

    /**
     * 多条件 查询反馈内容
     */
    PageConfig<PlatfromAdvise> selectPlatfromAdvise(HashMap<String, Object> params);

    /**
     * 根据id查询单个
     */
    PlatfromAdvise selectPlatfromAdviseById(HashMap<String, Object> params);

    /**
     * 修改反馈内容
     */
    int updatePlatfromAdvise(PlatfromAdvise plat);
}
