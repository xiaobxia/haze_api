package com.vxianjin.gringotts.web.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IYunFaTongApiService;
import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * 云证 合同签署和查看
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component(value = "signAndViewCnt")
public class SignAndViewComponent implements ISignAndViewComponent {

    /**
     * 日志打印
     */
    private static Logger LOGGER = LoggerFactory.getLogger(SignAndViewComponent.class);

    /**
     * 合同签署与查看
     *
     * @param userId
     * @param contractId
     * @return
     */
    @Override
    public JsonResult signAndViewContract(String userId, String contractId) throws IOException {


        JSONObject json = new JSONObject();
        json.put("contractId", contractId);
        json.put("userId", userId);
        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(json.toJSONString());

        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongApiService.class);
        Response<JSONObject> response = service.contractCheck(headerMap, contractId, userId).execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                JsonResult jet = dealCfcaResult(response.body().toJSONString());
                if (CfcaCommonUtil.CODE_SUCCESS.equals(jet.getCode())) {
                    String token = jet.getData().toString();
                    JSON.parseObject(token).getString("token");
                    jet.setData(token);
                }
            }
        }
        return new JsonResult();
    }

    /**
     * 处理云证系统返回的json字符串
     *
     * @param resultMsg
     * @return
     */
    private JsonResult dealCfcaResult(String resultMsg) {
        JsonResult ret = new JsonResult();
        ret.setCount("1");
        ret.setData(resultMsg);

        //云证出错可能会返回一个网页
        if (!StringUtils.isEmpty(resultMsg)) {
            if (resultMsg.contains("<html")) {
                ret.setData(resultMsg);
                ret.setMsg("CFCA DEAL ERROR,AND RETURN HTML PAGE!");
                ret.setResult(CfcaCommonUtil.RET_FAILURE);
                ret.setCode(CfcaCommonUtil.CODE_FAILURE);
                return ret;
            }
        }

        //当是json串处理成json
        JSONObject jsonCfca = JSON.parseObject(resultMsg);

        //返回結果為空或不存在code
        if (jsonCfca == null || StringUtils.isEmpty(jsonCfca.getString("code"))) {
            LOGGER.error("Generate contract failed , please check network!");
            ret.setMsg("Generate contract failed , please check network!");
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
            return ret;
        }
        String code = jsonCfca.getString("code");
        String msg = jsonCfca.getString("msg");
        //成功
        if ("0".equals(code)) {
            ret.setMsg("SUCCESS GENERATE CONTRACT!");
            ret.setCode(CfcaCommonUtil.RET_SUCCESS);
            ret.setCode(CfcaCommonUtil.CODE_SUCCESS);
            String data = jsonCfca.getString("data");
            ret.setData(data);
        } else {
            ret.setMsg(msg);
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
        }
        return ret;
    }
}
