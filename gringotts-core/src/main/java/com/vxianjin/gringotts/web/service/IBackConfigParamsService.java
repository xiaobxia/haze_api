package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.BackConfigParams;

import java.util.HashMap;
import java.util.List;

public interface IBackConfigParamsService {
    /**
     * @param params sysType参数分类ASSETS_TYPE是资产类型
     * @return
     */
    List<BackConfigParams> findParams(HashMap<String, Object> params);

    /**
     * 更新
     *
     * @param list
     * @return
     */
    int updateValue(List<BackConfigParams> list, String type);
}
