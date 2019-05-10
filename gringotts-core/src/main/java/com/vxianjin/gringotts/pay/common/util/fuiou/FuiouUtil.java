package com.vxianjin.gringotts.pay.common.util.fuiou;

import com.fuiou.mpay.encrypt.RSAUtils;
import com.fuiou.util.MD5;

import java.util.ArrayList;
import java.util.List;

public class FuiouUtil {

    public static List<String> XYCodeListSuc() {
        return new ArrayList<String>(){{
            add("0000");
            add("P000");
            add("51B3");
        }};
    }

    /**
     * 获取签名
     * @param signStr  签名串
     * @param signtp   签名类型
     * @param key      密钥
     * @return
     * @throws Exception
     */
    public static String getSign(String signStr,String signtp,String key) throws  Exception{
        String sign = "";
        if ("md5".equalsIgnoreCase(signtp)) {
            sign = MD5.MD5Encode(signStr);
        } else {
            sign =	RSAUtils.sign(signStr.getBytes("utf-8"), key);
        }
        return sign;
    }

}
