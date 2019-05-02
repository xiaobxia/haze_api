//package com.vxianjin.gringotts.web.client;
//
//import com.alibaba.fastjson.JSONObject;
//import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 云证 合同查看与签署
// *
// * @author tgy
// * @version [版本号, 2018年1月23日]
// * @see [相关类/方法]
// * @since [产品/模块版本]
// */
//
//public class SignAndViewClient extends BaseClient {
//
//    /**
//     * 合同签署
//     *
//     * @param userId
//     * @param contractId
//     * @return
//     */
//    public String signContract(String userId, String contractId) throws IOException {
//        JSONObject json = new JSONObject();
//        json.put("contractId", contractId);
//        json.put("userId", userId);
//        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(json.toJSONString());
//
//        String url = CfcaCommonUtil.SIGN_AND_VIEW_REQ_URL.concat("?contractid=" + contractId + "&userid=" + userId);
//        String result = "";
//        return result;
//    }
//
//}
