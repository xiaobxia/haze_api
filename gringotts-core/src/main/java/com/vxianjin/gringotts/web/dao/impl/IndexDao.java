package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IIndexDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.index.IndexDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class IndexDao extends BaseDao implements IIndexDao {

    /**
     * 查询首页内容
     *
     * @param map
     * @return
     */
    @Override
    public InfoIndex searchInfoIndex(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchInfoIndex", map);
    }

    /**
     * 查询公告
     *
     * @param map
     * @return
     */
    @Override
    public List<InfoNotice> searchInfoNoticeByIndex(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectList("searchInfoNoticeByIndex", map);
    }

    /**
     * 保存首页信息
     *
     * @param indexDto
     */
    @Override
    public void saveIndexDto(IndexDto indexDto) {
        getSqlSessionTemplate().delete("delIndexDto");
        getSqlSessionTemplate().insert("saveIndexDto", indexDto);
    }

    /**
     * 查询首页信息
     *
     * @param map
     * @return
     */
    @Override
    public IndexDto searchIndexDto(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchIndexDto", map);
    }

    /**
     * 动态查询用户信息
     *
     * @param map
     * @return
     */
    @Override
    public InfoIndexInfo searchInfoIndexInfo(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchInfoIndexInfo", map);
    }

    /**
     * 查询借款信息
     *
     * @param map
     * @return
     */
    @Override
    public BorrowOrder searchBorrowOrderByIndex(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchBorrowOrderByIndex", map);
    }
    @Override
    public int updateIndexInfoByUserIdOld(InfoIndexInfo indexInfo) {
        return getSqlSessionTemplate().update("updateIndexInfoByUserIdOld", indexInfo);
    }

    /* (non-Javadoc)
     * @see com.vxianjin.gringotts.web.dao.IIndexDao#updateIndexInfoByUserId(com.vxianjin.gringotts.web.pojo.InfoIndexInfo)
     */
    @Override
    public int updateIndexInfoByUserId(InfoIndexInfo indexInfo) {
        return getSqlSessionTemplate().update("updateIndexInfoByUserId", indexInfo);
    }

    /**
     * 保存用户动态数据
     *
     * @param indexInfo
     */
    @Override
    public void saveInfoIndexInfo(InfoIndexInfo indexInfo) {
        getSqlSessionTemplate().insert("saveInfoIndexInfo", indexInfo);
    }

    /**
     * 查询银行卡
     *
     * @param map
     * @return
     */
    @Override
    public UserCardInfo searchUserCardInfo(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchUserCardInfo", map);
    }

    /**
     * 处理button按钮事件
     *
     * @param map
     */
    @Override
    public void updateInfoUserInfoBorrowStatus(HashMap<String, Object> map) {
        getSqlSessionTemplate().update("updateInfoUserInfoBorrowStatus", map);
    }

    /**
     * 通过id查询用户
     *
     * @param map
     * @return
     */
    @Override
    public User searchUserByIndex(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchUserByIndex", map);
    }

    /**
     * 查询老用户
     *
     * @param map
     * @return
     */
    @Override
    public UserInfoOld searchUserInfoOld(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectOne("searchUserInfoOld", map);
    }

    /**
     * 更新老用户
     *
     * @param map
     */
    @Override
    public void updateUserInfoOld(HashMap<String, Object> map) {
        getSqlSessionTemplate().update("updateUserInfoOld", map);
    }

    /**
     * 退出删除用户信息
     *
     * @param map
     */
    @Override
    public void delIndexUser(HashMap<String, Object> map) {
        getSqlSessionTemplate().update("delIndexUser", map);
    }

    /**
     * 查询首页图片
     *
     * @param map
     * @return
     */
    @Override
    public List<InfoImage> searchInfoImage(HashMap<String, Object> map) {
        return getSqlSessionTemplate().selectList("searchInfoImage", map);
    }
}
