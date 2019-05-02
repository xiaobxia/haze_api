package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.util.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class HelloController extends BaseController {
    @RequestMapping("hello")
    public void hello(HttpServletRequest request,
                      HttpServletResponse response, Model model) {
        HashMap<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "200");
        resMap.put("message", "成功");
        JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
    }
}
