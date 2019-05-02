package com.vxianjin.gringotts.constant;

import java.util.ResourceBundle;

/**
 * 读取配置文件
 *
 * @author gaoyuhai
 */
@Deprecated
public class ConfigConstant {

    private static ResourceBundle resourceBundle;

    private static ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("config");
        }
        return resourceBundle;
    }

    public static String getConstant(String key) {
        return getConstant(key, null);
    }

    public static String getConstant(String key, String defaultValue) {
        String value;
        try {
            value = getResourceBundle().getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
        return value;
    }

}
