package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.dao.IPlatfromAdviseDao;
import com.vxianjin.gringotts.web.pojo.PlatfromAdvise;
import com.vxianjin.gringotts.web.service.IPlatfromAdviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PlatfromAdviseService implements IPlatfromAdviseService {

    @Autowired
    private IPlatfromAdviseDao platfromAdviseDao;
    @Autowired
    private IPaginationDao paginationDao;

    /**
     * 新增用户反馈内容
     */
    public int searchPlatfromAdvise(PlatfromAdvise plat) {
        return this.platfromAdviseDao.searchPlatfromAdvise(plat);
    }

    /**
     * 多条件 查询反馈内容
     */
    public PageConfig<PlatfromAdvise> selectPlatfromAdvise(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "PlatfromAdvise");
        return this.paginationDao.findPage("selectPlatfromAdvise", "selectPlatfromAdviseCount", params, "web");
    }

    /**
     * 根据id查询单个
     */
    public PlatfromAdvise selectPlatfromAdviseById(HashMap<String, Object> params) {
        return this.platfromAdviseDao.selectPlatfromAdviseById(params);
    }

    /**
     * 修改反馈内容
     */
    public int updatePlatfromAdvise(PlatfromAdvise plat) {
        return this.platfromAdviseDao.updatePlatfromAdvise(plat);
    }
}
