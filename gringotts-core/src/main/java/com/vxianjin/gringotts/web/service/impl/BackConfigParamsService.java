package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IBackConfigParamsDao;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.service.IBackConfigParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BackConfigParamsService implements IBackConfigParamsService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    IBackConfigParamsDao backConfigParamsDao;

    @Override
    public List<BackConfigParams> findParams(HashMap<String, Object> params) {

        return backConfigParamsDao.findParams(params);
    }

    @Override
    public int updateValue(List<BackConfigParams> list, String type) {
        int result = backConfigParamsDao.updateValue(list);
        if (result > 0) {
        }
        return result;
    }
}
