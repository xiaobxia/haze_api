package com.vxianjin.gringotts.risk.utils.yx;

public class YxSend {
    private String tx = "202";
    private String version = "v3";
    private YxSendData data;

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public YxSendData getData() {
        return data;
    }

    public void setData(YxSendData data) {
        this.data = data;
    }

}
