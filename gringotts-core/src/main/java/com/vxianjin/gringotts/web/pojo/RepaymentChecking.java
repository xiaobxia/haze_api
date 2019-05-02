package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RepaymentChecking implements Serializable {
    //  1、支付宝、2、银行卡主动还款、3、银行卡自动扣款、4、对公银行卡转账、5、线下还款
    public static final int REPAYMENT_STATUS_SUSS = 2;
    public static final int REPAYMENT_STATUS_SUSS_1 = 30;
    public static final int REPAYMENT_STATUS_SUSS_CHANGE = 5;

    //退款
    public static final int BACK_REPAY_STATUS_LIAN = 13;
    public static final int BACK_REPAY_STATUS_ALIPAY = 16;

    public static final Map<Integer, String> REPAYMENT_STATUS = new HashMap<Integer, String>();  //还款
    public static final Map<Integer, String> BACK_REPAY_STATUS = new HashMap<Integer, String>();  //退款
    private static final long serialVersionUID = -7543237353884723449L;

    static {
        REPAYMENT_STATUS.put(REPAYMENT_STATUS_SUSS, "还款成功");
        REPAYMENT_STATUS.put(REPAYMENT_STATUS_SUSS_1, "还款成功");
        REPAYMENT_STATUS.put(REPAYMENT_STATUS_SUSS_CHANGE, "已还款,未确认退款");

        BACK_REPAY_STATUS.put(BACK_REPAY_STATUS_LIAN, "连连退款");
        BACK_REPAY_STATUS.put(BACK_REPAY_STATUS_ALIPAY, "支付宝退款");
    }


    private String userId;  //用户ID
    private String realname;  //用户姓名
    private String phone;
    private Integer reapymentId;
    private Long repaymentAmount;
    private Long repaymentedAmount;
    private Long repaymentInterest;
    private Long trueRepaymentMoney;
    private Date repaymentTime;
    private Date oldRepaymentTime;
    private String remark;
    private String orderId;
    private Integer reapymentStatus;
    private Integer status;
    private Date createdAt;
    private Integer renewalDay;
    private Integer renewalType;
    private Integer repaymentType;
    private Date orderTime;  //续期时间
    private Integer capitalType; //所属资方
    private Long returnMoney;
    private String backOrderId;  //退款金额，由于线上数据量太大，无法新增字段，故用该字段表示


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Long repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Long getRepaymentedAmount() {
        return repaymentedAmount;
    }

    public void setRepaymentedAmount(Long repaymentedAmount) {
        this.repaymentedAmount = repaymentedAmount;
    }

    public Long getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(Long repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public Long getTrueRepaymentMoney() {
        return trueRepaymentMoney;
    }

    public void setTrueRepaymentMoney(Long trueRepaymentMoney) {
        this.trueRepaymentMoney = trueRepaymentMoney;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Date getOldRepaymentTime() {
        return oldRepaymentTime;
    }

    public void setOldRepaymentTime(Date oldRepaymentTime) {
        this.oldRepaymentTime = oldRepaymentTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getRenewalDay() {
        return renewalDay;
    }

    public void setRenewalDay(Integer renewalDay) {
        this.renewalDay = renewalDay;
    }

    public Integer getRenewalType() {
        return renewalType;
    }

    public void setRenewalType(Integer renewalType) {
        this.renewalType = renewalType;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getReapymentId() {
        return reapymentId;
    }

    public void setReapymentId(Integer reapymentId) {
        this.reapymentId = reapymentId;
    }

    public Integer getReapymentStatus() {
        return reapymentStatus;
    }

    public void setReapymentStatus(Integer reapymentStatus) {
        this.reapymentStatus = reapymentStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(Integer capitalType) {
        this.capitalType = capitalType;
    }

    public Long getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Long returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getBackOrderId() {
        return backOrderId;
    }

    public void setBackOrderId(String backOrderId) {
        this.backOrderId = backOrderId;
    }


}