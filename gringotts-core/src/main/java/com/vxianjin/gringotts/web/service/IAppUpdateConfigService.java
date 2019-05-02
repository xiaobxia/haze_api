package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.AppUpdateConfig;


/**
 * Created by giozola on 2018/10/12.
 */
public interface IAppUpdateConfigService {


    /**
     * 查找要更新的版本信息
     * 返回为空，则当前为最新版
     * @param appType appType
     * @param appVersion appVersion
     * @return AppUpdateConfig
     */
    AppUpdateConfig findUpdateVersionInfo(String appType,String appVersion);
}
