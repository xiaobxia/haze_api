package com.vxianjin.gringotts.api;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.service.ITaobaoCertificationApiService;
import com.vxianjin.gringotts.util.GenerateNo;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public class TaobaoCertificationApiServiceTest extends AbstractApiTest {

    private String getSign(String userId, String appId, String appSecret, String authItem, String timestamp, String sequenceNo) {
        String signStr = null;
        try {
            signStr = DigestUtils.md5Hex(String.format("%s%s%s%s%s", appId, appSecret, authItem, timestamp, sequenceNo));
        } catch (Exception e) {

        }
        return signStr;
    }

    @Test
    public void getAuthToken() throws IOException {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String appSecret = "2eb2f28cccc84ade9d55a6b58bc9639c";
        String appId = "gxbe52b43dda1abea44";
        String authItem = "ecommerce";
        String sequenceNo = "jxmoney" + GenerateNo.nextOrdId();
        String userId = "309";
        //构造请求数据
        JSONObject resParams = new JSONObject();
        resParams.put("appId", appId);
        resParams.put("sequenceNo", sequenceNo);
        resParams.put("authItem", authItem);
        resParams.put("timestamp", timestamp);
        resParams.put("name", "陈俊棋");
        resParams.put("idcard", "321282199206113230");
        resParams.put("phone", "18668247775");
        resParams.put("sign", getSign(userId, appId, appSecret, authItem, timestamp, sequenceNo));

        ITaobaoCertificationApiService service = CloseableOkHttp.obtainRemoteService(
                "https://prod.gxb.io/", ITaobaoCertificationApiService.class
        );
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), resParams.toJSONString()
        );
        Response<JSONObject> response = service.getAuthToken(body).execute();

        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(response.body());
        System.out.println(response.errorBody());
    }
}
