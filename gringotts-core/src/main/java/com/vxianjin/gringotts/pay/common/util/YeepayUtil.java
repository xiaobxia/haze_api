package com.vxianjin.gringotts.pay.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.JKey;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.model.YPBatchPayResultReq;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.Digest;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;


public class YeepayUtil {
    private static final Logger log = LoggerFactory.getLogger(YeepayUtil.class);
    /**
     * 请求的数据和商户自己的密钥拼成一个字符串
     */
    public static String getRequestStr(Map<String, String> pmap, String hmacKey) {
        StringBuffer hmacStr = new StringBuffer();

        hmacStr.append(pmap.get("cmd"))
                .append(pmap.get("mer_Id"))
                .append(pmap.get("batch_No"))
                .append(pmap.get("order_Id"))
                .append(pmap.get("amount"))
                .append(pmap.get("account_Number"))
                .append(hmacKey);
        return hmacStr.toString();
    }

    /**
     * 构造批量明细查询字符串
     * @param payResultReq 参数map
     * @param hmacKey 商户私钥
     * @return
     */
    public static String getBatchDetailQueryStr(YPBatchPayResultReq payResultReq, String hmacKey){

        StringBuffer hmacStr = new StringBuffer();

        hmacStr.append(payResultReq.getCmd())
                .append(payResultReq.getVersion())
                .append(payResultReq.getGroupId())
                .append(payResultReq.getMerId())
                .append(payResultReq.getQueryMode())
                .append(payResultReq.getBatchNo())
                .append(payResultReq.getOrderId())
                .append(payResultReq.getPageNo())
                .append(hmacKey);
        return hmacStr.toString();
    }

    /**
     * 响应的数据和商户自己的密钥拼成一个字符串
     */
    public static String getResponseStr(Map<String, Object> pmap, String hmacKey) {
        StringBuffer hmacStr = new StringBuffer();
        hmacStr.append(pmap.get("cmd") != null ? pmap.get("cmd").toString() : "")
                .append(pmap.get("ret_Code") != null ? pmap.get("ret_Code").toString() : "")
                .append(pmap.get("mer_Id") != null ? pmap.get("mer_Id").toString() : "")
                .append(pmap.get("batch_No") != null ? pmap.get("batch_No").toString() : "")
                .append(pmap.get("total_Amt") != null ? pmap.get("total_Amt").toString() : "")
                .append(pmap.get("total_Num") != null ? pmap.get("total_Num").toString() : "")
                .append(pmap.get("r1_Code") != null ? pmap.get("r1_Code").toString() : "")
                .append(hmacKey);
        return hmacStr.toString();
    }

    /**
     * 异步响应的数据和商户自己的密钥拼成一个字符串
     */
    public static String getNotifyResponseStr(Map<String, Object> pmap, String hmacKey) {
        StringBuffer hmacStr = new StringBuffer();
        hmacStr.append(pmap.get("cmd") != null ? pmap.get("cmd").toString() : "")
                .append(pmap.get("mer_Id") != null ? pmap.get("mer_Id").toString() : "")
                .append(pmap.get("batch_No") != null ? pmap.get("batch_No").toString() : "")
                .append(pmap.get("order_Id") != null ? pmap.get("order_Id").toString() : "")
                .append(pmap.get("ret_Code") != null ? pmap.get("ret_Code").toString() : "")
                .append(hmacKey);
        return hmacStr.toString();
    }

    /**
     *
     */
    public static Map<String, Object> getResponseMap(String responseMsg) {

        String cmdValue = null;

        Document backDocument = null;
        Map<String, Object> xmlBackMap = new HashMap<String, Object>();

        try {
            backDocument = DocumentHelper.parseText(responseMsg);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element backRootEle = backDocument.getRootElement();
        cmdValue = backRootEle.elementText("hmac");
        if (cmdValue == null) {
            return null;
        }

        List backlist = backRootEle.elements();

        for (int i = 0; i < backlist.size(); i++) {
            Element ele = (Element) backlist.get(i);
            String eleName = ele.getName();
            if (!"list".equals(eleName)) {
                xmlBackMap.put(eleName, ele.getText().trim());
            } else {
                continue;
            }
        }

        return xmlBackMap;
    }

    /**
     * 用数字证书进行签名
     */
    public static Map<String, Object> getSign(String hmacStr) {
        com.cfca.util.pki.cipher.Session tempsession = null;
        String ALGORITHM = SignatureUtil.SHA1_RSA;
        JCrypto jcrypto = null;
        if (tempsession == null) {
            try {
                //初始化加密库，获得会话session
                //多线程的应用可以共享一个session,不需要重复,只需初始化一次
                //初始化加密库并获得session。
                //系统退出后要jcrypto.finalize()，释放加密库
                jcrypto = JCrypto.getInstance();
                jcrypto.initialize(JCrypto.JSOFT_LIB, null);
                tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.toString());
            }
        }

        JKey jkey = null;
        X509Cert cert = null;
        try {
            jkey = KeyUtil.getPriKey(YeepayUtil.class.getClassLoader().getResource("/CER_yeepay_real/" + PropertiesConfigUtil.get("YEEPAY_CAFA")).toURI().getPath(), "123456");
            cert = CertUtil.getCert(YeepayUtil.class.getClassLoader().getResource("/CER_yeepay_real/" + PropertiesConfigUtil.get("YEEPAY_CAFA")).toURI().getPath(), "123456");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }

        X509Cert[] cs = new X509Cert[1];
        cs[0] = cert;

        String signMessage = "";
        SignatureUtil signUtil = null;
        try {
            //对请求的串进行MD5对数据进行签名
            String yphs = Digest.hmacSign(hmacStr);
            signUtil = new SignatureUtil();
            byte[] b64SignData;
            //对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
            b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(), ALGORITHM, jkey, cs, tempsession);
            if (jcrypto != null) {
                jcrypto.finalize(JCrypto.JSOFT_LIB, null);
            }
            signMessage = new String(b64SignData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("sign", signMessage);
        resultMap.put("session", tempsession);
        return resultMap;
    }

    /**
     * 构造单笔请求代付的xml数据
     */
    public static String getRequestXmlSingle(Map<String, String> pmap) {
        StringBuffer xmlStr = new StringBuffer();
        xmlStr.append("<data>")
                .append("<cmd>" + pmap.get("cmd") + "</cmd>")
                .append("<version>" + pmap.get("version") + "</version>")
                .append("<mer_Id>" + pmap.get("mer_Id") + "</mer_Id>")
                .append("<group_Id>" + pmap.get("group_Id") + "</group_Id>")
                .append("<bank_Name>" + pmap.get("bank_Name") + "</bank_Name>")
                .append("<batch_No>" + pmap.get("batch_No") + "</batch_No>")
                .append("<order_Id>" + pmap.get("order_Id") + "</order_Id>")
                .append("<amount>" + pmap.get("amount") + "</amount>")
                .append("<account_Name>" + pmap.get("account_Name") + "</account_Name>")
                .append("<account_Number>" + pmap.get("account_Number") + "</account_Number>")
                .append("<fee_Type>" + pmap.get("fee_Type") + "</fee_Type>")
                .append("<remarksInfo>" + pmap.get("remarksInfo") + "</remarksInfo>")
                .append("<urgency>" + pmap.get("urgency") + "</urgency>")
                .append("<hmac>" + pmap.get("hmac") + "</hmac>")
                .append("</data>");
        return xmlStr.toString();
    }

    /**
     * 构造单笔代付异步通知响应的xml数据
     */
    public static String getNotifyResponseXml(Map<String, Object> pmap, String hamc) {
        StringBuffer xmlStr = new StringBuffer();
        xmlStr.append("<?xml version=\"1.0\" encoding=\"GBK\"?>")
                .append("<data>")
                .append("<cmd>" + pmap.get("cmd").toString() + "</cmd>")
                .append("<version>" + pmap.get("version").toString() + "</version>")
                .append("<mer_Id>" + pmap.get("mer_Id").toString() + "</mer_Id>")
                .append("<batch_No>" + pmap.get("batch_No").toString() + "</batch_No>")
                .append("<order_Id>" + pmap.get("order_Id").toString() + "</order_Id>")
                .append("<ret_Code>" + pmap.get("status").toString() + "</ret_Code>")
                .append("<hmac>" + hamc + "</hmac>")
                .append("</data>");
        return xmlStr.toString();
    }

    /**
     *
     * 构造批次明细查询的xml数据
     * @param yeepayBatchPayDetailReq
     * @return
     */
    public static String getBatchPayDetailResponseXml(YPBatchPayResultReq yeepayBatchPayDetailReq){

        StringBuffer xmlStr = new StringBuffer();
        xmlStr.append("<?xml version=\"1.0\" encoding=\"GBK\"?>")
                .append("<data>")
                .append("<cmd>" + yeepayBatchPayDetailReq.getCmd() + "</cmd>")
                .append("<version>" + yeepayBatchPayDetailReq.getVersion() + "</version>")
                .append("<group_Id>"+yeepayBatchPayDetailReq.getGroupId()+"</group_Id>")
                .append("<mer_Id>" + yeepayBatchPayDetailReq.getMerId() + "</mer_Id>")
                .append("<query_Mode>" + yeepayBatchPayDetailReq.getQueryMode() + "</query_Mode>")
                .append("<product>" + yeepayBatchPayDetailReq.getProduct() + "</product>")
                .append("<batch_No>" + yeepayBatchPayDetailReq.getBatchNo() + "</batch_No>")
                .append("<order_Id>" + yeepayBatchPayDetailReq.getOrderId() + "</order_Id>")
                .append("<page_No>" + yeepayBatchPayDetailReq.getPageNo() + "</page_No>")
                .append("<hmac>" + yeepayBatchPayDetailReq.getHmac() + "</hmac>")
                .append("</data>");
        return xmlStr.toString();
    }

    /**
     * 对同步返回数据进行验签
     *
     * @return
     */
    public static boolean getVerifySign(String responseMsg, com.cfca.util.pki.cipher.Session session, String hmacKey) {

        try {

            Document document = DocumentHelper.parseText(responseMsg);
            Element rootEle = document.getRootElement();
            String cmdValue = rootEle.elementText("hmac");
            boolean sigerCertFlag = false;
            SignatureUtil signUtil = new SignatureUtil();
            sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), session);
            if (!sigerCertFlag) {
                return false;
            }

            String backmd5hmac = new String(signUtil.getSignedContent());

            Map<String, Object> xmlBackMap = getResponseMap(responseMsg);
            if (xmlBackMap == null) {
                return false;
            }

            //将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
            String backHmacStr = getResponseStr(xmlBackMap, hmacKey);

            String newmd5hmac = Digest.hmacSign(backHmacStr);
            if (newmd5hmac.equals(backmd5hmac) && signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf(PayConstants.SIANG_CERT) > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 对同步返回数据进行验签
     * 成功返回map,失败则返回null
     * @return
     */

    public static Map<String, Object> getVerifySignMap(String responseMsg, com.cfca.util.pki.cipher.Session session, String hmacKey) {

        try {
            Document document = DocumentHelper.parseText(responseMsg);
            Element rootEle = document.getRootElement();
            String cmdValue = rootEle.elementText("hmac");
            boolean sigerCertFlag = false;
            SignatureUtil signUtil = new SignatureUtil();
            sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), session);
            if (!sigerCertFlag) {
                return null;
            }

            String backmd5hmac = new String(signUtil.getSignedContent());

            Map<String, Object> xmlBackMap = getResponseMap(responseMsg);
            if (xmlBackMap == null) {
                return null;
            }

            //将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
            String backHmacStr = getResponseStr(xmlBackMap, hmacKey);

            String newmd5hmac = Digest.hmacSign(backHmacStr);
            if (newmd5hmac.equals(backmd5hmac) && signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf(PayConstants.SIANG_CERT) > 0) {
                return xmlBackMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对响应的报文进行签名验证
     */
    public static String getNotifyVerifySign(String requestXml, String hmacKey) {
        try {
            Document document = null;
            document = DocumentHelper.parseText(requestXml);

            Element rootEle = document.getRootElement();
            String cmdValue = rootEle.elementText("hmac");

            if (cmdValue == null) {
                return null;
            }

            //对服务器响应报文进行验证签名
            com.cfca.util.pki.cipher.Session tempsession = null;
            String ALGORITHM = SignatureUtil.SHA1_RSA;
            JCrypto jcrypto = null;
            if (tempsession == null) {
                //初始化加密库，获得会话session
                //多线程的应用可以共享一个session,不需要重复,只需初始化一次
                //初始化加密库并获得session。
                //系统退出后要jcrypto.finalize()，释放加密库
                jcrypto = JCrypto.getInstance();
                jcrypto.initialize(JCrypto.JSOFT_LIB, null);
                tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
            }

            JKey jkey = KeyUtil.getPriKey(YeepayUtil.class.getClassLoader().getResource("/CER_yeepay_real/" + PropertiesConfigUtil.get("YEEPAY_CAFA")).toURI().getPath(), "123456");
            X509Cert cert = CertUtil.getCert(YeepayUtil.class.getClassLoader().getResource("/CER_yeepay_real/" + PropertiesConfigUtil.get("YEEPAY_CAFA")).toURI().getPath(), "123456");

            X509Cert[] cs = new X509Cert[1];
            cs[0] = cert;

            boolean sigerCertFlag = false;
            SignatureUtil signUtil = new SignatureUtil();

            sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), tempsession);
            if (!sigerCertFlag) {
                return null;
            }
            String backmd5hmac = "";

            System.out.println("证书验签成功");
            backmd5hmac = new String(signUtil.getSignedContent());
            System.out.println("证书验签获得的MD5签名数据为----" + backmd5hmac);
            System.out.println("证书验签获得的证书dn为----" + new String(signUtil.getSigerCert()[0].getSubject()));
            //将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
            Document backDocument = null;
            try {
                backDocument = DocumentHelper.parseText(requestXml);
            } catch (DocumentException e) {
                System.out.println(e);
            }
            Element backRootEle = backDocument.getRootElement();
            List backlist = backRootEle.elements();

            Map<String, Object> xmlBackMap = new HashMap<String, Object>();

            for (int i = 0; i < backlist.size(); i++) {
                Element ele = (Element) backlist.get(i);
                String eleName = ele.getName();
                if (!"list".equals(eleName)) {
                    xmlBackMap.put(eleName, ele.getText().trim());
                } else {
                    continue;
                }
            }
            String backHmacStr = "";
            String[] backDigestValues = "cmd,mer_Id,batch_No,order_Id,status,message,hmacKey".split(",");
            for (int i = 0; i < backDigestValues.length; i++) {
                if ("hmacKey".equals(backDigestValues[i])) {
                    backHmacStr = backHmacStr + hmacKey;
                    continue;
                }
                String tempStr = (String) xmlBackMap.get(backDigestValues[i]);
                backHmacStr = backHmacStr + ((tempStr == null) ? "" : tempStr);
            }
            String newmd5hmac = Digest.hmacSign(backHmacStr);
            System.out.println("提交返回源数据为---||" + backHmacStr + "||");
            System.out.println("经过md5签名后的验证返回hmac为---||" + newmd5hmac + "||");
            System.out.println("提交返回的hmac为---||" + backmd5hmac + "||");

            if (!newmd5hmac.equals(backmd5hmac)) {
                System.out.println("md5验签不成功");
                return null;
            }

            //判断该证书DN是否为易宝
            if (signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf(PayConstants.SIANG_CERT) > 0) {
                System.out.println("证书DN是易宝的");

                cmdValue = "TransferNotify" + xmlBackMap.get("mer_Id") + xmlBackMap.get("batch_No") + xmlBackMap.get("order_Id") + "S" + hmacKey;
                String hmac = Digest.hmacSign(cmdValue);

                return new String(signUtil.p7SignMessage(true, hmac.getBytes(), ALGORITHM, jkey, cs, tempsession));
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //将获取到的response转换成json格式
    public static Map<String, Object> parseResponse(String yopresponse) {

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap = JSON.parseObject(yopresponse,
                new TypeReference<TreeMap<String, Object>>() {
                });
        System.out.println("将response转化为map格式之后: " + jsonMap);
        return jsonMap;
    }

    public static Map<String, Object> yeepayYOP(Map<String, Object> map, String url) throws IOException {

        Map<String, Object> result = new HashMap<>();
        YopRequest yoprequest = new YopRequest("OPR:" + PayConstants.MERCHANT_NO,PayConstants.MERCHANT_PRIVATE_KEY);
        Set<Map.Entry<String, Object>> entry = map.entrySet();
        for (Map.Entry<String, Object> s : entry) {
            yoprequest.addParam(s.getKey(), s.getValue());
        }
        log.info("yoprequest:{}",yoprequest.getParams());

        //向YOP发请求
        YopResponse yopresponse = YopClient3.postRsa(url, yoprequest);

        log.info("请求YOP之后的结果:{}",yopresponse.getStringResult());
//        	对结果进行处理
        if ("FAILURE".equals(yopresponse.getState())) {
            if (yopresponse.getError() != null){
                result.put("errorcode", yopresponse.getError().getCode());
                result.put("errormsg", yopresponse.getError().getMessage());
                log.info("错误明细:{}",yopresponse.getError().getSubMessage());
                log.info("系统处理异常结果:{}",result);
                return result;
            }
        }

        //成功则进行相关处理
        if (yopresponse.getStringResult() != null) {
            result = parseResponse(yopresponse.getStringResult());
        }

        return result;
    }
}
