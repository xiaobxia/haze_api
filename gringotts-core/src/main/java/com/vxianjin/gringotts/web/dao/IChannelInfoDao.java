package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.ChannelInfo;

import java.util.List;
import java.util.Map;

public interface IChannelInfoDao {

    /**
     * 根据条件查询
     *
     * @param map 参数名 ： <br>
     *            参数名 ：
     * @return list
     */
    List<ChannelInfo> findAll(Map<String, Object> params);

    List<String> findAllChUser(Map<String, Object> params);

    /**
     * 插入对象
     *
     * @param backUser
     */
    void insert(ChannelInfo channelInfo);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 更新对象
     *
     * @param backUser
     */
    void updateById(ChannelInfo channelInfo);


    /**
     * 插入推广员用户
     *
     * @param backUser
     */
    void insertChannelUserInfo(Map<String, Object> param);


    Integer findChannelIdByCode(String channelCode);

    Integer findUserIdByChannelId(Integer channelId);

    ChannelInfo findById(Integer channelId);
}
