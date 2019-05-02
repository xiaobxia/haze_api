//package com.vxianjin.gringotts.web.client;
//
//import com.alibaba.fastjson.JSONObject;
//import com.vxianjin.gringotts.api.CloseableOkHttp;
//import com.vxianjin.gringotts.api.service.IYunFaTongService;
//import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
//import retrofit2.Response;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 云证 获取token ,我方app未使用
// *
// * @author tgy
// * @version [版本号, 2018年1月23日]
// * @see [相关类/方法]
// * @since [产品/模块版本]
// */
//public class TokenClient {
//
//    /**
//     * 获取token用于签署以及查看合同
//     *
//     * @param contractId
//     * @param userId
//     * @return
//     */
//    public String feachToken(String userId, String contractId) throws IOException {
//
//        JSONObject json = new JSONObject();
//        json.put("contractId", contractId);
//        json.put("userId", userId);
//
//        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(json.toJSONString());
//
//        IYunFaTongService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongService.class);
//        Response<JSONObject> response = service.contractToken(headerMap, contractId, userId).execute();
//        if (response.isSuccessful()) {
//            if (response.body() != null) {
//                return response.body().toJSONString();
//            }
//        }
//    }
//}
