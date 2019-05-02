package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author dongyukai 2017-09-28
 * 从back.dao包迁移到web.dao
 */
@Repository
public interface IBackConfigParamsDao {
    /**
     * @param params sysType参数分类
     * @return
     */
    List<BackConfigParams> findParams(HashMap<String, Object> params);

    /**
     * 更新
     *
     * @param list
     * @return
     */
    int updateValue(List<BackConfigParams> list);
}
