package com.vxianjin.gringotts.exception;

import com.vxianjin.gringotts.base.ResultType;

/**
 * @Author: kiro
 * @Date: 2018/7/2
 * @Description:
 */
public class ResultException extends DarkCloudException {

    public static final int SUCCESS = 200;
    private int code;

    public ResultException(ResultType resultType) {
        super(resultType.getMsg());
        this.code = resultType.getCode();
    }

    public ResultException(int code) {
        this(code, null);
    }

    public ResultException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MsgException{" +
                "code=" + code +
                '}';
    }
}
