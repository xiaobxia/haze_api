package com.vxianjin.gringotts.base;

import com.vxianjin.gringotts.exception.ResultException;

/**
 * @Author: kiro
 * @Date: 2018/7/2
 * @Description:
 */
public enum ResultType {
    SUCCESS(ResultException.SUCCESS, "成功");

    private int code;
    private String msg;

    ResultType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
