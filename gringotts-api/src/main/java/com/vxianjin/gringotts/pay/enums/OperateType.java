package com.vxianjin.gringotts.pay.enums;

/**
 * @Author: chenkai
 * @Date: 2018/8/21 15:03
 * @Description:
 */
public enum OperateType {
    RENEWAL("RENEWAL","续期"),
    REPAYL("REPAYL","还款详情"),
    BORROW("BORROW","借款");

    OperateType(String code, String message) {
        this.code = code;
        this.message = message;
    }

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

    public OperateType getByCode(String code){
        return OperateType.valueOf(code);
    }
}
