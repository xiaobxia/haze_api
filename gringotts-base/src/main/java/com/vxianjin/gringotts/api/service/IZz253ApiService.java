package com.vxianjin.gringotts.api.service;

import com.alibaba.fastjson.JSONObject;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public interface IZz253ApiService {

    @POST("/msg/HttpBatchSendSM")
    @FormUrlEncoded
    Call<ResponseBody> msgHttpBatchSendSM(@FieldMap Map<String, String> map);

    @POST("http://smssh1.253.com/msg/send/json")
    Call<JSONObject> msgSendJSON(@Body RequestBody body);
}
