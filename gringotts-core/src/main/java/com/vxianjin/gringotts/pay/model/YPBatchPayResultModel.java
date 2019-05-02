package com.vxianjin.gringotts.pay.model;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 11:23
 * @Description: 易宝支付打款批次明细查询结果
 */
public class YPBatchPayResultModel {

    //本下级机构编号
    private String merId;
    //订单号
    private String orderId;
    //打款状态码
    private String rlCode;
    //银行状态
    private String bankStatus;
    //发起时间
    private String requestDate;
    //收款人姓名
    private String payeeName;
    //开户行
    private String payeeBankName;
    //收款账户
    private String payeeBankAccount;
    //金额
    private String amount;
    //留言
    private String note;

    //手续费
    private String fee;

    //实付金额
    private String realPayAmount;
    //处理时间
    private String completeDate;
    //退款时间
    private String refundDate;

    //失败原因
    private String failDesc;

    //摘要
    private String abstractInfo;
    //备注
    private String remarksInfo;

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRlCode() {
        return rlCode;
    }

    public void setRlCode(String rlCode) {
        this.rlCode = rlCode;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeBankAccount() {
        return payeeBankAccount;
    }

    public void setPayeeBankAccount(String payeeBankAccount) {
        this.payeeBankAccount = payeeBankAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(String realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    public String getFailDesc() {
        return failDesc;
    }

    public void setFailDesc(String failDesc) {
        this.failDesc = failDesc;
    }

    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }

    public String getRemarksInfo() {
        return remarksInfo;
    }

    public void setRemarksInfo(String remarksInfo) {
        this.remarksInfo = remarksInfo;
    }
}
