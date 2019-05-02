package com.vxianjin.gringotts.util.properties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 读取config.properties配置
 *
 * @author LTQ
 */
//@Deprecated
public class PropertiesConfigUtil {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new InputStreamReader(PropertiesConfigUtil.class.getResourceAsStream("/config.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            value = value.trim();
        }
        return value;
    }
}
