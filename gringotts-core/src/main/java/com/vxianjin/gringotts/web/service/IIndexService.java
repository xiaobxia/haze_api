package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.index.IndexDto;

import java.util.HashMap;
import java.util.List;

public interface IIndexService {
    /**
     * 查询首页内容
     *
     * @param map
     * @return
     */
    InfoIndex searchInfoIndex(HashMap<String, Object> map);

    /**
     * 查询公告
     *
     * @param map
     * @return
     */
    List<InfoNotice> searchInfoNoticeByIndex(HashMap<String, Object> map);

    /**
     * 保存首页信息
     *
     * @param indexDto
     */
    void saveIndexDto(IndexDto indexDto);

    /**
     * 查询首页信息
     *
     * @param map
     * @return
     */
    IndexDto searchIndexDto(HashMap<String, Object> map);

    /**
     * 动态查询用户信息
     *
     * @param map
     * @return
     */
    InfoIndexInfo searchInfoIndexInfo(HashMap<String, Object> map);

    /**
     * 获取默认信息
     *
     * @return
     */
    String getDefaultJson();

    /**
     * 获取借款信息
     *
     * @param userId
     * @return
     */
    String getLoanInfos(InfoIndexInfo indexInfo);


    /**
     * 根据用户ID存放indexinfo
     *
     * @param indexInfo
     * @return
     */
    int updateIndexInfoByUserId(InfoIndexInfo indexInfo);

    /**
     * 处理button按钮事件
     *
     * @param map
     */
    void updateInfoUserInfoBorrowStatus(HashMap<String, Object> map);

    /**
     * 查询user
     *
     * @param map
     * @return
     */
    User searchUserByIndex(HashMap<String, Object> map);

    /**
     * 查询首页图片
     *
     * @param map
     * @return
     */
    List<InfoImage> searchInfoImage(HashMap<String, Object> map);


    String getIosUpdageImage();
}
