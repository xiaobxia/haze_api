package com.vxianjin.gringotts.attach.controller;

import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.pojo.AppUpdateConfig;
import com.vxianjin.gringotts.web.service.IAppUpdateConfigService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class VersionsManagersController {
    private static Logger loger = LoggerFactory.getLogger(VersionsManagersController.class);

//    @Autowired
//    private BackConfigParamsService backConfigParamsService;

    @Resource
    private IAppUpdateConfigService appUpdateConfigService;


    /**
     * @param request req
     * @param response res
     */
    @RequestMapping(value = "versionsManagers/getVersionsInfo")
    public void getVersionsInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObj = new JSONObject();
        String code = "-1";
        String msg = "";
        try {
            String appVersion = request.getParameter("appVersion") == null ? "" : request.getParameter("appVersion");
            String clientType = request.getParameter("clientType") == null ? "" : request.getParameter("clientType");
            if (!"android".equals(clientType)) {
                appVersion = appVersion + ".0";
            }
            String[] str = appVersion.split("\\.");
            String v = "";
            if (str[0].toLowerCase().contains("V")) {
                v = str[0].substring(1, str[0].length() - 1);
            } else {
                v = str[0];
            }

            //0、不提示更新  1、提示更新 2、强制更新
            int verNum = Integer.parseInt(v) * 100 + Integer.parseInt(str[1]) * 10 + Integer.parseInt(str[2]);
            if (false) {
                resultMap.put("whether_need_upgrade", 1);
                resultMap.put("update_context", "");
                msg = "提示更新";
            } else {
                resultMap.put("whether_need_upgrade", 0);
                resultMap.put("update_context", "");
                msg = "不提示更新";
            }
            code = "0";
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            jsonObj.put("code", code);
            jsonObj.put("message", msg);
            jsonObj.put("data", dataMap);
            JSONUtil.toObjectJson(response, jsonObj.toString());
        }
    }

    /**
     * app更新接口
     */
    @RequestMapping("versionsManagers/appVersionCheck")
    public void appUpdataCheck(HttpServletRequest request, HttpServletResponse response) {
        AppUpdateConfig versionData = null;
        JSONObject jsonObj = new JSONObject();
        String code = "-1";
        String msg = "";
        String appVersion = request.getParameter("appVersion");
        String clientType = request.getParameter("clientType").toLowerCase();
        if (appVersion == null || "".equals(appVersion)|| clientType == null || "".equals(clientType)) {
            msg = "传入参数不能为空！";
            code = "500";
        } else {
            try {
                versionData = appUpdateConfigService.findUpdateVersionInfo(clientType, appVersion);
                msg = "成功";
                code = "0";
            } catch (Exception e) {
                loger.error("app更新接口异常", e);
                code = "500";
                msg = "系统异常";
            }
        }
        jsonObj.put("code", code);
        jsonObj.put("message", msg);
        jsonObj.put("data", versionData);
        JSONUtil.toObjectJson(response, jsonObj.toString());
    }
}
