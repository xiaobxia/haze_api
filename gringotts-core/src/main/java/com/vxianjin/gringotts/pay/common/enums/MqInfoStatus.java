package com.vxianjin.gringotts.pay.common.enums;

/**
 * mq信息状态
 *
 * @author jintian
 * @date 13:58
 */
public enum MqInfoStatus {

    PUSHED("pushed", "已推送"),
    SUCCESS_CALLBACK("success", "已接受到成功返回"),
    FAIL_CALLBACK("fail", "接受到错误返回");

    MqInfoStatus(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private String status;

    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
