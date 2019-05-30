package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.base.service.BaseService;
import com.vxianjin.gringotts.web.pojo.AppDownloadConfig;

import java.util.Map;

/**
 * APP分发配置Service
 * 
 * @author fully
 * @version 1.0.0
 * @date 2019-05-29 18:06:36
 */
public interface IAppDownloadConfigService extends BaseService<AppDownloadConfig, Integer> {

    AppDownloadConfig selectAppConfigSelective(Map<String, Object> paramMap);

}
