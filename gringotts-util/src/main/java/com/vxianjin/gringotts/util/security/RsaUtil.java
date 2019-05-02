package com.vxianjin.gringotts.util.security;

import com.vxianjin.gringotts.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 *@Author Created by Wzw  
 *@Date on 2019/2/2 0002 10:52
 */

public class RsaUtil {


    private static final Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    /**
     * 默认的key生成方式，根据该参数灵活管理 rsa加密、解密、签名、验签
     */
    public static String KEY_GEN_STYLE = "openssl";

    /**
     * key生成方式 - openssl生成
     */
    private static final String KEY_GEN_STYLE_OPENSSL = "openssl";

    /**
     * key生成方式 - RSA.java类代码生成
     */
    private static final String KEY_GEN_STYLE__CODE = "code";


    /**
     * 生成私钥文件
     * @param privateKeyStr
     * @return
     */
    public static PrivateKey generatePrivateKey(String privateKeyStr) {
        try {
            byte[] buffer = keyToBytes(privateKeyStr, KEY_GEN_STYLE);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            return key;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * key转byte数组
     * @param keyStr
     * @param genStyle
     * @return
     */
    public static byte[] keyToBytes(String keyStr,  String genStyle) {
        if (KEY_GEN_STYLE_OPENSSL.equals(genStyle)) {
            return Base64Util.base64DecodeToArray(keyStr);
        } else if (KEY_GEN_STYLE__CODE.equals(genStyle)) {
            return hexString2ByteArr(keyStr);
        }
        return null;
    }




    /**
     * 十六进制字符串转换为字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2ByteArr(String hexString) {
        if ((hexString == null) || (hexString.length() % 2 != 0)) {
            return new byte[0];
        }

        byte[] dest = new byte[hexString.length() / 2];

        for (int i = 0; i < dest.length; i++) {
            String val = hexString.substring(2 * i, 2 * i + 2);
            dest[i] = (byte) Integer.parseInt(val, 16);
        }
        return dest;
    }

    /**
     * 私钥加密
     * @param privateKeyStr 私钥字符串
     * @param str 加密原串
     * @return
     * @throws Exception
     */
    public static String encrypt(String privateKeyStr, String str){
        PrivateKey privateKey = RsaUtil.generatePrivateKey(privateKeyStr);
        if (privateKey == null) {
            throw new RuntimeException("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(str.getBytes("UTF-8"));
            // byte数组base64编码后存在非法字符，所以需要再base64编码一次
            return Base64Util.base64Encode(Base64Util.base64Encode(output));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无此加密算法");
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("使用默认RSA异常,请检查");
        } catch (InvalidKeyException e) {
            throw new RuntimeException("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new RuntimeException("明文数据已损坏");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的编码");
        }
    }







}
