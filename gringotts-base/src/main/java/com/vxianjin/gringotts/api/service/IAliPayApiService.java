package com.vxianjin.gringotts.api.service;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public interface IAliPayApiService {
    /**
     * 订单查询
     */
    @POST("/api/queryOrder")
    @FormUrlEncoded
    Call<JSONObject> queryOrder(@FieldMap Map<String, String> map);
}
