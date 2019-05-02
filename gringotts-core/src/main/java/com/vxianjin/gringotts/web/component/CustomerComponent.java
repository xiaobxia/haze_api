package com.vxianjin.gringotts.web.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.client.CustomerClient;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 云证-主体(借款人用户)信息录入接口
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component(value = "customerCnt")
public class CustomerComponent implements ICustomerComponent {

    /**
     * 日志打印
     */
    private static Logger LOGGER = LoggerFactory.getLogger(CustomerComponent.class);

    @Autowired
    private CustomerClient client;

    /**
     * 用户信息录入并向上游返回执行结果
     *
     * @param userInfo
     * @return
     * @throws IOException
     */
    @Override
    public JsonResult registUserCustomer(CfcaUserInfo userInfo) throws IOException {
        String result = client.addUser(userInfo);
        LOGGER.info(result);
        JsonResult ret = dealCfcaResult(result);
        String data = ret.getData().toString();
        //成功直接保存用戶ID
        if (CfcaCommonUtil.CODE_SUCCESS.equals(ret.getCode())) {
            userInfo.setCfcaUserId(JSON.parseObject(data).getString("userid"));
        }
        //用户已存在
        else if (CfcaCommonUtil.CODE_EXIST.equals(ret.getCode())) {
            userInfo.setCfcaUserId(JSON.parseObject(data).getString("userid"));
            userInfo.setCfcaIdcardImages(JSON.parseObject(data).getString("idcardImg1"));
            userInfo.setCfcaIdcardImages2(JSON.parseObject(data).getString("idcardImg2"));
        }
        //重設data
        ret.setData(userInfo);
        return ret;
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
            LOGGER.error("Add CFCA's customer failed , please check network!");
            ret.setMsg("Add CFCA's customer failed , please check network!");
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
            return ret;
        }
        String code = jsonCfca.getString("code");
        String msg = jsonCfca.getString("msg");
        //成功
        if ("0".equals(code)) {
            ret.setMsg("SUCCESS ADD CFCA CUSTOMER!");
            ret.setResult(CfcaCommonUtil.RET_SUCCESS);
            ret.setCode(CfcaCommonUtil.CODE_SUCCESS);
            //獲取云證userID
            String data = jsonCfca.getString("data");
            ret.setData(data);
        } else if ("1".equals(code)) {
            ret.setMsg("cfca user already exist!");
            ret.setResult(CfcaCommonUtil.RET_SUCCESS);
            ret.setCode(CfcaCommonUtil.CODE_EXIST);
            //獲取云證userID
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
