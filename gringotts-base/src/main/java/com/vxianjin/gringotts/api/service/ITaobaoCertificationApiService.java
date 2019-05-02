package com.vxianjin.gringotts.api.service;

import com.alibaba.fastjson.JSONObject;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public interface ITaobaoCertificationApiService {

    @POST("/crawler/auth/v2/get_auth_token")
    Call<JSONObject> getAuthToken(@Body RequestBody body);
}
