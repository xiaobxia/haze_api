package com.vxianjin.gringotts.api;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.service.IAliPayApiService;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public class AliPayApiServiceTest extends AbstractApiTest {

    @Test
    public void test() throws IOException {

        Map<String, String> paraMap = new TreeMap<String, String>();
        paraMap.put("merchantOutOrderNo", "123456789");
        paraMap.put("merid", "yft2018030600002");
        paraMap.put("noncestr", "JXAPPQUERY");
        String sign = MD5Util.md5(HttpUtil.formatMap(paraMap, false) + "key=" + "DY7fVTeB6FpDA31Y2cK4GBRhklN12ebi");
        paraMap.put("sign", sign);

        IAliPayApiService service = CloseableOkHttp.obtainRemoteService(
                "http://jh.chinambpc.com/", IAliPayApiService.class
        );
        Response<JSONObject> response = service.queryOrder(paraMap).execute();
        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(response.body());
    }
}
