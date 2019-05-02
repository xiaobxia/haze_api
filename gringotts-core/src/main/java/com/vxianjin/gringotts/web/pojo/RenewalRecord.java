package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RenewalRecord implements Serializable {
    public static final Map<Integer, String> RENEWAL_STATUS = new HashMap<Integer, String>();
    /**
     * 付款中
     */
    public static final Integer STATUS_PAYING = 0;
    /**
     * 付款成功
     */
    public static final Integer STATUS_SUCC = 1;
    /**
     * 付款失败
     */
    public static final Integer STATUS_FAIL = 2;
    /**
     * 对账之后待处理or待退款
     */
    public static final Integer STATUS_HANDLE = 5;
    public static final Map<Integer, String> RENEW_KIND = getRenewKind();
    private static final long serialVersionUID = 7038614746517546351L;
    //续期类型
    private static final int NORMAL_RENEW = 1;
    private static final int OVEDUE_RENEW = 2;
    private static final int OTHER_RENEW = 3;

    static {

        RENEWAL_STATUS.put(STATUS_PAYING, "待审核");
        RENEWAL_STATUS.put(STATUS_SUCC, "审核通过");
        RENEWAL_STATUS.put(STATUS_FAIL, "审核失败");
        RENEWAL_STATUS.put(STATUS_HANDLE, "待处理");
    }

    private Integer id;
    private Integer userId;
    private Integer assetRepaymentId;
    private Long repaymentPrincipal;
    private Long sumFee;
    private BigDecimal sumFeeBig;
    private Long repaymentInterest;
    private BigDecimal repaymentInterestBig;
    private Integer planLateFee;
    private BigDecimal planLateFeeBig;
    private Integer renewalFee;
    private BigDecimal renewalFeeBig;
    private Date oldRepaymentTime;
    private Integer renewalDay;
    private Date updatedAt;
    private String remark;
    private Integer status;
    private Date repaymentTime;
    private Date createdAt;
    private Long moneyAmount;
    private String orderId;
    private Integer renewalType;
    private Integer renewalKind;
    private String realname;
    private String userPhone;
    private Integer customerType;
    private Long repaymentAmount;
    private Long repaymentedAmount;
    private Long creditRepaymentTime;
    private Long repaymentRealTime;
    private Integer renewalCount;
    private Integer repayStatus;
    private Date orderTime;
    private Long returnMoney;

    private static Map<Integer, String> getRenewKind() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "正常续期");
        map.put(2, "延期续期");
        map.put(3, "其他");
        return map;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAssetRepaymentId() {
        return assetRepaymentId;
    }

    public void setAssetRepaymentId(Integer assetRepaymentId) {
        this.assetRepaymentId = assetRepaymentId;
    }

    public Long getRepaymentPrincipal() {
        return repaymentPrincipal;
    }

    public void setRepaymentPrincipal(Long repaymentPrincipal) {
        this.repaymentPrincipal = repaymentPrincipal;
    }

    public Long getSumFee() {
        return sumFee;
    }

    public void setSumFee(Long sumFee) {
        this.sumFee = sumFee;
    }

    public Long getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(Long repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public Integer getPlanLateFee() {
        return planLateFee;
    }

    public void setPlanLateFee(Integer planLateFee) {
        this.planLateFee = planLateFee;
    }

    public Integer getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(Integer renewalFee) {
        this.renewalFee = renewalFee;
    }

    public Date getOldRepaymentTime() {
        return oldRepaymentTime;
    }

    public void setOldRepaymentTime(Date oldRepaymentTime) {
        this.oldRepaymentTime = oldRepaymentTime;
    }

    public Integer getRenewalDay() {
        return renewalDay;
    }

    public void setRenewalDay(Integer renewalDay) {
        this.renewalDay = renewalDay;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getSumFeeBig() {
        return sumFeeBig;
    }

    public void setSumFeeBig(BigDecimal sumFeeBig) {
        if (null != sumFeeBig) {
            setSumFee(sumFeeBig.multiply(new BigDecimal(100)).longValue());
        }
        this.sumFeeBig = sumFeeBig;
    }

    public BigDecimal getRepaymentInterestBig() {
        return repaymentInterestBig;
    }

    public void setRepaymentInterestBig(BigDecimal repaymentInterestBig) {
        if (null != repaymentInterestBig) {
            setRepaymentInterest(repaymentInterestBig.multiply(new BigDecimal(100)).longValue());
        }
        this.repaymentInterestBig = repaymentInterestBig;
    }

    public BigDecimal getPlanLateFeeBig() {
        return planLateFeeBig;
    }

    public void setPlanLateFeeBig(BigDecimal planLateFeeBig) {
        if (null != planLateFeeBig) {
            setPlanLateFee(planLateFeeBig.multiply(new BigDecimal(100)).intValue());
        }
        this.planLateFeeBig = planLateFeeBig;
    }

    public BigDecimal getRenewalFeeBig() {
        return renewalFeeBig;
    }

    public void setRenewalFeeBig(BigDecimal renewalFeeBig) {
        if (null != renewalFeeBig) {
            setRenewalFee(renewalFeeBig.multiply(new BigDecimal(100)).intValue());
        }
        this.renewalFeeBig = renewalFeeBig;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getRenewalType() {
        return renewalType;
    }

    public void setRenewalType(Integer renewalType) {
        this.renewalType = renewalType;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
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

    public Long getCreditRepaymentTime() {
        return creditRepaymentTime;
    }

    public void setCreditRepaymentTime(Long creditRepaymentTime) {
        this.creditRepaymentTime = creditRepaymentTime;
    }

    public Long getRepaymentRealTime() {
        return repaymentRealTime;
    }

    public void setRepaymentRealTime(Long repaymentRealTime) {
        this.repaymentRealTime = repaymentRealTime;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
    }

    public Integer getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Long getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Long returnMoney) {
        this.returnMoney = returnMoney;
    }

    public Integer getRenewalKind() {
        return renewalKind;
    }

    public void setRenewalKind(Integer renewalKind) {
        this.renewalKind = renewalKind;
    }
}