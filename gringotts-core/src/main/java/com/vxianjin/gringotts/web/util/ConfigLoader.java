package com.vxianjin.gringotts.web.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <li>描述:配合文件加载类</li><br>
 * <li>This is about <code>ConfigLoader</code></li>
 *
 * @author hjy273@gmail.com
 * @version 1.0
 * @date 2017年5月12日 上午10:41:21
 */
//@Deprecated
public class ConfigLoader {

    private final static ConfigLoader configLoader = new ConfigLoader();
    private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
//    private PropertiesConfiguration configurationsQn;
    private PropertiesConfiguration configurationAli;

    private ConfigLoader() {
        try {
//            configurationsQn = new PropertiesConfiguration("qiniu-server.properties");
            configurationAli = new PropertiesConfiguration("aliyun-server.properties");
        } catch (ConfigurationException e) {
            logger.error("", e);
        }
    }

    public static ConfigLoader getInstance() {
        return configLoader;
    }

//    public PropertiesConfiguration getQnConfigurations() {
//        return configurationsQn;
//    }

    public PropertiesConfiguration getAliConfigurations() {
        return configurationAli;
    }
}
