package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.base.dao.BaseMapper;
import com.vxianjin.gringotts.base.service.impl.BaseServiceImpl;
import com.vxianjin.gringotts.web.dao.IAppDownloadConfigDao;
import com.vxianjin.gringotts.web.pojo.AppDownloadConfig;
import com.vxianjin.gringotts.web.service.IAppDownloadConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * APP分发配置ServiceImpl
 * 
 * @author fully
 * @version 1.0.0
 * @date 2019-05-29 18:06:36
 */
 
@Service
public class AppDownloadConfigService extends BaseServiceImpl<AppDownloadConfig, Integer> implements IAppDownloadConfigService {
	
    private static final Logger logger = LoggerFactory.getLogger(AppDownloadConfigService.class);

    @Resource
    private IAppDownloadConfigDao appDownloadConfigDao;

	@Override
	public BaseMapper<AppDownloadConfig, Integer> getMapper() {
		return appDownloadConfigDao;
	}

	@Override
	public AppDownloadConfig selectAppConfigSelective(Map<String, Object> paramMap) {
		return getMapper().findSelective(paramMap);
	}
}