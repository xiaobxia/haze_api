package com.vxianjin.gringotts.exception;

import java.io.Serializable;

/**
 * @Author: kiro
 * @Date: 2018/7/2
 * @Description:
 */
public class DarkCloudException extends Exception implements Serializable {

    public DarkCloudException() {
    }

    public DarkCloudException(String message) {
        super(message);
    }

    public DarkCloudException(String message, Throwable cause) {
        super(message, cause);
    }

    public DarkCloudException(Throwable cause) {
        super(cause);
    }

    public DarkCloudException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
