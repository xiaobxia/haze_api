package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.util.HttpUtilTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author jintian
 * @date 18:36
 */
public class YeepayWithdrawControllerTest {

    @Test
    public void payWithdrawCallback(){
       /* Map<String,Object> params = new HashMap<String,Object>();
        params.put("orderId","112015323171164452");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/withdrawCallback",params);
        System.out.println(result);*/
    }

    @Test
    public void payWithdraw(){
        Map<String,Object> params = new HashMap<String,Object>();

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/withdraw/186770/337259/sds/sds",params);
        System.out.println(result);
    }

}