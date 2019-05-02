package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.util.VersionUtil;
import com.vxianjin.gringotts.web.dao.IAppUpdateConfigDao;
import com.vxianjin.gringotts.web.pojo.AppUpdateConfig;
import com.vxianjin.gringotts.web.service.IAppUpdateConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by giozola on 2018/10/12.
 */
@Service
public class AppUpdateConfigService implements IAppUpdateConfigService {
    @Resource
    private IAppUpdateConfigDao appUpdateConfigDao;

    @Override
    public AppUpdateConfig findUpdateVersionInfo(String appType,String appVersion) {
        if (appVersion == null || "".equals(appVersion)){
            throw new RuntimeException("appVersion 不能为空!");
        }
        if (appType == null || "".equals(appType)){
            throw new RuntimeException("app_type 不能为空!");
        }
        List<AppUpdateConfig> appUpdateConfigList = appUpdateConfigDao.findList(appType);
        if (appUpdateConfigList == null || appUpdateConfigList.isEmpty()){
            throw new RuntimeException("没有查询到对应手机型号的信息 app_type:"+appType);
        }
        //最新版本信息
        AppUpdateConfig lasterVersion =  appUpdateConfigList.get(0);

        if (VersionUtil.versionCompare(lasterVersion.getAppVersion(),appVersion) == 1){
            //传参版本不是最新版本
            for (AppUpdateConfig appUpdateConfig: appUpdateConfigList) {
                String version = appUpdateConfig.getAppVersion();
                // 查询大于传参版本的版本信息
                if (VersionUtil.versionCompare(version,appVersion) == 1){
                    //若其中有强制更新，强制更新最新版本，
                    if(appUpdateConfig.getForceType() == 1){
                        lasterVersion.setForceType(1);
                    }
                }
            }
            return lasterVersion;
        }else{
            //版本是最新版本
            return null;
        }
    }

}
