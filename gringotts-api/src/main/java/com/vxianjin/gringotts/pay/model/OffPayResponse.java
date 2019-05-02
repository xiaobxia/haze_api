package com.vxianjin.gringotts.pay.model;

import java.io.Serializable;

/**
 * @author jintian
 * @date 14:24
 */
public class OffPayResponse implements Serializable{
    private static final long serialVersionUID = 5721431609543586233L;
    
    private boolean succ;

    private String code;

    private String message;

    public OffPayResponse(boolean succ) {
        this.succ = succ;
    }

    public OffPayResponse(boolean succ, String code, String message) {
        this.succ = succ;
        this.code = code;
        this.message = message;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
