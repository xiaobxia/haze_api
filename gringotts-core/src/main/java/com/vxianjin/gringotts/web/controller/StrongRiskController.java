package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.pojo.risk.StrongRiskResult;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 描述:
 * 强风控回调url
 *
 * @author zed
 * @since 2019-02-11 10:22 PM
 */
@Controller
@RequestMapping("risk")
public class StrongRiskController {
    @Resource
    private IUserService userService;

    private static final Logger log = LoggerFactory.getLogger(StrongRiskController.class);
    @PostMapping("registerStrongRisk")
    public void strongRisk(HttpServletRequest request){
        String data = request.getParameter("data");
        log.info("data :{}",data);
        StrongRiskResult riskResult = new StrongRiskResult();
        JSONObject jsonObject = JSON.parseObject(data);
        if(jsonObject!=null){
            String consumerNo = jsonObject.getString("consumerNo");
            String userId = consumerNo.replace(PropertiesConfigUtil.get("RISK_BUSINESS"),"");
            riskResult.setUserId(userId);
            riskResult.setAmount(jsonObject.getString("amount"));
            riskResult.setOrderNo(jsonObject.getString("orderNo"));
            riskResult.setResult(jsonObject.getString("result"));
            riskResult.setRiskStatusType(jsonObject.getString("riskStatusType"));
            riskResult.setType(jsonObject.getString("type"));
            riskResult.setConsumerNo(consumerNo);
            userService.insertUserStrongRiskResult(riskResult);
        }
    }

}

