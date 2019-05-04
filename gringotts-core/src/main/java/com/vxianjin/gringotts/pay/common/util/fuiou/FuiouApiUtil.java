package com.vxianjin.gringotts.pay.common.util.fuiou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.util.EncryUtil;
import com.vxianjin.gringotts.pay.common.util.RSA;
import com.vxianjin.gringotts.pay.common.util.RandomUtil;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolResponse;
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
public class FuiouApiUtil {

    private final static Logger logger = LoggerFactory.getLogger(FuiouApiUtil.class);
    /**
     * 格式化字符串
     */
    public static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }

    /**
     * 易宝支付代扣相关
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
        res = DESCoderFUIOU.desDecrypt(res,DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

        NewProtocolResponse response = XMapUtil.parseStr2Obj(NewProtocolResponse.class, res);

        logger.info("请求YOP之后结果：{}",response.toString());
        // 对结果进行处理
        if (!"0000".equals(response.getResponseCode())) {
            if (response.getResponseMsg() != null){
                result.put("errorcode", response.getResponseCode());
                result.put("errormsg", response.getResponseMsg());
                logger.info("错误明细:{}",response.getResponseMsg());
                logger.info("系统处理异常结果:{}",result);
                return result;
            }
        }
        // 成功则进行相关处理
        if ("0000".equals(response.getResponseCode())) {
            result.put("status", "0000");
            logger.info("response.getResult:{}",result);
        }
        return result;
    }

}
