package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.util.HttpUtilTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jintian
 * @date 18:35
 */
public class AlipayRepayControllerTest {

    @Test
    public void queryalipayRequest(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orderId","112015323171164452");

        String result = HttpUtilTest.httpPost("http://localhost:8080/alipay/alipay_query_req",params);
        System.out.println(result);
    }

    @Test
    public void payAlipayRenewalRequest(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");
        params.put("money","21100");

        String result = HttpUtilTest.httpPost("http://localhost:8080/alipay/alipay_renewal_req",params);
        System.out.println(result);
    }

    @Test
    public void payAlipayWithholdRequest(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");

        String result = HttpUtilTest.httpPost("http://localhost:8080/alipay/alipay_withhold_req",params);
        System.out.println(result);
    }

    @Test
    public void payAlipayCallback(){

    }
}