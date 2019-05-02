package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.pojo.Advertisement;
import com.vxianjin.gringotts.web.service.impl.AdvertisementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ad/")
public class AdvertisementController {

    private static Logger loger = Logger.getLogger(AdvertisementController.class);

    @Autowired
    private AdvertisementService adService;

    @RequestMapping("getAd")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        String code = "0";
        String msg = "success";
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("status", "0");
            List<Advertisement> all = adService.findOne(params);
            loger.info("ad advertisement size=" + (all == null ? "null" : all.size()));
            if (all == null || all.isEmpty()) {
                code = "-1";
                msg = "fail";
            } else {
                Advertisement ad = all.get(0);
                data.put("tcImage", ad.getUrl());
                data.put("tcUrl", ad.getReurl());
                data.put("tcStatus", ad.getStatus());
                if (request.getParameter("clientType").equals("android")) {
                    data.put("type", "0");
                } else {
                    data.put("type", "-1");
                }
            }
        } catch (Exception e) {
            loger.error("ad fial error:", e);
        }
        map.put("code", code);
        map.put("message", msg);
        map.put("data", data);
        JSONUtil.toObjectJson(response, JSON.toJSONString(map));
    }

}
