package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.Content;

import java.util.HashMap;
import java.util.List;

public interface IContentService {

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
     * 前台列表查看
     *
     * @param params channelType所属栏目
     * @return
     */
    PageConfig<Content> findPage(HashMap<String, Object> params);

    /**
     * 后台列表查看
     *
     * @param params channelType所属栏目<br>
     *               contentTitle标题<br>
     *               contentTxt内容
     * @return
     */
    PageConfig<Content> findBackPage(HashMap<String, Object> params);

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

    int deleteDrop(Integer id);
}
