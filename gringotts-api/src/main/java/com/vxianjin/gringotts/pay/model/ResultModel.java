package com.vxianjin.gringotts.pay.model;

import java.io.Serializable;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 14:35
 * @Description: 公共返回结构封装
 */
public class ResultModel<T> implements Serializable{

    private boolean succ;

    private String code;

    private String message;

    private T data;

    public ResultModel(boolean succ) {
        this.succ = succ;
    }

    public ResultModel(boolean succ, String code, String message) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
