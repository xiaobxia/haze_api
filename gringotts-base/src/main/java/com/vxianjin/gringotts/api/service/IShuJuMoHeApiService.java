package com.vxianjin.gringotts.api.service;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description: 数据魔盒
 */
public interface IShuJuMoHeApiService {


    /**
     * @param partnerCode 合作伙伴代码
     * @param partnerKey  合作伙伴秘钥
     * @param taskId      任务编号
     * @return JSONObject
     */
    @POST("/octopus/task.unify.query/v3")
    @FormUrlEncoded
    Call<JSONObject> taskUnifyQuery(@Field("partner_code") String partnerCode, @Field("partner_key") String partnerKey, @Field("task_id") String taskId);
}
