package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.Content;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IContentService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContentController extends BaseController {
    @Autowired
    public IContentService contentService;
    /*@RequestMapping("help")
    public String toHelp(HttpServletRequest request,HttpServletResponse response){
		return "content/help";
	}*/

    /**
     * 帮助中心
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("help")
    public String toHelp(HttpServletRequest request, HttpServletResponse response, Model model) {
        User logUser = this.loginFrontUserByDeiceId(request);
        try {
            if (logUser != null) {
                Map<String, String> intervalMap = SysCacheUtils.getConfigMap(BackConfigParams.WEBSITE);
                model.addAttribute("tel", intervalMap.get("service_phone"));// 客服电话
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "content/help";
    }

    @RequestMapping("content/activity")
    public String toactivity(HttpServletRequest request, HttpServletResponse response) {
        return "content/activity";
    }

    @RequestMapping("content/Olduser")
    public String Olduser(HttpServletRequest request, HttpServletResponse response) {
        return "content/Olduser";
    }

    /**
     * 查询帮助问题列表
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("help/findTheHotIssueList")
    public void findTheHotIssueList(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> json = new HashMap<String, Object>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        try {
            HashMap<String, Object> params = this.getParametersO(request);
            params.put("channelType", "CHANNEL_QUESTION");
            PageConfig<Content> pageConfig = contentService.findBackPage(params);
            List<Content> list = pageConfig.getItems();
            List<Map<String, Object>> resultList = new ArrayList();
            if (list != null && list.size() > 0) {
                for (Content con : list) {
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("id", con.getId());//公告编号
                    resultMap.put("channelTitle", con.getContentTitle());//标题
                    resultMap.put("contentTxt", con.getContentTxt());//内容
                    resultList.add(resultMap);
                }
                Map<String, String> pageInfo = new HashMap<String, String>();
                pageInfo.put(Constant.CURRENT_PAGE, pageConfig.getCurrentPage() + "");
                pageInfo.put("totalPage", pageConfig.getTotalPages() + "");
                pageInfo.put(Constant.PAGE_SIZE, pageConfig.getPageSize() + "");
                json.put("pageInfo", pageInfo);
                json.put("list", resultList);
                code = ResponseStatus.SUCCESS.getName();
                msg = ResponseStatus.SUCCESS.getValue();
            } else {
                msg = "暂时没有数据";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }

    /**
     * 查询活动列表
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("help/findactivity")
    public void findactivity(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> json = new HashMap<String, Object>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        try {
            HashMap<String, Object> params = this.getParametersO(request);
            params.put("channelType", "CHANNEL_NOTICE");
            PageConfig<Content> pageConfig = contentService.findBackPage(params);
            List<Content> list = pageConfig.getItems();
            List<Map<String, Object>> resultList = new ArrayList();
            if (list != null && list.size() > 0) {
                for (Content con : list) {
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("id", con.getId());//公告编号
                    resultMap.put("channelTitle", con.getContentTitle());//标题
                    resultMap.put("contentSummary", con.getContentSummary());//标题
                    resultMap.put("url", con.getExternalUrl());
                    resultMap.put("createTime", DateUtil.getDateFormat(con.getAddTime(), "yy-MM-dd hh:mm:ss"));
                    resultMap.put("columnName", SysCacheUtils.getConfigParams(BackConfigParams.CHANNEL).get(con.getChannelType()));
                    resultList.add(resultMap);
                }
                Map<String, String> pageInfo = new HashMap<String, String>();
                pageInfo.put(Constant.CURRENT_PAGE, pageConfig.getCurrentPage() + "");
                pageInfo.put("totalPage", pageConfig.getTotalPages() + "");
                pageInfo.put(Constant.PAGE_SIZE, pageConfig.getPageSize() + "");
                json.put("pageInfo", pageInfo);
                json.put("list", resultList);
                code = ResponseStatus.SUCCESS.getName();
                msg = ResponseStatus.SUCCESS.getValue();
            } else {
                msg = "暂时没有数据";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }

    /**
     * 公告详情
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("content/details")
    public String details(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (id != null && id != "") {
            Content content = contentService.findById(Integer.parseInt(id));
            model.addAttribute("content", content);
        }
        return "content/contentdetails";
    }
}
