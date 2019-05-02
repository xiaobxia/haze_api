package com.vxianjin.gringotts.pay.common.util;


import com.vxianjin.gringotts.constant.Commons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertiesUtil {

    /**
     * 日志打印
     */
    public static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 通过properties文件的key值获取属性值 功能详细描述
     *
     * @param key
     * @param path
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getValueByKey(String key, String path) {
        return readProperties(path).getProperty(key);
    }

    /**
     * 加载properties配置文件
     *
     * @param fileName
     */
    private static Properties readProperties(String fileName) {
        try {
            fileName = fileName.concat(".properties");
            Properties props = new Properties();

            String path = PropertiesUtil.class.getResource(
                    "/").getPath().concat(fileName);

            path = URLDecoder.decode(path, Commons.DEFAULTCHARSET);
            File file = new File(path);
            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(file), "UTF-8");
            props.load(inputStream);
            return props;
        } catch (Exception e) {
            LOGGER.error("lodding " + fileName + " properties failed!", e);
        }
        return null;
    }

    /**
     * 拼接自源目录下的配置文件路径
     *
     * @param folderName
     * @param configName
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getConfigPath(String folderName, String configName) {
        return folderName + "/" + configName;
    }

    /**
     * 得到所有的配置信息map集合
     *
     * @return
     */
    public static Map<?, ?> getAll(String path) {
        Map<String, String> map = new HashMap<String, String>();
        Properties props = readProperties(path);
        Enumeration<?> enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
}
