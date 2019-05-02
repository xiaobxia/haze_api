package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.Content;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContentDao {
    /**
     * 插入
     *
     * @param zbNews
     * @return
     */
    int insert(Content content);

    /**
     * 删除
     *
     * @return
     */
    int delete(Content content);

    int deleteDrop(Integer id);

    /**
     * 修改
     *
     * @return
     */
    int update(Content content);

    /**
     * 修改点击量
     *
     * @return
     */
    int updateViewCount(Integer id);

    /**
     * 根据主键查询
     *
     * @param id主键
     * @return
     */
    Content findById(Integer id);

    /**
     * 批量插入文章
     *
     * @param list
     * @return
     */
    int batchInsert(List<Content> list);

    /**
     * 批量删除文章
     *
     * @param list
     * @return
     */
    int deleteByFromUrl(List<String> list);
}
