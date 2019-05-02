package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.Content;
import com.vxianjin.gringotts.web.service.IContentService;
import com.vxianjin.gringotts.web.utils.SpringUtils;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/about")
public class AboutController extends BaseController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IContentService contentService;

    @RequestMapping(value = "/left")
    public String left(HttpServletRequest request,
                       HttpServletResponse response, Model model) {
        model.addAttribute("currentId", request.getParameter("currentId"));
        return "about_left";
    }

    @RequestMapping(value = "/company")
    public String company(HttpServletRequest request,
                          HttpServletResponse response, Model model) {
        return "about_company";
    }

    @RequestMapping(value = "/contactUs")
    public String comcontactUspany(HttpServletRequest request,
                                   HttpServletResponse response, Model model) {
        return "about_contactUs";
    }

    @RequestMapping(value = "/media")
    public String media(HttpServletRequest request,
                        HttpServletResponse response, Model model) {
        return "about_media";
    }

    @RequestMapping(value = "/notice")
    public String notice(HttpServletRequest request,
                         HttpServletResponse response, Model model) {
        return "about_notice";
    }

    @RequestMapping(value = "/question")
    public String question(HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        return "about_question";
    }

    @RequestMapping(value = "/fee")
    public String fee(HttpServletRequest request, HttpServletResponse response,
                      Model model) {
        model.addAttribute(MESSAGE, request.getParameter(MESSAGE));
        return "about_fee";
    }

    @RequestMapping(value = "gotoList/{type}")
    public String gotoList(HttpServletRequest request,
                           HttpServletResponse response, Model model,
                           @PathVariable("type") String type) {
        model.addAttribute("name", SysCacheUtils.getConfigParams(
                BackConfigParams.CHANNEL).get(type));
        model.addAttribute("type", type);
        return "about_list";
    }

    /**
     * 根据用户ID查询发布记录
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "findList/{type}", method = RequestMethod.POST)
    public void findList(HttpServletRequest request,
                         HttpServletResponse response, Model model,
                         @PathVariable("type") String type) {
        HashMap<String, Object> params = this.getParametersO(request);
        try {
            params.put("channelType", type);
            SpringUtils.renderJson(response, contentService.findPage(params));
        } catch (Exception e) {
            logger.error("findList error", e);
        }
    }

    @RequestMapping(value = "detail/{id}")
    public String gotoList(HttpServletRequest request,
                           HttpServletResponse response, Model model,
                           @PathVariable("id") Integer id) {
        Content content = contentService.findById(id);
        model.addAttribute("type", content.getChannelType());
        model.addAttribute("content", content);
        return "about_detail";
    }
}
