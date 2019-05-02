package com.vxianjin.gringotts.risk.service;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IShuJuMoHeApiService;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.risk.pojo.RiskOrders;
import com.vxianjin.gringotts.risk.utils.ConstantRisk;
import com.vxianjin.gringotts.util.WebClient;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TdService implements ITdService {
    private static final String PARTNER_CODE = "jiexiang_mohe";//合作方标识
    private static final String PARTNER_KEY = "9a30d6e3f69e433694630d374e07a5d7";//合作方密钥
    private static final String BASE_URL = "https://api.shujumohe.com/octopus";//基本地址
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IRiskOrdersService riskOrdersService;

    @Override
    public ResponseContent getReport(HashMap<String, Object> params) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        try {
            Object userName = params.get("userName");
            Object cardNum = params.get("cardNum");
            Object userId = params.get("userId");
            Object userPhone = params.get("userPhone");
            if (userName != null && cardNum != null && userId != null
                    && userPhone != null) {
                LinkedHashMap<String, String> map2 = SysCacheUtils
                        .getConfigParams(BackConfigParams.TD);
                String url = map2.get("TD_URL2") + "?partner_code="
                        + map2.get("TD_BZM") + "&partner_key="
                        + map2.get("TD_KEY") + "&app_name="
                        + map2.get("TD_APP_NAME");
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", userName + "");
                map.put("id_number", cardNum + "");
                map.put("mobile", userPhone + "");
                RiskOrders orders = new RiskOrders();
                orders.setUserId(userId + "");
                orders.setOrderType(ConstantRisk.TD);
                orders.setAct(ConstantRisk.TD_PRELOAN_APPLY);
                orders.setOrderNo(UUID.randomUUID().toString());
                orders.setReqParams("url=" + url + ",params=" + map);
                logger.info("getReport send  " + orders.toString());
                riskOrdersService.insert(orders);
                String result = WebClient.getInstance().doPost(url, map);
                logger.info("getReport return " + result);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.fromObject(result);
                    if (jsonObject.getBoolean("success")) {
                        serviceResult = new ResponseContent(
                                ResponseContent.SUCCESS, jsonObject
                                .getString("report_id"));
                    } else {
                        serviceResult = new ResponseContent("300", jsonObject
                                .getString("reason_code")
                                + jsonObject.getString("reason_desc"));
                    }
                    orders.setReturnParams(result);
                    orders.setStatus(RiskOrders.STATUS_SUC);
                    riskOrdersService.update(orders);
                } else {
                    orders.setReturnParams("td return null");
                    serviceResult = new ResponseContent("100", "同盾返回空或请求报错");
                }
                riskOrdersService.update(orders);

            } else {
                serviceResult = new ResponseContent("400", "必要参数不足！");
            }
        } catch (Exception e) {
            logger.error("getReport error ,params=" + params, e);
        }
        return serviceResult;
    }

    @Override
    public ResponseContent queryReport(HashMap<String, Object> params) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        try {
            Object userId = params.get("userId");
            Object reportId = params.get("reportId");
            if (reportId != null && userId != null) {
                LinkedHashMap<String, String> map2 = SysCacheUtils
                        .getConfigParams(BackConfigParams.TD);
                String url = map2.get("TD_URL3") + "?partner_code="
                        + map2.get("TD_BZM") + "&partner_key="
                        + map2.get("TD_KEY") + "&app_name="
                        + map2.get("TD_APP_NAME") + "&report_id=" + reportId;
                RiskOrders orders = new RiskOrders();
                orders.setUserId(userId + "");
                orders.setOrderType(ConstantRisk.TD);
                orders.setAct(ConstantRisk.TD_PRELOAN_REPORT);
                orders.setOrderNo(UUID.randomUUID().toString());
                orders.setReqParams("url=" + url);
                logger.info("queryReport send  " + orders.toString());
                riskOrdersService.insert(orders);
                String result = WebClient.getInstance().doGet(url);
                logger.info("queryReport return " + result);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.fromObject(result);
                    if (jsonObject.getBoolean("success")) {
                        serviceResult = new ResponseContent(
                                ResponseContent.SUCCESS, result);
                    } else {
                        if ("203".equals(jsonObject.getString("reason_code"))) {
                            serviceResult = new ResponseContent("203", jsonObject
                                    .getString("reason_code")
                                    + jsonObject.getString("reason_desc"));
                        } else {
                            serviceResult = new ResponseContent("300", jsonObject
                                    .getString("reason_code")
                                    + jsonObject.getString("reason_desc"));
                        }
                    }
                    orders.setReturnParams(result);
                    orders.setStatus(RiskOrders.STATUS_SUC);
                    riskOrdersService.update(orders);
                } else {
                    orders.setReturnParams("td return null");
                    serviceResult = new ResponseContent("100", "同盾返回空或请求报错");
                }
                riskOrdersService.update(orders);

            } else {
                serviceResult = new ResponseContent("400", "必要参数不足！");
            }
        } catch (Exception e) {
            logger.error("queryReport error ,params=" + params, e);
        }
        return serviceResult;
    }

    @Override
    public ResponseContent getMobileReport(HashMap<String, Object> params) {
        logger.info("getMobileReport start");
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        try {
            String taskId = params.get("task_id").toString();
            String userId = params.get("user_id").toString();
            //创建请求地址
            // https://api.shujumohe.com/octopus/task.unify.query/v3
            String url = BASE_URL + "/task.unify.query/v3?partner_code=" + PARTNER_CODE + "&partner_key=" + PARTNER_KEY;
            Map<String, String> headers = null;
            //请求参数
            String body_str = "task_id=" + taskId;
            String result = "";
            String code = "";
            String message = "";

            RiskOrders orders = new RiskOrders();
            orders.setUserId(userId + "");
            orders.setOrderType(ConstantRisk.TD);
            orders.setAct("TD_REPORT");
            orders.setOrderNo(UUID.randomUUID().toString());
            orders.setReqParams("url=" + url + ",params=" + body_str);

            IShuJuMoHeApiService service = CloseableOkHttp.obtainRemoteService(
                    "https://api.shujumohe.com/", IShuJuMoHeApiService.class);
            Call<com.alibaba.fastjson.JSONObject> call = service.taskUnifyQuery(
                    PARTNER_CODE,
                    PARTNER_KEY,
                    taskId
            );
            Response<com.alibaba.fastjson.JSONObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                com.alibaba.fastjson.JSONObject resultJson = response.body();
                code = resultJson.getString("code");
                message = resultJson.getString("message");
                if ("0".equals(code)) {
                    serviceResult = new ResponseContent(ResponseContent.SUCCESS, result);
                } else {
                    serviceResult = new ResponseContent(code, message);
                }
                orders.setReturnParams(result);
                orders.setStatus(RiskOrders.STATUS_SUC);
                riskOrdersService.update(orders);
            } else {
                orders.setReturnParams("td return null");
                serviceResult = new ResponseContent("100", "同盾返回空或请求报错");
            }
        } catch (Exception e) {
            logger.error("getMobileReport error ,params=" + JSON.toJSONString(params), e);
        }
        logger.info("getMobileReport end");
        return serviceResult;
    }

}
