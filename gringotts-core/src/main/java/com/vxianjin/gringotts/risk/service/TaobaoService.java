package com.vxianjin.gringotts.risk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.ITaobaoCertificationApiService;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 淘宝认证
 * Created by wukun on 2018/3/3
 */
@Service
public class TaobaoService implements ITaobaoService {
    private static final Logger logger = LoggerFactory.getLogger(TaobaoService.class);

    @Override
    public ResponseContent getToken(Map<String, String> params) {
        logger.info("TaobaoService getToken start");
        logger.info("TaobaoService getToken params=" + JSON.toJSONString(params));

//        ResponseContent result = new ResponseContent("400", "授权失败");

        String appId = PropertiesConfigUtil.get("TAOBAO_APPID");
        String appSecret = PropertiesConfigUtil.get("TAOBAO_APPSECRET");
        String sequenceNo = params.get("sequenceNo");
        String authItem = "ecommerce";
        String userId = params.get("userId");
        String realName = params.get("realName");
        String idNumber = params.get("idNumber");
        String phone = params.get("phone");
        String timestamp = ((int) (System.currentTimeMillis() / 1000)) + "";
        String requestUrl = PropertiesConfigUtil.get("TAOBAO_TOKEN_URL");

        //构造请求数据
        JSONObject resParams = new JSONObject();
        resParams.put("appId", appId);
        resParams.put("sequenceNo", sequenceNo);
        resParams.put("authItem", authItem);
        resParams.put("timestamp", timestamp);
        resParams.put("name", realName);
        resParams.put("idcard", idNumber);
        resParams.put("phone", phone);

        //获取签名
        String sign = getSign(userId, appId, appSecret, authItem, timestamp, sequenceNo);
        if (null == sign) {
            return new ResponseContent("400", "sign生成失败");
        }
        resParams.put("sign", sign);

        logger.info("TaobaoService getToken userId=" + userId + " resParams=" + resParams.toJSONString());

        String responseJson = null;

        //发送请求
        try {

            ITaobaoCertificationApiService service = CloseableOkHttp.obtainRemoteService(
                    "https://prod.gxb.io/", ITaobaoCertificationApiService.class
            );
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"), resParams.toJSONString()
            );
            Response<JSONObject> response = service.getAuthToken(body).execute();
            if (response.isSuccessful() && response.body() != null) {
                responseJson = response.body().toJSONString();
            }
            logger.info("TaobaoService getToken userId=" + userId + " responseJson=" + responseJson);
        } catch (Exception e) {
            logger.error("TaobaoService getToken userId=" + userId + " error:", e);
        }

        if (null == responseJson || "".equals(responseJson)) {
            return new ResponseContent("400", "系统异常，请稍后重试");
        }
        //解析返回结果
        JSONObject responseObj = JSONObject.parseObject(responseJson);
        if (null == responseObj) {
            return new ResponseContent("400", "数据解析失败");
        }
        //请求成功
        if ("1".equals(responseObj.get("retCode").toString())) {
            String token = JSONObject.parseObject(responseObj.getString("data")).getString("token");
            return new ResponseContent("200", token);
        } else {//请求失败
            return new ResponseContent("400", responseObj.get("retMsg").toString());
        }
    }

    @Override
    public String getUrl(Map<String, String> params) {
        logger.info("TaobaoService getUrl start");
        logger.info("TaobaoService getUrl params=" + JSON.toJSONString(params));

        String userId = params.get("userId");
        String token = params.get("token");
        String clientType = params.get("clientType");
        String deviceId = params.get("deviceId");
        String mobilePhone = params.get("mobilePhone");
        String returnUrl = PropertiesConfigUtil.get("TAOBAO_RETURN_URL") + "?userId=" + userId + "&clientType=" + clientType + "&deviceId=" + deviceId + "&mobilePhone=" + mobilePhone + "&record=3";
        String requestUrl = PropertiesConfigUtil.get("TAOBAO_REQUEST_URL");
        String jumpUrl = null;
        try {
            jumpUrl = requestUrl + "?returnUrl=" + URLEncoder.encode(returnUrl, "UTF-8")
                    + "&token=" + token
                    + "&title=淘宝授权"
                    + "&style=mobile"
                    + "&subItem=taobao"
                    + "&noProcess=true";
        } catch (UnsupportedEncodingException e) {
            logger.error("TaobaoService getUrl userId=" + userId + " error:", e);
        }

        if (StringUtils.isNotEmpty(jumpUrl)) {
            return jumpUrl;
        }

        return null;
    }


    private String getSign(String userId, String appId, String appSecret, String authItem, String timestamp, String sequenceNo) {
        String signStr = null;
        try {
            signStr = DigestUtils.md5Hex(String.format("%s%s%s%s%s", appId, appSecret, authItem, timestamp, sequenceNo));
        } catch (Exception e) {
            logger.error("TaobaoService getSign userId=" + userId + " error=", e);
        }

        return signStr;
    }
}
