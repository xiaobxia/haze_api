package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BorrowOrderChecking implements Serializable {
    public static final Map<String, String> banksequenceMap = new HashMap<String, String>();
    public static final Map<String, String> banksequenceNameMap = new HashMap<String, String>();
    public static final Map<Integer, String> allstatus = new HashMap<Integer, String>();
    public static final Map<Integer, String> capitalTypeMap = new HashMap<Integer, String>();
    private static final long serialVersionUID = -1224878218457897096L;
    public static Integer ASSEST_LQ = 0;
    public static Integer ASSEST_KD = 1;
    public static Integer ASSEST_WZD = 2;
    public static Integer STATUS_DTS = 0;
    public static Integer STATUS_TSZ = 1;
    public static Integer STATUS_TSCG = 2;
    public static Integer STATUS_TSSB = 3;

    static {
        banksequenceMap.put("102100099996", "1");//中国工商银行
        banksequenceMap.put("103100000026", "2");//中国农业银行
        banksequenceMap.put("303100000006", "3");//中国光大银行
        banksequenceMap.put("403100000004", "4");//中国邮政
        banksequenceMap.put("309391000011", "5");//兴业银行
        banksequenceMap.put("105100000017", "7");//中国建设银行
        banksequenceMap.put("308584000013", "8");//招商银行
        banksequenceMap.put("104100000004", "9");//中国银行
        banksequenceMap.put("310290000013", "10");//浦东发展银行
        banksequenceMap.put("307584007998", "11");//平安银行
        banksequenceMap.put("304100040000", "12");//华夏银行
//    	banksequenceMap.put("", "13");//花旗银行
        banksequenceMap.put("301290000007", "14");//交通银行
        banksequenceMap.put("305100000013", "15");//中国民生银行
        banksequenceMap.put("306581000003", "16");//广东发展银行
    }

    static {
        banksequenceNameMap.put("102100099996", "中国工商银行");//
        banksequenceNameMap.put("103100000026", "中国农业银行");//
        banksequenceNameMap.put("303100000006", "中国光大银行");//
        banksequenceNameMap.put("403100000004", "中国邮政");//
        banksequenceNameMap.put("309391000011", "兴业银行");//
        banksequenceNameMap.put("105100000017", "中国建设银行");//
        banksequenceNameMap.put("308584000013", "招商银行");//
        banksequenceNameMap.put("104100000004", "中国银行");//
        banksequenceNameMap.put("310290000013", "浦东发展银行");//
        banksequenceNameMap.put("307584007998", "平安银行");//
        banksequenceNameMap.put("304100040000", "华夏银行");//
//    	banksequenceMap.put("", "13");//花旗银行
        banksequenceNameMap.put("301290000007", "交通银行");//
        banksequenceNameMap.put("305100000013", "中国民生银行");//
        banksequenceNameMap.put("306581000003", "广东发展银行");//
    }

    static {
        allstatus.put(STATUS_DTS, "待推送");
        allstatus.put(STATUS_TSZ, "推送中");
        allstatus.put(STATUS_TSCG, "推送成功");
        allstatus.put(STATUS_TSSB, "推送失败");
    }

    static {
        capitalTypeMap.put(ASSEST_LQ, "小鱼儿");
        capitalTypeMap.put(ASSEST_KD, "小鱼儿");
        capitalTypeMap.put(ASSEST_WZD, "小鱼儿");
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
    private String outNo;
    private Date createdAt;
    private Date updatedAt;
    private Date orderTime;
    private Date loanTime;
    private Date loanEndTime;
    private Integer lateFeeApr;
    private Integer capitalType;//0利全，1口袋，2温州贷
    private String reasonRemark;
    private Byte isHitRiskRule;
    private Byte autoRiskCheckStatus;
    private Integer customerType;
    private String customerTypeName;
    private String yurref;
    private String userPhone;
    private String realname;
    private String idNumber;
    private String cardNo;
    private String banksequence;
    private String bankNumber;
    private Integer bankIscmb;
    private Integer status;//状态：0:待推送，1推送中，2推送成功，3推送失败
    private String remark;

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
        this.reasonRemark = reasonRemark == null ? null : reasonRemark.trim();
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
        this.customerTypeName = User.customerTypes.get(customerType);
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public String getYurref() {
        return yurref;
    }

    public void setYurref(String yurref) {
        this.yurref = yurref == null ? null : yurref.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getBanksequence() {
        return banksequence;
    }

    public void setBanksequence(String banksequence) {
        this.banksequence = banksequence == null ? null : banksequence.trim();
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber == null ? null : bankNumber.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}