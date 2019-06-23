package com.vxianjin.gringotts.pay.controller.bill99;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.common.util.bill99.KQRSA;
import com.vxianjin.gringotts.pay.common.util.chanpay.Bill99Util;
import com.vxianjin.gringotts.pay.service.bill99.Bill99WithdrawService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.vxianjin.gringotts.pay.common.util.bill99.PKIUtil.byte2UTF8String;
import static com.vxianjin.gringotts.pay.common.util.bill99.PKIUtil.utf8String2ByteWithBase64;
import static com.vxianjin.gringotts.pay.common.util.chanpay.Bill99Util.createSealDataType;

/**
 * 快钱支付代付相关
 * @author jintian on 2018/7/17.
 */
@Controller
@RequestMapping(value = "/bill99")
public class Bill99WithdrawController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(Bill99WithdrawController.class);

    @Resource
    private Bill99WithdrawService bill99WithdrawService;

    /**
     * 提现回调(快钱代收付)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdrawCallback" ,method = RequestMethod.POST)
    public String chanpayRepaymentNotify(HttpServletRequest request) {
        String xml = Bill99Util.genRequestXml(request);
        Map<String, String> drawParams = Bill99Util.getDrawParams();
        Map<String, String> params = new HashMap();
        boolean flag = true;
        try {
            Map<String, String> m = Bill99Util.xmlToMap(xml, "encryptedData", "digitalEnvelope");
            try {
                byte[] secretKey = KQRSA.decryptSecretKey(KQRSA.asymmetricAlgorithm, utf8String2ByteWithBase64(m.get
                        ("digitalEnvelope")), KQRSA.getPriKey(drawParams.get("pfxPath"), drawParams.get
                        ("keyStorePass"), drawParams.get("keyStorePass")));
                byte[] decrypt = KQRSA.decrypt(KQRSA.symmetricAlgorithm, utf8String2ByteWithBase64(m.get
                                ("encryptedData")),
                        secretKey);
                params.put("signedData", StringUtils.getTagText(xml, "signedData"));
                params.put("orderXml", byte2UTF8String(decrypt));

                String orderXml = params.get("orderXml");
                String orderId = StringUtils.getTagText(orderXml, "merchant_id");
                String outTradeNo = StringUtils.getTagText(orderXml, "order_seq_id");
                String code = StringUtils.getTagText(orderXml, "error_code");
                String payStatus = StringUtils.getTagText(orderXml, "status");
                String desc = StringUtils.getTagText(orderXml, "error_msg");

                bill99WithdrawService.payWithdrawCallback(orderId, outTradeNo, code, payStatus, desc);
            } catch (Exception e) {
                logger.info("快钱代付通知解密数据失败:" + e.getMessage());
                flag = false;
            }
        } catch (Exception e) {
            logger.error("提现回调(快钱代收付)", e);
        }
        return returnMessage(flag, drawParams, params.get("orderXml"));
    }


    /**
     * 用户提现（代付）请求接口
     *
     * @param userId   借款用户id
     * @param borrowId 借款订单id
     * @param uuid     此次交易的随机编号
     * @param sign     加密签名，用于数据校验，以防数据被篡改
     */
    @ResponseBody
    @RequestMapping(value = "withdraw/{userId}/{borrowId}/{uuid}/{sign}")
    public ResponseContent payWithdraw(@PathVariable String userId, @PathVariable String borrowId, @PathVariable String uuid, @PathVariable String sign) {
        logger.info("ChanPayWithdrawController.payWithdraw userId=" + userId + " borrowId=" + borrowId + " uuid=" + uuid + " sign=" + sign);
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowId) || StringUtils.isBlank(uuid) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return bill99WithdrawService.payWithdraw(userId, borrowId, uuid, sign);
        } catch (BizException e) {
            logger.info("ChanPayWithdrawController error:" + e.getErrorMsg() + "message:" + e.getMessage());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.info("ChanPayWithdrawController error message:" + e.getMessage());
            return new ResponseContent("-105", e.getMessage());
        }
    }

    private String returnMessage(boolean flag, Map<String, String> drawParams, String originalData) {
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        sb.append("<notifyResponse><notifyHead><version>1.0</version><memberCode>").append(drawParams.get("account")).
                append("</memberCode></notifyHead><notifyResponseBody><sealDataType>");
        String status = flag ? "111" : "112";
        originalData = originalData.replaceAll("<status>.*</status>", "<status>" + status + "</status>");
        createSealDataType(drawParams, sb, originalData);
        sb.append("</sealDataType><isReceived>1</isReceived></notifyResponseBody></notifyResponse>");
        return sb.toString();
    }

}
