package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IChannelInfoDao;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.pojo.ChannelInfo;
import com.vxianjin.gringotts.web.service.IChannelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelInfoService implements IChannelInfoService {

    @Autowired
    private IChannelInfoDao channelInfoDao;

    @Autowired
    private IPaginationDao paginationDao;

    @Override
    public List<ChannelInfo> findAll(Map<String, Object> params) {
        return channelInfoDao.findAll(params);
    }

    public List<String> findAllChUser(Map<String, Object> params) {
        return channelInfoDao.findAllChUser(params);
    }

    @Override
    public ChannelInfo findOneChannelInfo(HashMap<String, Object> params) {
        List<ChannelInfo> list = this.findAll(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public ChannelInfo findById(Integer id) {
        return channelInfoDao.findById(id);
    }

    @Override
    public void insert(ChannelInfo channelInfo) {

        channelInfoDao.insert(channelInfo);
    }

    @Override
    public void insertChannelUserInfo(Map<String, Object> param) {
        channelInfoDao.insertChannelUserInfo(param);
    }

    @Override
    public void updateById(ChannelInfo channelInfo) {
        channelInfoDao.updateById(channelInfo);
    }

    @Override
    public void deleteChannelInfoById(Integer id) {
        channelInfoDao.deleteById(id);
    }

    @Override
    public PageConfig<ChannelInfo> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "ChannelInfo");
        PageConfig<ChannelInfo> pageConfig = new PageConfig<ChannelInfo>();
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, "web");
        return pageConfig;

    }

    @Override
    public PageConfig<ChannelInfo> findChannelUserPage(
            HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "ChannelInfo");
        PageConfig<ChannelInfo> pageConfig = new PageConfig<ChannelInfo>();
        pageConfig = paginationDao.findPage("findUserAll", "findUserAllCount", params, "web");
        return pageConfig;
    }

    @Override
    public PageConfig<ChannelInfo> findChannelRecordPage(
            HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "ChannelInfo");
        PageConfig<ChannelInfo> pageConfig = new PageConfig<ChannelInfo>();
        pageConfig = paginationDao.findPage("findRecordAll", "findRecordAllCount", params, "web");
        return pageConfig;
    }


    @Override
    public Integer findChannelIdByCode(String channelCode) {
        return channelInfoDao.findChannelIdByCode(channelCode);
    }

    @Override
    public Integer findUserIdByChannelId(Integer channelId) {
        return channelInfoDao.findUserIdByChannelId(channelId);
    }
}
