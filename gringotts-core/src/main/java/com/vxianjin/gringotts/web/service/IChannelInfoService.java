package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.ChannelInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：推广 <br>
 * 创建人：<br>
 * 创建时间：2016年12月21日 14:19:54 <br>
 */
public interface IChannelInfoService {
    /**
     * 根据条件查询用户
     *
     * @param map 参数名 ：userAccount，含义：用户名 <br>
     *            参数名 ：status 含义：状态
     * @return 角色list
     */
    List<ChannelInfo> findAll(Map<String, Object> params);

    List<String> findAllChUser(Map<String, Object> params);

    Integer findChannelIdByCode(String channelCode);

    Integer findUserIdByChannelId(Integer channelId);

    /**
     * 根据条件查询用户<br>
     * 只返回第一个用户对象<br>
     *
     * @param map 参数名 ：userAccount，含义：用户名 <br>
     *            参数名 ：status 含义：状态 参数名：id 含义：用户主键
     * @return 用户对象
     */
    ChannelInfo findOneChannelInfo(HashMap<String, Object> params);

    ChannelInfo findById(Integer id);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    void deleteChannelInfoById(Integer id);

    /**
     * 插入用户对象
     *
     * @param backUser
     */
    void insert(ChannelInfo channelInfo);

    /**
     * 插入推广员用户
     *
     * @param backUser
     */
    void insertChannelUserInfo(Map<String, Object> param);

    /**
     * 更新用户对象
     *
     * @param backUser
     */
    void updateById(ChannelInfo channelInfo);

    /**
     * 分页查询推广渠道信息
     *
     * @param params
     * @return
     */
    PageConfig<ChannelInfo> findPage(HashMap<String, Object> params);

    /**
     * 分页查询推广员信息
     *
     * @param params
     * @return
     */
    PageConfig<ChannelInfo> findChannelUserPage(HashMap<String, Object> params);

    /**
     * 分页查询推广记录
     *
     * @param params
     * @return
     */
    PageConfig<ChannelInfo> findChannelRecordPage(HashMap<String, Object> params);


}
