package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.ChannelReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface IChannelReportService {

    ChannelReport getChannelReportById(Integer id);

    /**
     * 分页
     */
    PageConfig<ChannelReport> findPage(HashMap<String, Object> params);

    /**
     * 分页
     */
    PageConfig<ChannelReport> findPrPage(HashMap<String, Object> params);

    List<ChannelReport> getAllChannelReports(Map<String, Object> params);

    List<ChannelReport> getAllPrChannelReports(Map<String, Object> params);

    /**
     * 统计每日数据
     *
     * @return
     */
    boolean saveChannelReport();


}
