package com.vxianjin.gringotts.pay.model.fuiou;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "RESPONSE")
public class NewProtocolOrderResponse {

    @XNode("VERSION")
    private String version;

    @XNode("TYPE")
    private String type;

    @XNode("RESPONSECODE")
    private String responseCode;

    @XNode("RESPONSEMSG")
    private String responseMsg;

    @XNode("MCHNTCD")
    private String mchntcd;

    @XNode("USERID")
    private String userId;

    @XNode("MCHNTORDERID")
    private String mchntOrderId;

    @XNode("ORDERID")
    private String orderId;

    @XNode("PROTOCOLNO")
    private String protocolNo;

    @XNode("BANKCARD")
    private String bankcard;

    @XNode("AMT")
    private String amt;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getMchntcd() {
        return mchntcd;
    }

    public void setMchntcd(String mchntcd) {
        this.mchntcd = mchntcd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMchntOrderId() {
        return mchntOrderId;
    }

    public void setMchntOrderId(String mchntOrderId) {
        this.mchntOrderId = mchntOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "NewProtocolOrderResponse{" +
                "version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", mchntcd='" + mchntcd + '\'' +
                ", userId='" + userId + '\'' +
                ", mchntOrderId='" + mchntOrderId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", protocolNo='" + protocolNo + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}
