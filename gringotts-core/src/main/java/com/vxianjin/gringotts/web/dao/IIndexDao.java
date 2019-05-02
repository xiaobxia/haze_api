package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.index.IndexDto;

import java.util.HashMap;
import java.util.List;

public interface IIndexDao {

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
     * 查询借款信息
     *
     * @param map
     * @return
     */
    BorrowOrder searchBorrowOrderByIndex(HashMap<String, Object> map);

    /**
     * 根据用户ID更新IndexInfo信息-old
     *
     * @param indexInfo
     * @return
     */
    int updateIndexInfoByUserIdOld(InfoIndexInfo indexInfo);

    /**
     * 根据用户ID更新IndexInfo信息
     *
     * @param indexInfo
     * @return
     */
    int updateIndexInfoByUserId(InfoIndexInfo indexInfo);

    /**
     * 保存用户动态数据
     *
     * @param indexInfo
     */
    void saveInfoIndexInfo(InfoIndexInfo indexInfo);

    /**
     * 查询银行卡
     *
     * @param map
     * @return
     */
    UserCardInfo searchUserCardInfo(HashMap<String, Object> map);

    /**
     * 处理button按钮事件
     *
     * @param map
     */
    void updateInfoUserInfoBorrowStatus(HashMap<String, Object> map);

    /**
     * 通过id查询用户
     *
     * @param map
     * @return
     */
    User searchUserByIndex(HashMap<String, Object> map);

    /**
     * 查询老用户
     *
     * @param map
     * @return
     */
    UserInfoOld searchUserInfoOld(HashMap<String, Object> map);

    /**
     * 更新老用户
     *
     * @param map
     */
    void updateUserInfoOld(HashMap<String, Object> map);

    /**
     * 退出删除用户信息
     *
     * @param map
     */
    void delIndexUser(HashMap<String, Object> map);

    /**
     * 查询首页图片
     *
     * @param map
     * @return
     */
    List<InfoImage> searchInfoImage(HashMap<String, Object> map);
}
