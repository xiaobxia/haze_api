package com.vxianjin.gringotts.api;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.service.IZz253ApiService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
public class Zz253ApiServiceTest extends AbstractApiTest {


    @Test
    public void msgHttpBatchSendSM() throws IOException {

        IZz253ApiService service = CloseableOkHttp.obtainRemoteService(
                "http://zapi.253.com/", IZz253ApiService.class
        );

        Map<String, String> map = new HashMap<>();
        map.put("account", "V0712335");
        map.put("pswd", "xKNw2XW0pv5ff6");
        map.put("mobile", "18668214775");
        map.put("msg", "测试创蓝短信语音消息通知");
        map.put("needstatus", "true");

        Response<ResponseBody> response = service.msgHttpBatchSendSM(map).execute();
        if (response.isSuccessful() && response.body() != null) {
            ResponseBody body = response.body();
            String string = body.string();
            String[] split = string.split("\n");
            String[] split1 = split[0].split(",");
            if (split1.length >= 2) {
                if (split1[1].equals("0")) {
                    System.out.println("1");
                } else {
                    System.out.println("0");
                }
            }
        }
    }

    @Test
    public void msgSendJSON() throws IOException {


        String account = "V0712335";
        String pswd = "xKNw2XW0pv5ff6";
        String smsSign = "【有零花】";
        String msg = smsSign + "内容内容内容";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", account);
        jsonObject.put("password", pswd);
        jsonObject.put("msg", msg);
        jsonObject.put("phone", "18668214775");

        IZz253ApiService service = CloseableOkHttp.obtainRemoteService(
                "http://zapi.253.com/", IZz253ApiService.class
        );
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toJSONString());
        Call<JSONObject> call = service.msgSendJSON(body);
        Response<JSONObject> response = call.execute();
        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(response.body());
    }
}
