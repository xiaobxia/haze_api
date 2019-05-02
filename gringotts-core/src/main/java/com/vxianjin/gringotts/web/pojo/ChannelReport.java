package com.vxianjin.gringotts.web.pojo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ChannelReport implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5483950801917017713L;
    private Integer id;
    private Date reportDate;
    private Integer registerCount = 0;
    private Integer attestationRealnameCount = 0;
    private Integer attestationBankCount = 0;
    private Integer contactCount = 0;
    private Integer jxlCount = 0;
    private Integer alipayCount = 0;
    private Integer zhimaCount = 0;
    private Integer companyCount = 0;
    private Integer borrowApplyCount = 0;

    private Integer borrowApplyOutCount = 0;

    private Integer borrowSucCount = 0;

    private Integer borrowSucOutCount = 0;

    private Integer blackUserCount = 0;
    private Integer lateDayCount = 0;
    private Integer lateDaySumCount = 0;
    private BigDecimal borrowRate;

    private BigDecimal borrowRateOut;

    private BigDecimal passRate;

    private BigDecimal passRateOut;

    private BigDecimal intoMoney;
    private BigDecimal newIntoMoney;
    private BigDecimal oldIntoMoney;
    private Integer approveErrorCount = 0;
    private Integer channelid;
    private String channelName;
    private String channelProvince;
    private String channelCity;
    private String channelArea;
    private Date createdAt;

    private String channelSuperName;
    private BigDecimal channelMoney;


    private Integer androidCount = 0;//Android注册量
    private Integer iosCount = 0;//ios注册量
    private Integer pcCount = 0;//pc注册量
    /*2018-04-18新增*/
    private Integer dayRealnameCount;
    private Integer dayBankCount;
    private Integer dayContactCount;
    private Integer dayTdCount;
    private Integer dayAlipayCount;
    private Integer dayZhimaCount;
    private Integer dayCompanyCount;
    private Integer dayBlackUserCount;
    private Integer dayBorrowApplyCount;
    private Integer dayBorrowSucCount;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /*--*/
    public Integer getAndroidCount() {
        return androidCount;
    }

    public void setAndroidCount(Integer androidCount) {
        this.androidCount = androidCount;
    }

    public Integer getIosCount() {
        return iosCount;
    }

    public void setIosCount(Integer iosCount) {
        this.iosCount = iosCount;
    }

    public Integer getPcCount() {
        return pcCount;
    }

    public void setPcCount(Integer pcCount) {
        this.pcCount = pcCount;
    }

    public BigDecimal getChannelMoney() {
        return channelMoney;
    }

    public void setChannelMoney(BigDecimal channelMoney) {
        this.channelMoney = channelMoney;
    }

    public String getChannelSuperName() {
        return channelSuperName;
    }

    public void setChannelSuperName(String channelSuperName) {
        this.channelSuperName = channelSuperName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getBorrowRate() {
        BigDecimal a = new BigDecimal(borrowApplyCount);
        BigDecimal d = new BigDecimal(registerCount);
        BigDecimal borrowRate = BigDecimal.ZERO;
        if (borrowApplyCount > 0 && registerCount > 0) {
            borrowRate = a.multiply(new BigDecimal(100)).divide(d, 2,
                    BigDecimal.ROUND_HALF_UP);
        }
        return borrowRate;
    }

    public void setBorrowRate(BigDecimal borrowRate) {
        this.borrowRate = borrowRate;
    }

    public BigDecimal getNewIntoMoney() {
        return newIntoMoney;
    }

    public void setNewIntoMoney(BigDecimal newIntoMoney) {
        this.newIntoMoney = newIntoMoney;
    }

    public BigDecimal getOldIntoMoney() {
        return oldIntoMoney;
    }

    public void setOldIntoMoney(BigDecimal oldIntoMoney) {
        this.oldIntoMoney = oldIntoMoney;
    }

    public Integer getLateDaySumCount() {
        return lateDaySumCount;
    }

    public void setLateDaySumCount(Integer lateDaySumCount) {
        this.lateDaySumCount = lateDaySumCount;
    }

    public Integer getBlackUserCount() {
        return blackUserCount;
    }

    public void setBlackUserCount(Integer blackUserCount) {
        this.blackUserCount = blackUserCount;
    }

    public Integer getLateDayCount() {
        return lateDayCount;
    }

    public void setLateDayCount(Integer lateDayCount) {
        this.lateDayCount = lateDayCount;
    }

    public String getChannelProvince() {
        return channelProvince;
    }

    public void setChannelProvince(String channelProvince) {
        this.channelProvince = channelProvince;
    }

    public String getChannelCity() {
        return channelCity;
    }

    public void setChannelCity(String channelCity) {
        this.channelCity = channelCity;
    }

    public String getChannelArea() {
        return channelArea;
    }

    public void setChannelArea(String channelArea) {
        this.channelArea = channelArea;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChannelid() {
        return channelid;
    }

    public void setChannelid(Integer channelid) {
        this.channelid = channelid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getAttestationRealnameCount() {
        return attestationRealnameCount;
    }

    public void setAttestationRealnameCount(Integer attestationRealnameCount) {
        this.attestationRealnameCount = attestationRealnameCount;
    }

    public Integer getAttestationBankCount() {
        return attestationBankCount;
    }

    public void setAttestationBankCount(Integer attestationBankCount) {
        this.attestationBankCount = attestationBankCount;
    }

    public Integer getContactCount() {
        return contactCount;
    }

    public void setContactCount(Integer contactCount) {
        this.contactCount = contactCount;
    }

    public Integer getJxlCount() {
        return jxlCount;
    }

    public void setJxlCount(Integer jxlCount) {
        this.jxlCount = jxlCount;
    }

    public Integer getAlipayCount() {
        return alipayCount;
    }

    public void setAlipayCount(Integer alipayCount) {
        this.alipayCount = alipayCount;
    }

    public Integer getZhimaCount() {
        return zhimaCount;
    }

    public void setZhimaCount(Integer zhimaCount) {
        this.zhimaCount = zhimaCount;
    }

    public Integer getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(Integer companyCount) {
        this.companyCount = companyCount;
    }

    public Integer getBorrowApplyCount() {
        return borrowApplyCount;
    }

    public void setBorrowApplyCount(Integer borrowApplyCount) {
        this.borrowApplyCount = borrowApplyCount;
    }

    public Integer getBorrowSucCount() {
        return borrowSucCount;
    }

    public void setBorrowSucCount(Integer borrowSucCount) {
        this.borrowSucCount = borrowSucCount;
    }

    public BigDecimal getPassRate() {

        BigDecimal a = new BigDecimal(borrowApplyCount);
        BigDecimal b = new BigDecimal(borrowSucCount);
        BigDecimal passRate = BigDecimal.ZERO;
        if (borrowApplyCount > 0 && borrowSucCount > 0) {
            passRate = b.multiply(new BigDecimal(100)).divide(a, 2,
                    BigDecimal.ROUND_HALF_UP);
        }

        return passRate;
    }

    public void setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
    }

    public BigDecimal getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(BigDecimal intoMoney) {
        this.intoMoney = intoMoney;
    }

    public Integer getApproveErrorCount() {
        return approveErrorCount;
    }

    public void setApproveErrorCount(Integer approveErrorCount) {
        this.approveErrorCount = approveErrorCount;
    }

    public Integer getDayRealnameCount() {
        return dayRealnameCount;
    }

    public void setDayRealnameCount(Integer dayRealnameCount) {
        this.dayRealnameCount = dayRealnameCount;
    }

    public Integer getDayBankCount() {
        return dayBankCount;
    }

    public void setDayBankCount(Integer dayBankCount) {
        this.dayBankCount = dayBankCount;
    }

    public Integer getDayContactCount() {
        return dayContactCount;
    }

    public void setDayContactCount(Integer dayContactCount) {
        this.dayContactCount = dayContactCount;
    }

    public Integer getDayTdCount() {
        return dayTdCount;
    }

    public void setDayTdCount(Integer dayTdCount) {
        this.dayTdCount = dayTdCount;
    }

    public Integer getDayAlipayCount() {
        return dayAlipayCount;
    }

    public void setDayAlipayCount(Integer dayAlipayCount) {
        this.dayAlipayCount = dayAlipayCount;
    }

    public Integer getDayZhimaCount() {
        return dayZhimaCount;
    }

    public void setDayZhimaCount(Integer dayZhimaCount) {
        this.dayZhimaCount = dayZhimaCount;
    }

    public Integer getDayCompanyCount() {
        return dayCompanyCount;
    }

    public void setDayCompanyCount(Integer dayCompanyCount) {
        this.dayCompanyCount = dayCompanyCount;
    }

    public Integer getDayBlackUserCount() {
        return dayBlackUserCount;
    }

    public void setDayBlackUserCount(Integer dayBlackUserCount) {
        this.dayBlackUserCount = dayBlackUserCount;
    }

    public Integer getDayBorrowApplyCount() {
        return dayBorrowApplyCount;
    }

    public void setDayBorrowApplyCount(Integer dayBorrowApplyCount) {
        this.dayBorrowApplyCount = dayBorrowApplyCount;
    }

    public Integer getDayBorrowSucCount() {
        return dayBorrowSucCount;
    }

    public void setDayBorrowSucCount(Integer dayBorrowSucCount) {
        this.dayBorrowSucCount = dayBorrowSucCount;
    }

    public Integer getBorrowApplyOutCount() {
        return borrowApplyOutCount;
    }

    public void setBorrowApplyOutCount(Integer borrowApplyOutCount) {
        this.borrowApplyOutCount = borrowApplyOutCount;
    }

    public Integer getBorrowSucOutCount() {
        return borrowSucOutCount;
    }

    public void setBorrowSucOutCount(Integer borrowSucOutCount) {
        this.borrowSucOutCount = borrowSucOutCount;
    }

    public BigDecimal getBorrowRateOut() {
        BigDecimal a = new BigDecimal(borrowApplyOutCount);
        BigDecimal d = new BigDecimal(registerCount);
        BigDecimal borrowOutRate = BigDecimal.ZERO;
        if (borrowApplyOutCount > 0 && registerCount > 0) {
            borrowOutRate = a.multiply(new BigDecimal(100)).divide(d, 2,
                    BigDecimal.ROUND_HALF_UP);
        }
        return borrowOutRate;
    }

    public void setBorrowRateOut(BigDecimal borrowRateOut) {
        this.borrowRateOut = borrowRateOut;
    }

    public BigDecimal getPassRateOut() {
        BigDecimal a = new BigDecimal(borrowApplyOutCount);
        BigDecimal b = new BigDecimal(borrowSucOutCount);
        BigDecimal passRateOut = BigDecimal.ZERO;
        if (borrowApplyOutCount > 0 && borrowSucOutCount > 0) {
            passRateOut = b.multiply(new BigDecimal(100)).divide(a, 2,
                    BigDecimal.ROUND_HALF_UP);
        }

        return passRateOut;
    }

    public void setPassRateOut(BigDecimal passRateOut) {
        this.passRateOut = passRateOut;
    }
}
