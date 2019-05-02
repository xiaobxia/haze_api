package com.vxianjin.gringotts.util.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @Author: kiro
 * @Date: 2018/7/9
 * @Description:
 */
public class Printer {
    private static Logger logger = LoggerFactory.getLogger(Printer.class);

    public static <K, V> void print(String tag, Map<K, V> map) {
        logger.info("[" + tag + "] +");
        Set<Map.Entry<K, V>> set = map.entrySet();
        for (Map.Entry<K, V> entry : set) {
            logger.info("[" + tag + "]  {" + entry.getKey() + ":" + entry.getValue() + "}");
        }
        logger.info("[" + tag + "] -");
    }
}
