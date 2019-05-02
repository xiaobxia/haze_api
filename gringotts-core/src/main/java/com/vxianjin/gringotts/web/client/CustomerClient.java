package com.vxianjin.gringotts.web.client;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IYunFaTongApiService;
import com.vxianjin.gringotts.util.DefValueUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * 云证-主体(借款人用户)信息录入接口
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
@Deprecated
public class CustomerClient extends BaseClient {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClient.class);
    /**
     * 请求类型
     */
    private static final Integer reqType = 2;

    /**
     * 信息录入请求
     *
     * @param userInfo 用户信息
     * @return
     */
    public String addUser(CfcaUserInfo userInfo) throws IOException {
        String jsonValue = buildUserParams(userInfo);
        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(jsonValue);

        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongApiService.class);
        Response<JSONObject> response = service.customerAdd(headerMap, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonValue)).execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                saveHttpMessage(jsonValue, response.body().toJSONString(), reqType, userInfo.getUser().getId());
                return response.body().toJSONString();
            } else {
                saveHttpMessage(jsonValue, null, reqType, userInfo.getUser().getId());
            }
        } else {
            saveHttpMessage(jsonValue, null, reqType, userInfo.getUser().getId());
        }
        return null;
    }

    /**
     * 构建与云证交互的json串对象
     *
     * @param userInfo 用户信息
     * @return
     */
    private String buildUserParams(CfcaUserInfo userInfo) {
        JSONObject json = new JSONObject();
        Integer debtType = userInfo.getDebtType();
        Integer customerType = userInfo.getCustomerType();
        User user = userInfo.getUser();
        UserCardInfo cardInfo = userInfo.getCardInfo();

        Integer debtTypeStr = DefValueUtil.def(debtType, Integer.valueOf(CfcaCommonUtil.USER_DEPT_TYPE));
        Integer customerTypeStr = DefValueUtil.def(customerType, Integer.valueOf(CfcaCommonUtil.USER_CUSTOMER_TYPE));
        userInfo.setDebtType(debtTypeStr);
        userInfo.setCustomerType(customerTypeStr);
        json.put("debtType", debtTypeStr);
        json.put("customerType", customerTypeStr);
        JSONObject userJson = new JSONObject();
        userJson.put("userName", user.getRealname());
        userJson.put("address", user.getPresentAddressDistinct().concat(user.getPresentAddress()));
        userJson.put("mobile", user.getUserPhone());
        userJson.put("email", user.getEmail());
        userJson.put("idcardNo", user.getIdNumber());
        userJson.put("idcardImages", userInfo.getCfcaIdcardImages());
        userJson.put("idcardImages2", userInfo.getCfcaIdcardImages2());
        if (cardInfo != null) {
            String cardNo = cardInfo.getCard_no();
            String banAdd = cardInfo.getBankAddress();
            //甲方帐号
            userJson.put("bankCardNo", StringUtils.isNotEmpty(cardNo) ? cardNo : "");
            userJson.put("bankAddress", StringUtils.isNotEmpty(banAdd) ? banAdd : "");
        }
        userJson.put("wechatNo", user.getWechatAccount());
        userJson.put("qq", user.getQq());
        userJson.put("emergencyMobile", user.getFirstContactPhone());
        json.put("user", userJson);
        return json.toJSONString();
    }

    @Override
    public Logger getLOGGER() {
        return LOGGER;
    }
}
