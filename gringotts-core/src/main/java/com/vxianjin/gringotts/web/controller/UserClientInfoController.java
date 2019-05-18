package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.IUserClientInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jintian
 * @date 17:52
 */
@Controller
@RequestMapping("userClient")
public class UserClientInfoController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserClientInfoController.class);

    @Resource
    private IUserClientInfoService service;

    @ResponseBody
    @RequestMapping("saveUserClientInfo")
    public ResponseContent saveUserClientInfo(HttpServletRequest request, String clientId) {
        try {
            User user = loginFrontUserByDeiceId(request);
            logger.info("request clientId :" + clientId + " userId:" + (null == user ? "null" : user.getId()));
            if (user == null) {
                return new ResponseContent("-101", "登录用户未找到,请重新登录");
            }
            String userId = user.getId();
            service.saveUserClientInfo(Integer.valueOf(userId), clientId);
            return new ResponseContent("0", "保存成功");
        } catch (Exception e) {
            logger.error("UserClientInfoController.saveUserClientInfo has error,", e);
            return new ResponseContent("-101", "系统异常");
        }
    }


    /**
     * 回调页面
     */
    @RequestMapping("operatorAuthCallback")
    public String operatorAuthCallback(HttpServletRequest request, Model model) {
        String success = request.getParameter("success");
        String remark = request.getParameter("remark");
        model.addAttribute("success", StringUtils.isNotBlank(success) ? Integer.parseInt(success) : 0);
        model.addAttribute("remark", remark);
        return "userinfo/operatorAuthCallback";
    }
}