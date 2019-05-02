package com.vxianjin.gringotts.pay.service.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author jintian
 * @date 2018/9/12 14:15
 */
@Component
public class MapiConnectionService {

    private Logger logger = LoggerFactory.getLogger(MapiConnectionService.class);

    @Value("#{mapiSettings['mapi.url']}")
    private String url;

    @Value("#{mapiSettings['mapi.version']}")
    private String version;

    @Value("#{mapiSettings['mapi.appId']}")
    private String appId;

    @Value("#{mapiSettings['mapi.token.key']}")
    private String tokenKey;

    private static String RULE_ENGINE = "rule_engine";

    public String requestMapi(Map params, String reqSystem, String apiName) {
        logger.info("prepare req mapi ,reqSystem :" + reqSystem + " apiName:" + apiName);
        JSONObject reqJson = new JSONObject();

        reqJson.put("_api", apiName);
        reqJson.put("_v", version);
        reqJson.put("_sg", reqSystem);

        reqJson.put("_sy", "2BA740F7-292C-495B-AC2C-BA415A5B6839||fe80::10c5:28d4:754d:9e4c||750*1334||iPhone");
        reqJson.put("_t", System.currentTimeMillis());
        reqJson.put("_appId", appId);

        // 将请求参数放进去
        reqJson.putAll(params);

        String at = TokenUtil.token(TokenUtil.getParameterMap(reqJson), tokenKey);
        reqJson.put("_at", at);

        logger.info("mapi reqUrl:" + url + " reqParams:" + JSON.toJSONString(reqJson));
        String responseString = HttpUtil.post(url, reqJson);
        logger.info("mapi response:" + responseString);
        if (responseString == null) {
            logger.info("req mapi has error,reqSystem :" + reqSystem + " apiName:" + apiName);
            return "";
        }
        JSONObject respJson = JSON.parseObject(responseString);

        if (!"true".equals(respJson.getString("success"))) {
            logger.info("req mapi fail,reqSystem :" + reqSystem + " apiName:" + apiName + " msgCode" + reqJson.getString("msgCode"));
            return "";
        }
        logger.info("end req mapi ,reqSystem :" + reqSystem + " apiName:" + apiName);
        return respJson.getString("model");
    }


    public String requestRoleEngine(Map params, String apiName) {
        return requestMapi(params, RULE_ENGINE, apiName);
    }
}
