package com.vxianjin.gringotts.pay.common.util.fuiou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.enums.PayRecordStatus;
import com.vxianjin.gringotts.pay.common.util.EncryUtil;
import com.vxianjin.gringotts.pay.common.util.RSA;
import com.vxianjin.gringotts.pay.common.util.RandomUtil;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolOrderResponse;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolResponse;
import com.vxianjin.gringotts.pay.model.fuiou.PayforreqResponse;
import com.vxianjin.gringotts.util.MapUtils;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 *  易宝API请求工具类
 * @author : chenkai
 * @date : 2018/7/19 16:39
 */
public class FuiouApiUtil {

    private final static Logger logger = LoggerFactory.getLogger(FuiouApiUtil.class);
    /**
     * 格式化字符串
     */
    public static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }

    /**
     * fuiou首笔验相关
     * @param APIFMS 对象转换后的XML字符串
     * @param uri uri
     * @return map
     * @throws IOException ex
     */
    public static Map<String, Object> FuiouYOP(String APIFMS, String uri) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String,String> map = new HashMap<String, String>();
        APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));
        map.put("MCHNTCD",FuiouConstants.API_MCHNT_CD);
        map.put("APIFMS", APIFMS);
        String res = new HttpPoster(uri).postStr(map);
        if (!uri.contains(FuiouConstants.NEW_PROTOCOL_CHECKRESULT_URL)) {
            res = DESCoderFUIOU.desDecrypt(res,DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));
        }


        NewProtocolResponse response = XMapUtil.parseStr2Obj(NewProtocolResponse.class, res);

        logger.info("请求FUIOU-YOP之后结果：{}",response.toString());
        // 对结果进行处理
        if (!FuiouUtil.XYCodeListSuc().contains(response.getResponseCode())) {
            if (response.getResponseMsg() != null){
                logger.info("错误明细:{}",response.getResponseMsg());
                result.put("payStatus", PayRecordStatus.PAY_FAIL);
                result.put("errorcode", response.getResponseCode());
                result.put("errormsg", response.getResponseMsg());
                logger.info("系统处理异常结果:{}",result);
                return result;
            }
        } else {
            // 成功则进行相关处理
            if ("0000".equals(response.getResponseCode())) {
                result.put("status", response.getResponseCode());
                //result.put("errorcode", response.getResponseCode());
                result.put("payStatus", PayRecordStatus.PAY_SUCCESS);
                result.put("errormsg", response.getResponseMsg());
                result.put("agreeno", response.getProtocolNo());
                result.put("mchntorderId", response.getMchntorderid());
                logger.info("response.getResult:{}",result);
            }
        }

        return result;
    }

    /**
     * fuiou支付相关
     * @param APIFMS 对象转换后的XML字符串
     * @param uri uri
     * @return map
     * @throws IOException ex
     */
    public static Map<String, Object> FuiouOD(String APIFMS, String uri) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String,String> map = new HashMap<String, String>();
            APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));
            map.put("MCHNTCD",FuiouConstants.API_MCHNT_CD);
            map.put("APIFMS", APIFMS);
            String res = new HttpPoster(uri).postStr(map);

            res = DESCoderFUIOU.desDecrypt(res,DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

            NewProtocolOrderResponse response = XMapUtil.parseStr2Obj(NewProtocolOrderResponse.class, res);

            logger.info("请求FUIOU-OD之后结果：{}",response.toString());
            // 对结果进行处理
            if (!"0000".equals(response.getResponseCode()) && !"P000".equals(response.getResponseCode())) {
                if (response.getResponseMsg() != null){
                    result.put("errorcode", response.getResponseCode());
                    result.put("errormsg", response.getResponseMsg());
                    logger.info("错误明细:{}",response.getResponseMsg());
                    logger.info("系统处理异常结果:{}",result);
                    return result;
                }
            }
            // 成功则进行相关处理
            if ("P000".equals(response.getResponseCode())) {//支付处理中
                result.put("status", "P000");
                result.put("fuiouOrderId", response.getOrderId());
                result.put("requestNo", response.getMchntOrderId());
                logger.info("response.getResult:{}",result);
            }

            if ("0000".equals(response.getResponseCode())) {//直接成功，不用等待回调
                result.put("status", "0000");
                result.put("fuiouOrderId", response.getOrderId());
                result.put("requestNo", response.getMchntOrderId());
                logger.info("response.getResult:{}",result);
            }
        } catch (Exception e) {
            logger.info("FuiouOD. error:{}, errormsg:{}",result, e);
        }
        return result;
    }

    /**
     * fuiou代收付相关
     * @param map
     * @param uri
     * @return
     * @throws Exception
     */
    public static Map<String, Object> FuiouPS(Map<String,String> map, String uri) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String res = HttpPoster.requestPost(uri, map);
        //res = DESCoderFUIOU.desDecrypt(res,DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

        PayforreqResponse response = XMapUtil.parseStr2Obj(PayforreqResponse.class, res);

        logger.info("请求FUIOU-PS之后结果：{}",response.toString());
        // 对结果进行处理
        if (!"000000".equals(response.getRet()) && !"0000".equals(response.getRet())) {
            if (response.getMemo() != null){
                result.put("errorcode", response.getRet());
                result.put("errormsg", response.getMemo());
                logger.info("错误明细:{}",response.getMemo());
                logger.info("系统处理异常结果:{}",result);
                return result;
            }
        }
        // 成功则进行相关处理
        if ("0000".equals(response.getRet()) || "000000".equals(response.getRet())) {
            result.put("status", "0000");
            result.put("transferStatusCode", response.getRet());
            result.put("agreeno", response.getMemo());
            logger.info("response.getResult:{}",result);
        }
        return result;
    }

}
