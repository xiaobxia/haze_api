package com.vxianjin.gringotts.risk.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiefei
 * @date 2018/05/12
 */
public class RiskModelOrder {
    private Long id;
    /**
     * 借款订单id
     */
    private Integer borrowOrderId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 新老用户标志
     */
    private Integer customerType;


    /**
     * 模型编号
     */
    private String modelCode;
    /**
     * 变量增删版本号
     */
    private Integer variableVersion;
    /**
     * 分箱得分版本号
     */
    private Integer binningVersion;
    /**
     * cutoff线版本号
     */
    private Integer cutoffVersion;
    /**
     * 不重要修改的版本号
     */
    private Integer canIgnoreVersion;

    /**
     * cutoff线
     */
    private Integer cutoff;
    /**
     * 复审上限
     */
    private Integer reviewUp;
    /**
     * 复审下限
     */
    private Integer reviewDown;
    /**
     * 老用户意见
     */
    private Integer oldCustomerAdvice;
    /**
     * 硬指标意见
     */
    private Integer inflexibleAdvice;
    /**
     * 模型放款意见
     */
    private Integer modelAdvice;
    /**
     * 汇总意见
     */
    private Integer summaryAdvice;
    /**
     * 执行意见
     */
    private Integer executeAdvice;
    /**
     * 人工复审意见
     */
    private Integer personReviewAdvice;
    /**
     * 人工复审复审员账户
     */
    private String personReviewAccount;
    /**
     * 人工复审备注
     */
    private String personReviewRemark;
    /**
     * 人工复审时间
     */
    private Date personReviewTime;


    /**
     * 模型得分
     */
    private Integer modelScore;
    /**
     * 总放款开关状态(0、开启；1、关闭)
     */
    private Integer loanSwitchStatus;

    private Date createTime;

    private Date updateTime;

    //总放款开关状态(1、开启；0、关闭)
    public static final int LOAN_OFF = 0;
    public static final int LOAN_ON = 1;
    public static final Map<Boolean, Integer> LOAN_SWITCH_TYPE = new HashMap<>();

    static {
        LOAN_SWITCH_TYPE.put(true, LOAN_ON);
        LOAN_SWITCH_TYPE.put(false, LOAN_OFF);
    }

    //未给出建议
    public static final int ADVICE_NULL = -1;
    //通过
    public static final int ADVICE_PASS = 0;
    //复审
    public static final int ADVICE_REVIEW = 1;
    //拒绝
    public static final int ADVICE_REJECT = 2;

    public static final Map<String, Integer> ADVICE_TYPE = new HashMap<>();

    static {
        ADVICE_TYPE.put("PASS", ADVICE_PASS);
        ADVICE_TYPE.put("REVIEW", ADVICE_REVIEW);
        ADVICE_TYPE.put("REJECT", ADVICE_REJECT);
    }

    public String getPersonReviewAccount() {
        return personReviewAccount;
    }

    public void setPersonReviewAccount(String personReviewAccount) {
        this.personReviewAccount = personReviewAccount;
    }

    public String getPersonReviewRemark() {
        return personReviewRemark;
    }

    public void setPersonReviewRemark(String personReviewRemark) {
        this.personReviewRemark = personReviewRemark;
    }

    public Date getPersonReviewTime() {
        return personReviewTime;
    }

    public void setPersonReviewTime(Date personReviewTime) {
        this.personReviewTime = personReviewTime;
    }

    public Integer getOldCustomerAdvice() {
        return oldCustomerAdvice;
    }

    public void setOldCustomerAdvice(Integer oldCustomerAdvice) {
        this.oldCustomerAdvice = oldCustomerAdvice;
    }

    public Integer getPersonReviewAdvice() {
        return personReviewAdvice;
    }

    public void setPersonReviewAdvice(Integer personReviewAdvice) {
        this.personReviewAdvice = personReviewAdvice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBorrowOrderId() {
        return borrowOrderId;
    }

    public void setBorrowOrderId(Integer borrowOrderId) {
        this.borrowOrderId = borrowOrderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Integer getVariableVersion() {
        return variableVersion;
    }

    public void setVariableVersion(Integer variableVersion) {
        this.variableVersion = variableVersion;
    }

    public Integer getBinningVersion() {
        return binningVersion;
    }

    public void setBinningVersion(Integer binningVersion) {
        this.binningVersion = binningVersion;
    }

    public Integer getCutoffVersion() {
        return cutoffVersion;
    }

    public void setCutoffVersion(Integer cutoffVersion) {
        this.cutoffVersion = cutoffVersion;
    }

    public Integer getCanIgnoreVersion() {
        return canIgnoreVersion;
    }

    public void setCanIgnoreVersion(Integer canIgnoreVersion) {
        this.canIgnoreVersion = canIgnoreVersion;
    }

    public Integer getCutoff() {
        return cutoff;
    }

    public void setCutoff(Integer cutoff) {
        this.cutoff = cutoff;
    }

    public Integer getReviewUp() {
        return reviewUp;
    }

    public void setReviewUp(Integer reviewUp) {
        this.reviewUp = reviewUp;
    }

    public Integer getReviewDown() {
        return reviewDown;
    }

    public void setReviewDown(Integer reviewDown) {
        this.reviewDown = reviewDown;
    }

    public Integer getInflexibleAdvice() {
        return inflexibleAdvice;
    }

    public void setInflexibleAdvice(Integer inflexibleAdvice) {
        this.inflexibleAdvice = inflexibleAdvice;
    }

    public Integer getModelAdvice() {
        return modelAdvice;
    }

    public void setModelAdvice(Integer modelAdvice) {
        this.modelAdvice = modelAdvice;
    }

    public Integer getSummaryAdvice() {
        return summaryAdvice;
    }

    public void setSummaryAdvice(Integer summaryAdvice) {
        this.summaryAdvice = summaryAdvice;
    }

    public Integer getExecuteAdvice() {
        return executeAdvice;
    }

    public void setExecuteAdvice(Integer executeAdvice) {
        this.executeAdvice = executeAdvice;
    }

    public Integer getModelScore() {
        return modelScore;
    }

    public void setModelScore(Integer modelScore) {
        this.modelScore = modelScore;
    }

    public Integer getLoanSwitchStatus() {
        return loanSwitchStatus;
    }

    public void setLoanSwitchStatus(Integer loanSwitchStatus) {
        this.loanSwitchStatus = loanSwitchStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }
}
