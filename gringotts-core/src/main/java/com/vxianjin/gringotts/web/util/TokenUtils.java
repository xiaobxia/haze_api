package com.vxianjin.gringotts.web.util;

import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.web.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @类说明：app自动登录token相关工具类
 * @类 名：TokenUtils.java
 * @作 者：李江华
 * @创建日期：2015年7月30日 下午6:36:16
 */
public class TokenUtils {
    public static final Integer effSecond = 3 * 24 * 60 * 60;//app保持登录有效天数
    private static final Integer effDay = 3;//app保持登录有效天数
    private static final String password = "0123456789lbsoft";
    private static Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @方法说明：token自动生成工具方法 token = des(userId###userAccount###userPassword###登录时间###有效时间);
     * @方法作者：李江华
     * @创建日期：2015年7月30日 下午6:37:18
     * @返回类型：String
     */
    public static String generateToken(User user) {

        String userId = user.getId();
        String userName = user.getUserName();
        //String userPassword = user.getUserPassword();
        //Date userLogintime = user.getUserLogintime();

        //待加密明文
        StringBuffer mingwen = new StringBuffer();

        mingwen.append(userId).append("###").append(userName);
/*			.append("###").append(userPassword)
            .append("###").append(format.format(userLogintime));*/

        try {

            //String encryptDES = DesUtil.encryptDES(mingwen.toString(), PayConstants.DES_PUBLIC_KEY_IOS_ANDROID);

            String encryptDES = AESUtil.encrypt(mingwen.toString(), password);
            return encryptDES;

        } catch (Exception e) {

            logger.error("生成密文token出错，错误信息为：" + e.getMessage());
        }
        return null;
    }

    /**
     * token有效时间
     *
     * @param userLogintime 当前登录时间
     * @return
     */
    public static Date getEffTime(Date userLogintime) {
        Date effTime = DateUtil.addDay(userLogintime, effDay);
        return effTime;
    }

    /**
     * @方法说明：解密token
     * @返回类型：Map<String,String> 具体为：
     * userId:userId;
     * userAccount:userAccount;
     * @方法作者：李江华
     * @创建日期：2015年7月30日 下午7:03:28
     */
    public static Map<String, Object> decryptToken(String token) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            if (token != null && StringUtils.isNotEmpty(token)) {
                String mingwen = new String(AESUtil.decrypt(StringUtils.trimToEmpty(token), password)).trim();

                String[] split = mingwen.split("###");
                //userId###userAccount###userPassword###登录时间###有效时间
                result.put("userId", split[0]);
                result.put("userAccount", split[1]);
/*				result.put("userPassword", split[2]);
                result.put("loginTime", format.parse(split[3]));*/

                return result;
            }
        } catch (Exception e) {
            logger.error("解密密文token出错，错误信息为：" + e.getMessage());
        }

        return null;
    }

}
