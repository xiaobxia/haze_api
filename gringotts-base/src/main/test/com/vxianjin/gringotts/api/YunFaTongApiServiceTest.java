package com.vxianjin.gringotts.api;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.service.IYunFaTongApiService;
import com.vxianjin.gringotts.util.security.MD5Util;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public class YunFaTongApiServiceTest extends AbstractApiTest {
    public static String getSign(String utime, String json) {
        return MD5Util.MD5("2000018" + "2827cd7e07a4b48944c4c9c116f04dc8" + utime + json);
    }

    public static Map<String, String> getHeaderMap(String json) {
        String utime = String.valueOf(System.currentTimeMillis());
        String sign = getSign(utime, json);
        Map<String, String> map = new HashMap<String, String>();
        map.put("utime", utime);
        map.put("userCode", "2000018");
        map.put("sign", sign);
        return map;

    }

    @Test
    public void contractStatus() throws IOException {
        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(
                "http://yqdc.yunfatong.com/", IYunFaTongApiService.class);
        Response<JSONObject> response = service.contractStatus("1").execute();
        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(response.body());
        System.out.println(response.errorBody());
    }

    @Test
    public void contractToken() throws IOException {

        JSONObject json = new JSONObject();
        json.put("contractId", "1");
        json.put("userId", "1");

        Map<String, String> headerMap = getHeaderMap(json.toJSONString());

        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(
                "http://yqdc.yunfatong.com/", IYunFaTongApiService.class);
        Response<JSONObject> response = service.contractToken(headerMap, "1", "1").execute();
        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(response.body());
    }
}
