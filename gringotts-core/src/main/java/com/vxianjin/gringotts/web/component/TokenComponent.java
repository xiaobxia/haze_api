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
 * 云证 获取Token
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component(value = "tokenCnt")
public class TokenComponent implements ITokenComponent {

    /**
     * 日志打印
     */
    private static Logger LOGGER = LoggerFactory.getLogger(TokenComponent.class);

    /**
     * 获取token用于签署以及查看合同
     *
     * @param contractId
     * @param userId
     * @return
     */
    @Override
    public JsonResult getToken(String userId, String contractId) throws IOException {

        JSONObject json = new JSONObject();
        json.put("contractId", contractId);
        json.put("userId", userId);

        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(json.toJSONString());

        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongApiService.class);
        Response<JSONObject> response = service.contractToken(headerMap, contractId, userId).execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                return dealCfcaResult(response.body().toJSONString());
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
            LOGGER.error("Get Token failed , please check network!");
            ret.setMsg("Get Token failed , please check network!");
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
            return ret;
        }
        String code = jsonCfca.getString("code");
        String msg = jsonCfca.getString("msg");
        //成功
        if ("0".equals(code)) {
            ret.setMsg("GET TOKEN SUCCESS!");
            ret.setCode(CfcaCommonUtil.RET_SUCCESS);
            ret.setCode(CfcaCommonUtil.CODE_SUCCESS);
            ret.setData(msg);
        } else {
            ret.setMsg(msg);
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
        }
        return ret;

    }
}
