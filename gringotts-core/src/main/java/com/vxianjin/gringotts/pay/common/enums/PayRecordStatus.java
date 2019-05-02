package com.vxianjin.gringotts.pay.common.enums;

/**
 * @Author: chenkai
 * @Date: 2018/7/19 16:28
 * @Description:
 */
public enum PayRecordStatus {

    ACCEPT("ACCEPT","已接收"),
    TO_VALIDATE("TO_VALIDATE","待短信确认"),
    PAY_FAIL("PAY_FAIL","支付失败"),
    PROCESSING("PROCESSING","处理中"),
    PAY_SUCCESS("PAY_SUCCESS","支付成功"),
    TIME_OUT("TIME_OUT","超时失败"),
    FAIL("FAIL","系统异常"),
    ;

    PayRecordStatus(String code, String message) {
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

    public static PayRecordStatus getByCode(String code){
        return PayRecordStatus.valueOf(code);
    }
}
