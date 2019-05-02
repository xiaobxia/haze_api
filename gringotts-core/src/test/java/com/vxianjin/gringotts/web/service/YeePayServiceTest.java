package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.component.YeepayService;
import com.vxianjin.gringotts.web.AbstractServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public class YeePayServiceTest extends AbstractServiceTest {

    @Autowired
    YeepayService yeepayService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void getWithdrawRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("batchNo", "201201310000001");
        map.put("bankName", "中国光大银行");
        map.put("amount", "10000");
        map.put("cardName", "陈俊棋");
        map.put("cardNo", "4242424242424242");
        map.put("orderId", "20120131000000101");
        map.put("userId", "110");
        yeepayService.getWithdrawRequest(map);
    }
    @Test
    public void testSms(){
        System.out.println(PayConstants.SIANG_CERT);
//        String content = "1111";
//        String phone = "121312321";
//        PublishAdapter publishAdapter = PublishFactory.getPublishAdapter(EventTypeEnum.PAY.getCode());
//        publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(), content, phone);
    }
}
