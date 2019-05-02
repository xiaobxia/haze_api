package com.vxianjin.gringotts.web.pojo.risk;

/**
 * 描述:
 * 强风控结果
 *
 * @author zed
 * @since 2019-02-11 10:43 PM
 */
public class StrongRiskResult {
    private String userId;
    /**
     * 识别号
     */
    private String consumerNo;
    /**
     * 结果
     */
    private String result;
    /**
     * 额度
     */
    private String amount;
    /**
     * 订单号
     */
    private String orderNo;

    private String riskStatusType;
    private String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRiskStatusType() {
        return riskStatusType;
    }

    public void setRiskStatusType(String riskStatusType) {
        this.riskStatusType = riskStatusType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

