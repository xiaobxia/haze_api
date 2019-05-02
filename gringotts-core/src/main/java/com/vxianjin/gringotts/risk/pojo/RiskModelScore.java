package com.vxianjin.gringotts.risk.pojo;

import java.util.Date;

/**
 * @author xiefei
 * @date 2018/05/03
 */
public class RiskModelScore {
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
     * 变量编号
     */
    private String variableName;
    /**
     * 变量值
     */
    private Double variableValue;
    /**
     * 变量得分
     */
    private Integer variableScore;

    private Date createTime;

    private Date updateTime;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getCutoffVersion() {
        return cutoffVersion;
    }

    public void setCutoffVersion(Integer cutoffVersion) {
        this.cutoffVersion = cutoffVersion;
    }

    public Integer getBinningVersion() {
        return binningVersion;
    }

    public void setBinningVersion(Integer binningVersion) {
        this.binningVersion = binningVersion;
    }

    public Integer getCanIgnoreVersion() {
        return canIgnoreVersion;
    }

    public void setCanIgnoreVersion(Integer canIgnoreVersion) {
        this.canIgnoreVersion = canIgnoreVersion;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Double getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(Double variableValue) {
        this.variableValue = variableValue;
    }

    public Integer getVariableScore() {
        return variableScore;
    }

    public void setVariableScore(Integer variableScore) {
        this.variableScore = variableScore;
    }
}
