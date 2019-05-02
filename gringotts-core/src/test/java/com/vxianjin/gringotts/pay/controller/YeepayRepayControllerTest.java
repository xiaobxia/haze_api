package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.util.HttpUtilTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jintian
 * @date 18:36
 */
public class YeepayRepayControllerTest {

    @Test
    public void payWithholdCallback(){

    }

    @Test
    public void payRenewalWithholdCallback(){

    }

    @Test
    public void repaymentWithholdRequest(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("borrowId","337259");
        params.put("bankId","");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/repayWithholdRequest",params);
        System.out.println(result);
    }

    @Test
    public void repaymentWithholdConfirm(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");
        params.put("smsCode","ceshi");
        params.put("requestNo","1415323149635033");
        params.put("payPwd","123456");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/repayWithholdConfirm",params);
        System.out.println(result);
    }

    @Test
    public void repaymentWithholdResendSmscode(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId","186770");
        params.put("request_no","18677020180723112545360490");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/repayWithholdSmscode",params);
        System.out.println(result);
    }

    @Test
    public void repaymentWithhold(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");
        params.put("payPwd","123456");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/repay-withhold",params);
        System.out.println(result);
    }

    @Test
    public void renewalWithhold(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");
        params.put("payPwd","123456");

        params.put("money","18100");
        params.put("bankId","443599");
        params.put("sgd","");


        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/renewal-withhold",params);
        System.out.println(result);
    }

    @Test
    public void autoWithhold(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/auto-withhold",params);
        System.out.println(result);
    }

    @Test
    public void queryWithhold(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");
        params.put("orderNo","18677020180723103823738699");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/query-withhold",params);
        System.out.println(result);
    }

    @Test
    public void queryRenewalWithhold(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id","337259");
        params.put("orderNo","18677020180723112545360490");

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/query-renewalWithhold",params);
        System.out.println(result);
    }

    @Test
    public void collectionWithhold(){

        String result = HttpUtilTest.httpPost("http://localhost:8080/yeepay/collection-withhold/186770/82242/18100/sds/sds",null);
        System.out.println(result);
    }


}