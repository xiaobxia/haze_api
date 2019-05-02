package com.vxianjin.gringotts.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jintian
 * @date 18:37
 */
public class HttpUtilTest {

    public static String httpPost(String url, Map<String, Object> paramsMap) {
        HttpClient httpClient = new DefaultHttpClient();
        String strResult = "";
        HttpPost httpPost = new HttpPost(url);
        try{
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramsMap != null) {
                for (String key : paramsMap.keySet()) {
                    nvps.add(new BasicNameValuePair(key, JSON.toJSONString(paramsMap.get(key))));
                }
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int code = httpResponse.getStatusLine().getStatusCode();

            if (code == 200) {    //请求成功
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            httpPost.releaseConnection();
        }
        return strResult;
    }

    public String httpGet() {
        return "";
    }
}
