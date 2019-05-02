package com.vxianjin.gringotts.pay.model;

/**
 * 易宝主动还款短信重发请求
 * Created by jintian on 2018/7/17.
 */
public class YeepayRepaySmsReq {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 绑卡请求编号
     */
    private String request_no;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequest_no() {
        return request_no;
    }

    public void setRequest_no(String request_no) {
        this.request_no = request_no;
    }
}
