package com.vxianjin.gringotts.constant;

import java.util.ResourceBundle;

/**
 * @author dongyukai 2017-09-28
 * 从back.utils包中迁到web.utils包
 */
//@Deprecated
public class SmsConfigConstant {

    public static ResourceBundle resourceBundle;

    public static void initConfig() {
        resourceBundle = ResourceBundle.getBundle("sms");
    }

    public static String getConstant(String key) {
        if (resourceBundle == null) {
            initConfig();
        }
        String value = "";
        try {
            value = new String(resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {

        }
        return value;

    }

    public static String getConstant(String key, String defaultValue) {
        String value = null;
        try {
            if (resourceBundle == null) initConfig();
            value = resourceBundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
        return value;
    }

}

