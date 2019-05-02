package com.vxianjin.gringotts.pay.common.enums;

/**
 * @Author: chenkai
 * @Date: 2018/7/16 16:31
 * @Description:
 */
public enum  EventTypeEnum {

    PAY("PAY","代付"),
    REPAY("REPAY","代扣"),
    ;

    EventTypeEnum(String code, String message) {
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

    public EventTypeEnum getByCode(String code){
        return EventTypeEnum.valueOf(code);
    }
}
