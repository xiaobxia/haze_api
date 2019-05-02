package com.vxianjin.gringotts.api;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.service.IShuJuMoHeApiService;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public class ShuJuMoHeApiServiceTest extends AbstractApiTest {

    @Test
    public void taskUnifyQuery() throws IOException {
        IShuJuMoHeApiService service = CloseableOkHttp.obtainRemoteService(
                "https://api.shujumohe.com/", IShuJuMoHeApiService.class);
        Call<JSONObject> call = service.taskUnifyQuery(
                "jiexiang_mohe",
                "9a30d6e3f69e433694630d374e07a5d7",
                "TASKYYS100000201711021047400741403178"
        );
        Response<JSONObject> response = call.execute();

        System.out.println(response.isSuccessful());
        System.out.println(response.message());
        System.out.println(response.code());
        System.out.println(response.body());
    }
}
