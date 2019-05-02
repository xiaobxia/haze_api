package com.vxianjin.gringotts.pay.model;

/**
 * 易宝支付续期回调请求内容
 * Created by jintian on 2018/7/17.
 */
public class YeepayContinueBackReq {
    /**
     * 请求内容
     */
    private String data;

    /**
     * 验证信息
     */
    private String encryptkey;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEncryptkey() {
        return encryptkey;
    }

    public void setEncryptkey(String encryptkey) {
        this.encryptkey = encryptkey;
    }
}
