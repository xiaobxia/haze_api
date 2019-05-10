package com.vxianjin.gringotts.pay.model.fuiou;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "RESPONSE")
public class NewProtocolResponse {

    @XNode("VERSION")
    private String version;

    @XNode("RESPONSECODE")
    private String responseCode;

    @XNode("RESPONSEMSG")
    private String responseMsg;

    @XNode("PROTOCOLNO")
    private String protocolNo;

    @XNode("MCHNTSSN")
    private String mchntssn;

    @XNode("MCHNTORDERID")
    private String mchntorderid;

    @XNode("AMT")
    private String amt;

    @XNode("ORDERDATE")
    private String orderdate;

    @XNode("BANKCARD")
    private String bankcard;

    @XNode("SIGN")
    private String sign;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getMchntssn() {
        return mchntssn;
    }

    public void setMchntssn(String mchntssn) {
        this.mchntssn = mchntssn;
    }

    public String getMchntorderid() {
        return mchntorderid;
    }

    public void setMchntorderid(String mchntorderid) {
        this.mchntorderid = mchntorderid;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "NewProtocolResponse{" +
                "version='" + version + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", protocolNo='" + protocolNo + '\'' +
                ", mchntssn='" + mchntssn + '\'' +
                ", mchntorderid='" + mchntorderid + '\'' +
                ", amt='" + amt + '\'' +
                ", orderdate='" + orderdate + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
