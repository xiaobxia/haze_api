package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.util.HttpUtilTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giozola on 2018/10/12.
 */
public class VersionsManagersControllerTest {
    @Test
    public void payAlipayRenewalRequest(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("clientType","ios");
        params.put("appVersion","1.0.0");
        String result = HttpUtilTest.httpPost("http://localhost:8080/versionsManagers/appVersionCheck",params);
        System.out.println(result);
    }
}
