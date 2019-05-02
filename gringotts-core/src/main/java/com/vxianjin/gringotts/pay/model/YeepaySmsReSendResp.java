package com.vxianjin.gringotts.pay.model;

/**
 * 易宝短信重发送返回内容
 * Created by jintian on 2018/7/18.
 */
public class YeepaySmsReSendResp {
    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    private String time;

    private String requestNo;

    public YeepaySmsReSendResp(String code, String message, String time, String requestNo) {
        this.code = code;
        this.message = message;
        this.time = time;
        this.requestNo = requestNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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
