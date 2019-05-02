package com.vxianjin.gringotts.pay.common.enums;

/**
 * @Author: chenkai
 * @Date: 2018/7/20 13:49
 * @Description: 错误返回基类
 */
public enum ErrorBase {

    SUCCESS("SUCCESS","成功"),
    FAIL("FAIL","失败");

    private String code;
    private String message;

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

    ErrorBase(String code, String message) {

        this.code = code;
        this.message = message;
    }
}
