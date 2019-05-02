package com.vxianjin.gringotts.api.service;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description: https://luosimao.com/service/sms
 * <p>
 * 标准的短信接口
 * 仅需少量开发，就快速接入您的日常业务
 */
public interface ILuoSiMaoApiService {

    /**
     * 人机验证接口
     *
     * @param map map
     *            api_key	网站校验api
     *            response	前端校验结果
     * @return {"error":0,"res":"success"}
     */
    @POST("/api/site_verify")
    @FormUrlEncoded
    Call<JSONObject> siteVerify(@FieldMap Map<String, String> map);
}
