package com.vxianjin.gringotts.web.service;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.risk.service.TaobaoService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.web.AbstractServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public class TaobaoServiceTest extends AbstractServiceTest {

    @Autowired
    TaobaoService taobaoService;

    @Test
    public void getToken() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("realName", "陈俊棋");
        map.put("idNumber", "321282199206113230");
        map.put("phone", "18668247775");
        map.put("userId", "309");
        map.put("sequenceNo", "jxmoney" + GenerateNo.nextOrdId());
        ResponseContent content = taobaoService.getToken(map);
        System.out.println(JSONObject.toJSON(content));
    }
}
