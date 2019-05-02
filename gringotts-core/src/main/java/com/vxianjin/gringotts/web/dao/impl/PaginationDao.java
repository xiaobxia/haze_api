package com.vxianjin.gringotts.web.dao.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * 分页
 *
 * @param <T>
 * @author gaoyuhai 2016-6-22 下午03:52:43
 */
@SuppressWarnings("unchecked")
@Repository
public class PaginationDao<T> extends BaseDao implements IPaginationDao<T> {
    @Override
    public PageConfig<T> findPage(String listSql, String countSql, HashMap map,
                                  String type) {
        String nameSpace = "";
        if (map.containsKey(Constant.NAME_SPACE)) {
            nameSpace = "com.vxianjin.gringotts." + type + ".dao.I"
                    + map.get(Constant.NAME_SPACE) + "Dao.";
        }
        Integer curPage = 1;
        if (map.containsKey(Constant.CURRENT_PAGE)
                && StringUtils.isNoneBlank(String.valueOf(map
                .get(Constant.CURRENT_PAGE)))) {
            curPage = Integer.valueOf(String.valueOf(map
                    .get(Constant.CURRENT_PAGE)));
        }

        Integer numPerPage = 10;
        if (map.containsKey(Constant.PAGE_SIZE)
                && StringUtils.isNoneBlank(String.valueOf(map
                .get(Constant.PAGE_SIZE)))) {
            numPerPage = Integer.valueOf(String.valueOf(map
                    .get(Constant.PAGE_SIZE)));
        }

        // 得到数据记录总数
        Integer totalRecord = getSqlSessionTemplate().selectOne(
                nameSpace + countSql, map);
        PageConfig<T> pageConfig = new PageConfig<T>(totalRecord, numPerPage,
                curPage);
        if (pageConfig.getTotalResultSize() > 0) {
            pageConfig.setItems(getSqlSessionTemplate().selectList(
                    nameSpace + listSql, map,
                    new PageBounds(curPage, numPerPage, null, false)));
        }
        return pageConfig;

    }

    @Override
    public PageConfig getMyPage(String listId, String countId, HashMap map,
                                String type) {
        String nameSpace = "";
        if (map.containsKey(Constant.NAME_SPACE)) {
            if (StringUtils.isNotBlank(type)) {
                nameSpace = "com.vxianjin.gringotts.web.dao.I" + map.get(Constant.NAME_SPACE)
                        + "Dao.";
            } else {
                nameSpace = "com.vxianjin.gringotts.back.dao.I"
                        + map.get(Constant.NAME_SPACE) + "Dao.";
            }
        }
        Integer curPage = 1;
        if (map.containsKey(Constant.CURRENT_PAGE)
                && StringUtils.isNoneBlank(String.valueOf(map
                .get(Constant.CURRENT_PAGE)))) {
            curPage = Integer.valueOf(String.valueOf(map
                    .get(Constant.CURRENT_PAGE)));
        }

        Integer numPerPage = 10;
        if (map.containsKey(Constant.PAGE_SIZE)
                && StringUtils.isNoneBlank(String.valueOf(map
                .get(Constant.PAGE_SIZE)))) {
            numPerPage = Integer.valueOf(String.valueOf(map
                    .get(Constant.PAGE_SIZE)));
        }

        map.put(Constant.CURRENT_PAGE, (curPage - 1) * numPerPage);
        map.put(Constant.PAGE_SIZE, numPerPage);
        // 得到数据记录总数
        Integer totalRecord = getSqlSessionTemplate().selectOne(
                nameSpace + countId, map);
        PageConfig pageConfig = new PageConfig(totalRecord, numPerPage, curPage);
        pageConfig.setTotalResultSize(totalRecord);
        if (pageConfig.getTotalPageNum() > 0) {
            // fyc注释，pagebounds中后两个参数不传，到这里会默认在list方法外包裹一层select count(1)，在查询一次
            pageConfig.setItems(getSqlSessionTemplate().selectList(
                    nameSpace + listId, map));
        }
        return pageConfig;
    }

    @Override
    public PageConfig<T> findWriteOffPage(String listSql, String countSql, HashMap map,
                                          String type) {
        String nameSpace = "";
        if (map.containsKey(Constant.NAME_SPACE)) {
            nameSpace = "com.info." + type + ".dao.I"
                    + map.get(Constant.NAME_SPACE) + "Dao.";
        }
        Integer curPage = 1;
        if (map.containsKey(Constant.CURRENT_PAGE)
                && StringUtils.isNotBlank(String.valueOf(map
                .get(Constant.CURRENT_PAGE)))) {
            curPage = Integer.valueOf(String.valueOf(map
                    .get(Constant.CURRENT_PAGE)));
        }

        Integer numPerPage = 10;
        if (map.containsKey(Constant.PAGE_SIZE)
                && StringUtils.isNotBlank(String.valueOf(map
                .get(Constant.PAGE_SIZE)))) {
            numPerPage = Integer.valueOf(String.valueOf(map
                    .get(Constant.PAGE_SIZE)));
        }

        // 得到数据记录总数
        Integer totalRecord = getSqlSessionTemplate().selectOne(
                nameSpace + countSql, map);
        PageConfig<T> pageConfig = new PageConfig<T>(totalRecord, numPerPage,
                curPage);
        if (pageConfig.getTotalResultSize() > 0) {
            pageConfig.setItems(getSqlSessionTemplate().selectList(
                    nameSpace + listSql, map,
                    new PageBounds(curPage, numPerPage, null, false)));
        }
        return pageConfig;

    }
}
