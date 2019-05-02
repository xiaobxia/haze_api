package com.vxianjin.gringotts.web.service;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.pay.service.MqInfoService;
import com.vxianjin.gringotts.web.AbstractServiceTest;
import com.vxianjin.gringotts.web.service.impl.AutoRiskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class AutoRiskServiceTest extends AbstractServiceTest {

    @Autowired
    private AutoRiskService autoRiskService;
    @Value("#{mqSettings['cs_topic']}")
    private String csTopic;

    @Value("#{mqSettings['cs_repay_target']}")
    private String aiTarget;

    @Resource
    private MqInfoService mqInfoService;
    @Test
    public void dealRemoteCreditReport() {
//        autoRiskService.dealRemoteCreditReport(773793);
        Map<String,String> map = new HashMap<>();
        map.put("phone","17301706972");
        map.put("name","zed");
        mqInfoService.sendMq(csTopic,aiTarget, "123");
    }

}