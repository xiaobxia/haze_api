package com.vxianjin.gringotts.pay.common.util.chanpay;

import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.vxianjin.gringotts.pay.common.util.bill99.CCSUtil;
import com.vxianjin.gringotts.pay.common.util.bill99.KQRSA;
import com.vxianjin.gringotts.pay.model.bill99.Pay2bankResponse;
import com.vxianjin.gringotts.pay.model.bill99.ResponseBody;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import com.vxianjin.gringotts.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bill99Util {

    private static final Logger logger = LoggerFactory.getLogger(Bill99Util.class);

    //字符编码
    private static String encoding = "UTF-8";

    //服务端URL
    private static String URL = PropertiesConfigUtil.get("BILL99_URL");
    //测试账户信息
    private static String membercode = PropertiesConfigUtil.get("BILL99_MEMBERCODE");

    private static String cerPath = PropertiesConfigUtil.get("BILL99_CERPATH");

    private static String pfxPath = PropertiesConfigUtil.get("BILL99_PFXPATH");

    private static String keyStorePass = PropertiesConfigUtil.get("BILL99_KEYSTOREPASS");

    public static Map<String, String> getDrawParams() {
        return new HashMap(){{
            put("account", membercode);
            put("cerPath", cerPath);
            put("pfxPath", pfxPath);
            put("keyStorePass", keyStorePass);
        }};
    }

    public static ResponseBody unsealMsg(Map<String, String> params) throws Exception {
        Map<String, String> drawParams = getDrawParams();

        String sign = getSign(drawParams, params, "pay2BankRequest",
                "pay2bankHead", "pay2BankOrder");
        logger.info("快钱加密请求报文 = {}", sign);
        String sealMsg = invokeCSSCollection(sign);

        logger.info("快钱加密返回报文 = {}", sealMsg);
        Pay2bankResponse response = CCSUtil.converyToJavaBean(sealMsg, Pay2bankResponse.class);
        return response.getResponseBody();
    }

    private static String invokeCSSCollection(String requestXml) throws Exception {
        //初始化HttpClient
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(URL);
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);
        SSLContext.setDefault(sslContext);
        // url的连接等待超时时间设置
        client.getHttpConnectionManager().getParams().setConnectionTimeout(2000);

        // 读取数据超时时间设置
        client.getHttpConnectionManager().getParams().setSoTimeout(3000);
        method.setRequestEntity(new StringRequestEntity(requestXml, "text/html", "utf-8"));
        client.executeMethod(method);

        //打印服务器返回的状态
        System.out.println(method.getStatusLine());

        //打印返回的信息
        InputStream stream = method.getResponseBodyAsStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream,encoding));
        StringBuffer buf = new StringBuffer();
        String line;
        while (null != (line = br.readLine())) {
            buf.append(line).append("\n");
        }
        //释放连接
        method.releaseConnection();
        return buf.toString();
    }

    public static String getSign(Map<String, String> drawParams, Map<String, String> m, String reqTag, String headTag,
                                 String paramTag) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>").append(
                "<").append(reqTag).append("><").append(headTag).append("><version>1.0</version><memberCode>");
        sb.append(drawParams.get("account")).append("</memberCode></").append(headTag).append
                ("><requestBody><sealDataType>");
        String orderXml = getOrderXml(m, paramTag);
        logger.info("快钱代付:" + orderXml);
        createSealDataType(drawParams, sb, orderXml);
        sb.append("</sealDataType></requestBody></").append(reqTag).append(">");
        return sb.toString();
    }

    private static String getOrderXml(Map<String, String> params, String tag) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><").append(tag).append(">");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry
                    .getKey()).append(">");
        }
        sb.append("</").append(tag).append(">");
        return sb.toString();
    }

    public static void createSealDataType(Map<String, String> drawParams, StringBuffer sb, String orderXml) {
        byte[] data = new byte[0];
        try {
            data = orderXml.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] secretKey = KQRSA.generateSecretKey();
        byte[] encryptedData = KQRSA.encrypt(KQRSA.symmetricAlgorithm, data, secretKey);
        byte[] digitalEnvelope = KQRSA.encryptSecretKey(KQRSA.asymmetricAlgorithm, secretKey, KQRSA.getPubKey
                (drawParams.get("cerPath")));
        byte[] signedData = KQRSA.sign(data, KQRSA.getPriKey(drawParams.get("pfxPath"), drawParams.get
                ("keyStorePass"), drawParams.get("keyStorePass")));
        Map<String, String> params = new LinkedHashMap<>();
        params.put("originalData", "");
        params.put("signedData", byte2UTF8StringWithBase64(signedData));
        params.put("encryptedData", byte2UTF8StringWithBase64(encryptedData));
        params.put("digitalEnvelope", byte2UTF8StringWithBase64(digitalEnvelope));
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry
                    .getKey()).append(">");
        }
    }

    public static String byte2UTF8StringWithBase64(byte[] bytes) {
        try {
            return new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.info("byte2UTF8String exception", e);
            return "";
        }
    }

    public static String genRequestXml(HttpServletRequest httpRequest) {
        String line;
        ServletInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            is = httpRequest.getInputStream();
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.info("genRequestXml exception");
        } finally {
            try {
                if (null != is) is.close();
                if (null != isr) isr.close();
                if (null != br) br.close();
            } catch (Exception e) {
                logger.info("io close exception");
            }
        }

        return sb.toString();
    }

    public static Map<String, String> xmlToMap(String data, String... params) {
        Map<String, String> m = new HashMap<>();
        try {
            for (String key : params) {
                if (!StringUtils.isEmpty(key)) {
                    m.put(key, StringUtils.getTagText(data, key));
                }
            }
        } catch (Exception e) {
            logger.info("xmlToMap error: {}", e.getMessage());
        }
        return m;
    }



}
