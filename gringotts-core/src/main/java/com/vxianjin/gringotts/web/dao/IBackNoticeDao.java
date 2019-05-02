package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BackNotice;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface IBackNoticeDao {
    /**
     * @param params
     * @return
     */
    List<BackNotice> findParams(HashMap<String, Object> params);

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据主键更新
     *
     * @param id
     * @return
     */
    int update(BackNotice backNotice);

    /**
     * 插入
     *
     * @param BackNotice
     * @return
     */
    int insert(BackNotice backNotice);
}
