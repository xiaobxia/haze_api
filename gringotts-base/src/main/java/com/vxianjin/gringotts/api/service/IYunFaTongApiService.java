package com.vxianjin.gringotts.api.service;


import com.alibaba.fastjson.JSONObject;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public interface IYunFaTongApiService {

    @GET("/api/contract/status")
    Call<JSONObject> contractStatus(@Query("id") String contractId);

    @POST("/api/contract/token")
    @FormUrlEncoded
    Call<JSONObject> contractToken(@HeaderMap Map<String, String> headers, @Field("contractId") String contractId, @Field("userId") String userId);

    @POST("/api/contract/check")
    @FormUrlEncoded
    Call<JSONObject> contractCheck(@HeaderMap Map<String, String> headers, @Field("contractId") String contractId, @Field("userId") String userId);

    @POST("/api/file/upload")
    @FormUrlEncoded
    Call<JSONObject> fileUpload(@HeaderMap Map<String, String> headers, @Field("file") String file, @Field("type") String type);

    @POST("/api/customer/add")
    @FormUrlEncoded
    Call<JSONObject> customerAdd(@HeaderMap Map<String, String> headers, @Body RequestBody body);

    @POST("/api/contract/create")
    @FormUrlEncoded
    Call<JSONObject> contractCreate(@HeaderMap Map<String, String> headers, @Body RequestBody body);
}
