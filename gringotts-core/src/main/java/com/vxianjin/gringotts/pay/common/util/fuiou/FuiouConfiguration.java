package com.vxianjin.gringotts.pay.common.util.fuiou;

import java.util.ResourceBundle;

public class FuiouConfiguration {

    private static final String CONFIG_FILE = "fuiou";
    private static Object lock = new Object();
    private static FuiouConfiguration config = null;
    private static ResourceBundle rb = null;

    private FuiouConfiguration() {
        rb = ResourceBundle.getBundle(CONFIG_FILE);
    }

    public static FuiouConfiguration getInstance() {
        synchronized (lock) {
            if (null == config) {
                config = new FuiouConfiguration();
            }
        }
        return (config);
    }

    public String getValue(String key) {
        return (rb.getString(key));
    }
}