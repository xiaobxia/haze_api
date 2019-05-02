package com.vxianjin.gringotts.web.pojo.risk;

/**
 * 描述:
 * 强风控请求参数
 *
 * @author zed
 * @since 2019-02-02 11:43 AM
 */
public class RiskInfo {
    /**
     * 渠道名称
     */
    private String channel;
    /**
     * callback url
     */
    private String notifyUrl;
    /**
     * 场景
     */
    private String scene;
    /**
     * 附加
     */
    private String reqExt;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getReqExt() {
        return reqExt;
    }

    public void setReqExt(String reqExt) {
        this.reqExt = reqExt;
    }
}

