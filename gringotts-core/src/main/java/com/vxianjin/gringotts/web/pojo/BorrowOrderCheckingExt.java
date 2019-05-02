package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BorrowOrderCheckingExt implements Serializable {
    /**
     * 招财猫资产查询选项
     * TODO：于
     */
    public static final Map<Integer, String> capitalZCMTypeMap = new HashMap<Integer, String>();
    private static final long serialVersionUID = -3602629831267746531L;
    /**
     * 招财猫
     * 此标记对应表中的capital_type 1为招财猫 0 为不确定
     * TODO：于
     */
//    public static Integer ACCEST_ZCM_UNKNOW=0;
    public static Integer ACCEST_ZCM = 1;

    static {
//    	capitalZCMTypeMap.put(ACCEST_ZCM_UNKNOW, "未知");
        capitalZCMTypeMap.put(ACCEST_ZCM, "招财猫");
    }

    private Integer id;
    private Integer userId;
    private Integer assetOrderId;
    private Integer orderType;
    private Integer moneyAmount;
    private Integer apr;
    private Integer loanInterests;
    private Integer intoMoney;
    private Integer loanMethod;
    private Integer loanTerm;
    private String operatorName;
    private String outNo;
    private Date createdAt;
    private Date updatedAt;
    private Date orderTime;
    private Date loanTime;
    private Date loanEndTime;
    private Integer lateFeeApr;
    private Integer affiliationUkey;
    private Integer capitalType;
    private String reasonRemark;
    private Byte isHitRiskRule;
    private Byte autoRiskCheckStatus;
    private Integer customerType;
    private String yurref;
    private String userPhone;
    private String realname;
    private String idNumber;
    private String cardNo;
    private String banksequence;
    private String bankNumber;
    private Integer bankIscmb;
    private Integer status;
    private Integer signStatus;
    private Integer manyAssetpack;
    private Integer oneAssetpackMoney;
    private Date oneAssetpackSigntime;
    private Integer onePushStatus;
    private Integer twoAssetpackMoney;
    private Date twoAssetpackSigntime;
    private Integer twoPushStatus;
    private String remark;
    private Integer packetId;

    public Integer getPacketId() {
        return packetId;
    }

    public void setPacketId(Integer packetId) {
        this.packetId = packetId;
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

    public Integer getAssetOrderId() {
        return assetOrderId;
    }

    public void setAssetOrderId(Integer assetOrderId) {
        this.assetOrderId = assetOrderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Integer moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Integer getApr() {
        return apr;
    }

    public void setApr(Integer apr) {
        this.apr = apr;
    }

    public Integer getLoanInterests() {
        return loanInterests;
    }

    public void setLoanInterests(Integer loanInterests) {
        this.loanInterests = loanInterests;
    }

    public Integer getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(Integer intoMoney) {
        this.intoMoney = intoMoney;
    }

    public Integer getLoanMethod() {
        return loanMethod;
    }

    public void setLoanMethod(Integer loanMethod) {
        this.loanMethod = loanMethod;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Date getLoanEndTime() {
        return loanEndTime;
    }

    public void setLoanEndTime(Date loanEndTime) {
        this.loanEndTime = loanEndTime;
    }

    public Integer getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(Integer lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public Integer getAffiliationUkey() {
        return affiliationUkey;
    }

    public void setAffiliationUkey(Integer affiliationUkey) {
        this.affiliationUkey = affiliationUkey;
    }

    public Integer getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(Integer capitalType) {
        this.capitalType = capitalType;
    }

    public String getReasonRemark() {
        return reasonRemark;
    }

    public void setReasonRemark(String reasonRemark) {
        this.reasonRemark = reasonRemark;
    }

    public Byte getIsHitRiskRule() {
        return isHitRiskRule;
    }

    public void setIsHitRiskRule(Byte isHitRiskRule) {
        this.isHitRiskRule = isHitRiskRule;
    }

    public Byte getAutoRiskCheckStatus() {
        return autoRiskCheckStatus;
    }

    public void setAutoRiskCheckStatus(Byte autoRiskCheckStatus) {
        this.autoRiskCheckStatus = autoRiskCheckStatus;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getYurref() {
        return yurref;
    }

    public void setYurref(String yurref) {
        this.yurref = yurref;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBanksequence() {
        return banksequence;
    }

    public void setBanksequence(String banksequence) {
        this.banksequence = banksequence;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public Integer getBankIscmb() {
        return bankIscmb;
    }

    public void setBankIscmb(Integer bankIscmb) {
        this.bankIscmb = bankIscmb;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public Integer getManyAssetpack() {
        return manyAssetpack;
    }

    public void setManyAssetpack(Integer manyAssetpack) {
        this.manyAssetpack = manyAssetpack;
    }

    public Integer getOneAssetpackMoney() {
        return oneAssetpackMoney;
    }

    public void setOneAssetpackMoney(Integer oneAssetpackMoney) {
        this.oneAssetpackMoney = oneAssetpackMoney;
    }

    public Date getOneAssetpackSigntime() {
        return oneAssetpackSigntime;
    }

    public void setOneAssetpackSigntime(Date oneAssetpackSigntime) {
        this.oneAssetpackSigntime = oneAssetpackSigntime;
    }

    public Integer getOnePushStatus() {
        return onePushStatus;
    }

    public void setOnePushStatus(Integer onePushStatus) {
        this.onePushStatus = onePushStatus;
    }

    public Integer getTwoAssetpackMoney() {
        return twoAssetpackMoney;
    }

    public void setTwoAssetpackMoney(Integer twoAssetpackMoney) {
        this.twoAssetpackMoney = twoAssetpackMoney;
    }

    public Date getTwoAssetpackSigntime() {
        return twoAssetpackSigntime;
    }

    public void setTwoAssetpackSigntime(Date twoAssetpackSigntime) {
        this.twoAssetpackSigntime = twoAssetpackSigntime;
    }

    public Integer getTwoPushStatus() {
        return twoPushStatus;
    }

    public void setTwoPushStatus(Integer twoPushStatus) {
        this.twoPushStatus = twoPushStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}