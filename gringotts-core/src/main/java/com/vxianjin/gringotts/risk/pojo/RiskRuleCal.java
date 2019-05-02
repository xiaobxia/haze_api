package com.vxianjin.gringotts.risk.pojo;

import java.util.Date;

public class RiskRuleCal {
    private Integer id;
    private Integer userId;
    private Integer ruleId;
    private Integer creditId;
    private String formulaShow;
    private String ruleValue;
    private String ruleDetail;
    private Date addTime;
    private Integer attentionType;
    private String ruleName;
    private Integer assetId;

    public RiskRuleCal() {
        super();
    }

    public RiskRuleCal(Integer userId, Integer ruleId, Integer creditId,
                       Integer assetId, String formulaShow, String ruleValue,
                       String ruleDetail, String ruleName, Integer attentionType) {
        super();
        this.userId = userId;
        this.ruleId = ruleId;
        this.creditId = creditId;
        this.assetId = assetId;
        this.formulaShow = formulaShow;
        this.ruleValue = ruleValue;
        this.ruleDetail = ruleDetail;
        this.ruleName = ruleName;
        this.attentionType = attentionType;
    }

    public Integer getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(Integer attentionType) {
        this.attentionType = attentionType;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public String getFormulaShow() {
        return formulaShow;
    }

    public void setFormulaShow(String formulaShow) {
        this.formulaShow = formulaShow;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(String ruleDetail) {
        this.ruleDetail = ruleDetail;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

}
