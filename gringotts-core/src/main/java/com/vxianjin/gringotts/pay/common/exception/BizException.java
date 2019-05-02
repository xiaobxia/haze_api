package com.vxianjin.gringotts.pay.common.exception;

import com.vxianjin.gringotts.pay.common.enums.ErrorCode;

/**
 * Created by jintian on 2018/7/17.
 */
public class BizException extends RuntimeException {
    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    public BizException(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMsg();
    }

    public BizException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
