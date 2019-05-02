package com.vxianjin.gringotts.pay.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.util.YeepayApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * 易宝查询订单状态
 *
 * @author zed
 * @since 2019-01-25 5:39 PM
 */
@RequestMapping("yeepay")
@Controller
public class YeepayPayQueryController {
    private static final Logger log = LoggerFactory.getLogger(YeepayPayQueryController.class);

    @PostMapping("queryPayRecord")
    public String queryPayRecordStatus(String merchantNo,String requestNo ){
        Map<String,String> map = new HashMap<>();
        map.put("merchantno",merchantNo);
        map.put("requestno",requestNo);
        Map<String,Object> resultMap = new HashMap<>();
        try{
            resultMap  = YeepayApiUtil.yeepayYOP(map, PayConstants.REPAY_RECORD_QUERY_REQURL);

        }catch (Exception e){
            log.error("queryPayRecordStatus error:{}",e);
        }
        return JSON.toJSONString(resultMap);
    }

//    public static void main(String[] args) {
//        Map<String,String> map = new HashMap<>();
//        map.put("merchantno","10000469946");
//        map.put("requestno","1015484124739768");
//        Map<String,Object> resultMap = new HashMap<>();
//        try{
//            resultMap  = YeepayApiUtil.yeepayYOP(map, PayConstants.REPAY_RECORD_QUERY_REQURL);
//
//        }catch (Exception e){
//            log.error("queryPayRecordStatus error:{}",e);
//        }
//    }
}

