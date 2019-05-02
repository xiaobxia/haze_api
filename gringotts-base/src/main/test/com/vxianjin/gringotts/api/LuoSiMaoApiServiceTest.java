package com.vxianjin.gringotts.api;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.service.ILuoSiMaoApiService;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public class LuoSiMaoApiServiceTest extends AbstractApiTest {

    @Test
    public void siteVerify() {

        Map<String, String> map = new HashMap<>();
        map.put("api_key", "378bcbdea2762d6c1f50f9096e6a649e");
        map.put("response", "123456");

        ILuoSiMaoApiService service = CloseableOkHttp.obtainRemoteService(
                "https://captcha.luosimao.com/", ILuoSiMaoApiService.class
        );
        try {
            Call<JSONObject> call = service.siteVerify(map);
            Response<JSONObject> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jo = response.body();
                System.out.println("返回结果判定：" + "0".equals(jo.getString("error")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("判定结束");
    }
}
