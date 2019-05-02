package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.UserPushUntil;
import com.vxianjin.gringotts.util.WebClient;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.service.IPushUserService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送用户认证信息
 *
 * @author Administrator
 */
@Service
public class PushUserService implements IPushUserService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IUserService userService;


    @Override
    public ResponseContent addPushUserApproves(HashMap<String, Object> params) {
        ResponseContent serviceResult = new ResponseContent("500", "系统异常");
        try {
            logger.error("begin addPushUserApproves");
            Map<String, String> mapPush = SysCacheUtils.getConfigParams("PUSH_USER");
            String pushUrl = mapPush.get("push_user_url");
            String pushIsopen = mapPush.get("push_isopen");
            if (!"1".equals(pushIsopen)) {
                serviceResult.setMsg("addPushUserApprove pushIsopen close 推送未开启");
                return serviceResult;
            }
            String urlAdd = UserPushUntil.ADD_URL;
            Object userId = params.get("userId");
            if (userId != null && pushUrl != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pushId", params.get("pushId").toString());
                map.put("userId", params.get("userId").toString());
                map.put("pushType", params.get("pushType").toString());
                if (params.containsKey("userPhone") && params.get("userPhone") != null) {
                    map.put("userPhone", params.get("userPhone").toString());
                }
                if (params.containsKey("realName") && params.get("realName") != null) {
                    map.put("realName", params.get("realName").toString());
                }

                map.put("approveTime", params.get("approveTime").toString());
                // 设置连接5秒超时
                HashMap<String, Integer> paramsTime = new HashMap<String, Integer>();
                paramsTime.put("soketOut", 5000);
                paramsTime.put("connOut", 5000);
                String url = pushUrl + urlAdd;
                String result = WebClient.getInstance().doPost(url, map);
                String paramsJosn = JSONUtil.beanToJson(map);
                logger.error("requestparamsJosn+" + paramsJosn + "==" + url);
//				String result = new WebClient().postJsonData(url, paramsJosn, paramsTime);

                if (result == null) {
                    serviceResult.setMsg("连接超时！");
                    return serviceResult;
                }
                try {
                    serviceResult = JSONUtil.jsonToBean(result, ResponseContent.class);
                } catch (Exception e) {
                    serviceResult.setMsg("请求地址错误导致返回结果解析错误");
                    logger.error(" addPushUserApprove URL error  pushType:" + params.get("pushType") + ", userId:" + params.get("userId")
                            + ",dateTime:" + params.get("approveTime"), e);

                }
            } else {
                serviceResult.setMsg("参数不足");
                return serviceResult;
            }

        } catch (Exception e) {

            logger.error(
                    " Dt info error pushType:" + params.get("pushType") + ", userId:" + params.get("userId") + ",dateTime:"
                            + params.get("approveTime"), e);

        }
        return serviceResult;
    }

}
