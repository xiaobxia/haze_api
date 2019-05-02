package com.vxianjin.gringotts.api.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public interface IYeePayApiService {

    @POST("/app-merchant-proxy/groupTransferController.action")
    Call<ResponseBody> groupTransfer(@Body RequestBody body);
}
