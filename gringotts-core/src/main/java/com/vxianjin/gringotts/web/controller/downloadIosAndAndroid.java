package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.common.ResponseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于首页二维码跳转对应下载地址
 *
 * @param
 * @author 2017年1月4日 17:29:03
 */
@Controller
public class downloadIosAndAndroid extends BaseController {
    Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/download/iosAndAndroid")
    public String callBack(HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        Integer userId = null;

        return "userinfo/downloadIosAndAndroid";
    }


}
