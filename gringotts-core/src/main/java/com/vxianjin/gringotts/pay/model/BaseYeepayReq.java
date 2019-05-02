package com.vxianjin.gringotts.pay.model;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 10:34
 * @Description: 易宝公共请求参数
 */
public class BaseYeepayReq {
    //命令
    private String cmd;
    //接口版本
    private String version;
    //交易商户编号
    private String merId;
    //签名信息
    private String hmac;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
}
