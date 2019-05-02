package com.vxianjin.gringotts.pay.common.exception;

/**
 * @Author: chenkai
 * @Date: 2018/7/17 14:25
 * @Description:
 */
public class PayException extends RuntimeException{

    private String coed;

    public PayException(String message) {
        super(message);
    }

    public PayException(String coed, String message) {
        super(message);
        this.coed = coed;
    }

    public PayException(String message, Throwable cause, String coed, String message1) {
        super(message, cause);
        this.coed = coed;
    }

    public PayException(Throwable cause, String coed, String message) {
        super(message, cause);
        this.coed = coed;
    }

    public PayException(String coed, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.coed = coed;
    }
}
