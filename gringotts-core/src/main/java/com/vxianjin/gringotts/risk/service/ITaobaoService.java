package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.common.ResponseContent;

import java.util.Map;

/**
 * 淘宝认证
 * Created by wukun on 2018/3/3
 */
public interface ITaobaoService {

    /**
     * 获取token
     *
     * @param params
     * @return
     */
    ResponseContent getToken(Map<String, String> params);

    /**
     * 获取认证链接
     *
     * @param params
     * @return
     */
    String getUrl(Map<String, String> params);
}
