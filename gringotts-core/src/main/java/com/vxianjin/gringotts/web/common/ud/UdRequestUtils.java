package com.vxianjin.gringotts.web.common.ud;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

public class UdRequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(UdRequestUtils.class);

    public static String dataservice(JSONObject jsonObject) throws Exception {
        String signature = getMd5(jsonObject, PropertiesConfigUtil.get("UD_SECURITY_KEY"));
        String url = String.format(PropertiesConfigUtil.get("UD_DATASERVICE_URL"), PropertiesConfigUtil.get("UD_PUB_KEY"), "Y1001005",
                DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"), signature);
        logger.info("url地址为：" + url);
        String response = doHttpRequest(url, jsonObject);
        return response;
    }

    /**
     * Http请求
     */
    public static String doHttpRequest(String url, JSONObject reqJson) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        //设置传入参数
        StringEntity entity = new StringEntity(reqJson.toJSONString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        logger.info(url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            String respContent = EntityUtils.toString(he, "UTF-8");
            //return (JSONObject) JSONObject.parse(respContent);
            return respContent;
        }
        return null;
    }

    public static JSONObject getRequestHeader(String session_id) throws IOException {
        JSONObject header = new JSONObject();
        if (StringUtils.isNotBlank(session_id)) {
            header.put("session_id", session_id);
        }
        String sign_time = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
        String partner_order_id = UUID.randomUUID().toString();
        String sign = getMD5Sign(PropertiesConfigUtil.get("UD_PUB_KEY"), partner_order_id, sign_time, PropertiesConfigUtil.get("UD_SECURITY_KEY"));
        header.put("partner_order_id", partner_order_id);
        header.put("sign", sign);
        header.put("sign_time", sign_time);
        return header;
    }

    /**
     * 生成md5签名
     */
    public static String getMD5Sign(String pub_key, String partner_order_id, String sign_time, String security_key) throws UnsupportedEncodingException {
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        logger.info("认证比对阶段输入签名signField：" + signStr);
        return MD5Utils.MD5Encrpytion(signStr.getBytes("UTF-8"));
    }

    /**
     * 加签
     * @param jsonObject
     * @param secretkey
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getMd5(JSONObject jsonObject, String secretkey) throws UnsupportedEncodingException {
        String sign = String.format("%s|%s", jsonObject, secretkey);
        logger.info("获取用户画像数据阶段输入签名：" + sign);
        return MD5Utils.MD5Encrpytion(sign.getBytes("UTF-8"));
    }

}
