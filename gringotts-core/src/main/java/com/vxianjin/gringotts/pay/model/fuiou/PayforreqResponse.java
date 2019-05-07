package com.vxianjin.gringotts.pay.model.fuiou;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "payforrsp")
public class PayforreqResponse {

    @XNode("ret")
    private String ret;

    @XNode("memo")
    private String memo;

    @XNode("transStatusDesc")
    private String transStatusDesc;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTransStatusDesc() {
        return transStatusDesc;
    }

    public void setTransStatusDesc(String transStatusDesc) {
        this.transStatusDesc = transStatusDesc;
    }
}
