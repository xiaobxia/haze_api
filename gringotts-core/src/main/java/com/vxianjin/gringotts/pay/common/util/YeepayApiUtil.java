package com.vxianjin.gringotts.pay.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *  易宝API请求工具类
 * @author : chenkai
 * @date : 2018/7/19 16:39
 */
public class YeepayApiUtil {

    private final static Logger logger = LoggerFactory.getLogger(YeepayApiUtil.class);
    /**
     * 格式化字符串
     */
    public static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }

    /**
     * 实例化公钥
     * @return public key
     */
    public static PublicKey getPubKey() {
        PublicKey publicKey = null;
        try {
            // 直接写死 config json里面的public key
            // 自己的公钥(测试)
            String publickey =PayConstants.YEEPAY_PUBLIC_KEY;
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(publickey));
            // RSA对称加密算法
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            logger.error("getPubKey error:{}",e);
        } catch (InvalidKeySpecException e) {
            logger.error("getPubKey error:{}",e);
        } catch (IOException e) {
            logger.error("getPubKey error:{}",e);
        }
        return publicKey;
    }
    /**
     * 实例化私钥
     *
     * @return private key
     */
    public static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        String priKey = PayConstants.MERCHANT_PRIVATE_KEY;
        PKCS8EncodedKeySpec priPKCS8;
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(priKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (IOException e) {
            logger.error("getPrivateKey error:{}",e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("getPrivateKey error:{}",e);
        } catch (InvalidKeySpecException e) {
            logger.error("getPrivateKey error:{}",e);
        }
        return privateKey;
    }
    /**
     * 解析http请求返回
     */
    public static Map<String, Object> parseHttpResponseBody(int statusCode, String responseBody, String type, String userId) throws Exception {

        logger.info("YeepayService " + type + " userId=" + userId + " responseBody=" + responseBody);

        String merchantPrivateKey = PayConstants.MERCHANT_PRIVATE_KEY;
        String yeepayPublicKey = PayConstants.YEEPAY_PUBLIC_KEY;

        Map<String, Object> result = new HashMap<>();

        if (statusCode != 200) {
            result.put("errorcode", statusCode);
            result.put("errormsg", "系统异常");
            return result;
        }
        //解析响应结果
        Map<String, Object> jsonMap = JSON.parseObject(responseBody,
                new TypeReference<TreeMap<String, Object>>() {
                });

        if (jsonMap.containsKey("errorcode")) {
            result = jsonMap;
            return (result);
        }

        String dataFromYeepay = formatString(jsonMap.get("data").toString());
        String encryptkeyFromYeepay = formatString(jsonMap.get("encryptkey").toString());

        boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay,
                yeepayPublicKey, merchantPrivateKey);
        //验证签名是否正确
        if (!signMatch) {
            result.put("errorcode", "500");
            result.put("errormsg", "签名错误");
            return (result);
        }

        String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
        String decryptData = AESUtil.decryptFromBase64(dataFromYeepay, yeepayAESKey);

        logger.info("YeepayService " + type + " userId=" + userId + " data=" + decryptData);

        result = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, Object>>() {
        });

        return (result);
    }
    /**
     * 取得商户AESKey
     */
    public static String getMerchantAESKey() {
        return (RandomUtil.getRandom(16));
    }


//    public static Map<String,Object> httpExecuteResult(Map<String,Object> dataMap,String userId,String reqUrl,String interfaceName){
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod(reqUrl);
//
//        Map<String, Object> resultMap = new HashMap<>();
//        try {
//            String jsonStr = JSON.toJSONString(dataMap);
//            //商户AESKey
//            String merchantAESKey = getMerchantAESKey();
//            String data = AESUtil.encryptToBase64(jsonStr, merchantAESKey);
//            //易宝公玥
//            String encryptkey = RSA.encrypt(merchantAESKey, PayConstants.YEEPAY_PUBLIC_KEY);
//
//            logger.info("YeepayService "+interfaceName+" AES requestData=" + data);
//            logger.info("YeepayService "+interfaceName+" encryptkey=" + encryptkey);
//
//            NameValuePair[] datas = {new NameValuePair("merchantno", PayConstants.MERCHANT_NO),
//                    new NameValuePair("data", data),
//                    new NameValuePair("encryptkey", encryptkey)};
//
//            postMethod.setRequestBody(datas);
//
//            int statusCode = httpClient.executeMethod(postMethod);
//            byte[] responseByte = postMethod.getResponseBody();
//            String responseBody = new String(responseByte, "UTF-8");
//            logger.info("yeepay response :{}",responseBody);
//            resultMap = parseHttpResponseBody(statusCode, responseBody, "getYBRepayResult", userId);
//
//        } catch (Exception e) {
//            logger.error("YeepayService "+interfaceName+" error userId=" + userId, e);
//            e.printStackTrace();
//        } finally {
//            postMethod.releaseConnection();
//        }
//
//        return resultMap;
//    }

//    /**
//     * 解密支付回调参数requstStr
//     *
//     * @param requstStr str
//     * @return map
//     */
//    public static Map<String, String> decryptCallbackData(String requstStr) {
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(requstStr);
//
//            if (jsonObject == null) {
//                logger.error("decryptCallbackData error requstStr=null");
//                return null;
//            }
//
//            String data = jsonObject.getString("data");
//            String encryptkey = jsonObject.getString("encryptkey");
//
//            String merchantPrivateKey = PayConstants.MERCHANT_PRIVATE_KEY;
//            String yeepayPublicKey = PayConstants.YEEPAY_PUBLIC_KEY;
//            Map<String, String> callbackResult;
//
//            boolean signMatch = EncryUtil.checkDecryptAndSign(data, encryptkey, yeepayPublicKey, merchantPrivateKey);
//            //验签失败
//            if (!signMatch) {
//                return null;
//            }
//            String yeepayAESKey = RSA.decrypt(encryptkey, merchantPrivateKey);
//            String decryptData = AESUtil.decryptFromBase64(data, yeepayAESKey);
//            callbackResult = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {
//            });
//
//            return callbackResult;
//        } catch (Exception e) {
//            logger.error("decryptCallbackData error", e);
//        }
//
//        return null;
//    }

    /**
     * YOP 解析callback
     * @param str str
     * @return map
     */
    public static Map<String,String> getCallBackParamMap(String str){
        Map<String,String> callbackResult;
        // 请求内容解密成需要的数据
        try {
            //开始解密
            DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
            dto.setCipherText(str);
            PrivateKey privateKey = YeepayApiUtil.getPrivateKey();
            PublicKey publicKey = YeepayApiUtil.getPubKey();
            dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
            logger.info("payWithholdCallback result:{}",dto.getPlainText());
            callbackResult = YeepayApiUtil.parseResponse2(dto.getPlainText());
            return callbackResult;
        } catch (Exception e) {
            logger.error("解密失败:{}",e);
            throw new RuntimeException("回调解密失败！");
        }
    }
    public static Map<String,Object> getPayCallBackParamMap(String str){
        Map<String,Object> callbackResult;
        // 请求内容解密成需要的数据
        try {
            //开始解密
            DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
            dto.setCipherText(str);
            PrivateKey privateKey = YeepayApiUtil.getPrivateKey();
            PublicKey publicKey = YeepayApiUtil.getPubKey();
            dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
            logger.info("payWithholdCallback result:{}",dto.getPlainText());
            callbackResult = YeepayApiUtil.parseResponse(dto.getPlainText());
            return callbackResult;
        } catch (Exception e) {
            logger.error("解密失败:{}",e);
            throw new RuntimeException("回调解密失败！");
        }
    }

    /**
     * 易宝支付代扣相关
     * @param map map
     * @param uri uri
     * @return map
     * @throws IOException ex
     */
    public static Map<String, Object> yeepayYOP(Map<String, String> map, String uri) throws IOException {
        // 传三参的方式方法
        YopRequest yoprequest = new YopRequest("SQKK"+PayConstants.MERCHANT_NO, PayConstants.MERCHANT_PRIVATE_KEY);
        Map<String, Object> result = new HashMap<>();
        Set<Map.Entry<String, String>> entry = map.entrySet();
        for (Map.Entry<String, String> s : entry) {
            yoprequest.addParam(s.getKey(), s.getValue());
        }
        logger.info("yoprequest :{}",yoprequest.getParams());
        // 向YOP发请求
        YopResponse yopresponse = YopClient3.postRsa(uri, yoprequest);
        logger.info("请求YOP之后结果：{}",yopresponse.toString());
        logger.info("请求YOP之后结果：{}",yopresponse.getStringResult());
        // 对结果进行处理
        if ("FAILURE".equals(yopresponse.getState())) {
            if (yopresponse.getError() != null){
                result.put("errorcode", yopresponse.getError().getCode());
                result.put("errormsg", yopresponse.getError().getMessage());
                logger.info("错误明细:{}",yopresponse.getError().getSubMessage());
                logger.info("系统处理异常结果:{}",result);
                return result;
            }
        }
        // 成功则进行相关处理
        if (yopresponse.getStringResult() != null) {
            result = parseResponse(yopresponse.getStringResult());
            logger.info("yopresponse.getStringResult:{}",result);
        }
        return result;
    }
    /**
     * 将获取到的yopresponse转换成json格式
     * @param yopresponse res
     * @return map
     */
    public static Map<String, Object> parseResponse(String yopresponse) {

        Map<String, Object> jsonMap;
        jsonMap = JSON.parseObject(yopresponse, new TypeReference<TreeMap<String, Object>>() {
        });
        logger.info("将结果yopresponse转化为map格式之后: {}",jsonMap);
        return jsonMap;
    }
    // 将获取到的yopresponse转换成json格式
    public static Map<String, String> parseResponse2(String yopresponse) {

        Map<String, String> jsonMap;
        jsonMap = JSON.parseObject(yopresponse, new TypeReference<TreeMap<String, String>>() {
        });
        logger.info("将结果yopresponse转化为map格式之后: {}",jsonMap);
        return jsonMap;
    }


}
