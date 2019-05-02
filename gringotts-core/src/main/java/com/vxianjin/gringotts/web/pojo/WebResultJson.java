package com.vxianjin.gringotts.web.pojo;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * ${DESCRIPTION}
 * Created by Kevin on 2016/10/13.
 */

public class WebResultJson<T> {

    private String code;
    private String msg;
    private boolean success;
    private T result;

    public WebResultJson(String code, String msg, boolean success, T result) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.result = result;
    }

    public static void main(String[] args) {
        //格式化 pmoney 两位
        DecimalFormat df = new DecimalFormat("#.0");
        BigDecimal bg = new BigDecimal("12345.567");
        System.out.println(df.format(new BigDecimal("12345.55").doubleValue()));
        System.out.println(String.format("%.2f", bg));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
