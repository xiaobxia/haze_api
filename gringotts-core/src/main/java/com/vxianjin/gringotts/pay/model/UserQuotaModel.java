package com.vxianjin.gringotts.pay.model;

import java.math.BigDecimal;

/**
 * @Author: chenkai
 * @Date: 2018/9/12 11:15
 * @Description:
 */
public class UserQuotaModel {
    //用户id
    private Integer userId;
    //可借产品线的额度
    private BigDecimal borrowAmount;
    //总费率
    private BigDecimal totalFeeRate;
    //借款利息
    private BigDecimal borrowInterest;
    //快速信审费
    private BigDecimal turstTrial;
    //平台使用费
    private BigDecimal platformLicensing;
    //代收通道费
    private BigDecimal collectChannelFee;
    //账户管理费
    private BigDecimal accountManagerFee;
    //滞纳金
    private BigDecimal lateFee;
    //续期费
    private BigDecimal renewalFee;
    //续期手续费
    private BigDecimal renewalPoundage;
    //借款期限
    private Integer borrowDay;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(BigDecimal borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public BigDecimal getTotalFeeRate() {
        return totalFeeRate;
    }

    public void setTotalFeeRate(BigDecimal totalFeeRate) {
        this.totalFeeRate = totalFeeRate;
    }

    public BigDecimal getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(BigDecimal borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public BigDecimal getTurstTrial() {
        return turstTrial;
    }

    public void setTurstTrial(BigDecimal turstTrial) {
        this.turstTrial = turstTrial;
    }

    public BigDecimal getPlatformLicensing() {
        return platformLicensing;
    }

    public void setPlatformLicensing(BigDecimal platformLicensing) {
        this.platformLicensing = platformLicensing;
    }

    public BigDecimal getCollectChannelFee() {
        return collectChannelFee;
    }

    public void setCollectChannelFee(BigDecimal collectChannelFee) {
        this.collectChannelFee = collectChannelFee;
    }

    public BigDecimal getAccountManagerFee() {
        return accountManagerFee;
    }

    public void setAccountManagerFee(BigDecimal accountManagerFee) {
        this.accountManagerFee = accountManagerFee;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(BigDecimal renewalFee) {
        this.renewalFee = renewalFee;
    }

    public BigDecimal getRenewalPoundage() {
        return renewalPoundage;
    }

    public void setRenewalPoundage(BigDecimal renewalPoundage) {
        this.renewalPoundage = renewalPoundage;
    }

    public Integer getBorrowDay() {
        return borrowDay;
    }

    public void setBorrowDay(Integer borrowDay) {
        this.borrowDay = borrowDay;
    }
}
