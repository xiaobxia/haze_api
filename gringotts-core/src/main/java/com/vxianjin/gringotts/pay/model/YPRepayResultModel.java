package com.vxianjin.gringotts.pay.model;

/**
 * @Author: chenkai
 * @Date: 2018/7/19 15:55
 * @Description:
 */
public class YPRepayResultModel {
    //商户编号
    private String merchantNo;
    //充值请求号
    private String requestNo;

    //易宝流水号
    private String ybOrderId;

    //订单状态
    private String status;

    //金额
    private String amount;

    //卡号前六位
    private String cardTop;

    //卡号后六位
    private String cardLast;

    //银行编码
    private String bankCode;

    //签名
    private String sign;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getYbOrderId() {
        return ybOrderId;
    }

    public void setYbOrderId(String ybOrderId) {
        this.ybOrderId = ybOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardTop() {
        return cardTop;
    }

    public void setCardTop(String cardTop) {
        this.cardTop = cardTop;
    }

    public String getCardLast() {
        return cardLast;
    }

    public void setCardLast(String cardLast) {
        this.cardLast = cardLast;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
