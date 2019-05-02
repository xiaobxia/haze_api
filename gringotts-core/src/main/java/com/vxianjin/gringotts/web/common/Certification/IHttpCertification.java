package com.vxianjin.gringotts.web.common.Certification;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.web.pojo.User;

import java.util.Map;

public interface IHttpCertification {
    /**
     * 认证银行卡
     *
     * @param params params
     * @return res
     */
    ResponseContent bankCard(Map<String, String> params);

    /**
     * 人脸识别
     * @param user user
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param params params
     * @return res
     */
    ResponseContent face(User user, Map<String, String> params, String apiKey, String apiSecret);

    /**
     * 扫描身份证
     *
     * @param params params
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @return res
     */
    ResponseContent idcardScanning(Map<String, String> params, String apiKey, String apiSecret);
}
