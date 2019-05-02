package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 行为配置
 *
 * @author Administrator
 */
public class JsStepConfig implements Serializable {
    /**
     * 注册成功
     */
    public static final String STEP_REGISTER = "REGISTER";
    /**
     * 认证成功
     */
    public static final String STEP_APPROVE = "APPROVE";
    /**
     * 借款成功
     */
    public static final String STEP_BORROW = "BORROW";
    /**
     * 还款成功
     */
    public static final String STEP_REPAYMENT = "REPAYMENT";
    /**
     * 邀请注册成功
     */
    public static final String STEP_INVITE = "INVITE";
    private static final long serialVersionUID = 4146229818640464409L;
    private String id;
    private String stepCode;//行为code
    private String stepName;//行为名称
    private Integer effectiveCount;//抽奖次数
    private String remark;
    private Integer status;//是否参与抽奖（1是，0否）
    private Date updateTime;//添加时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode == null ? null : stepCode.trim();
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName == null ? null : stepName.trim();
    }

    public Integer getEffectiveCount() {
        return effectiveCount;
    }

    public void setEffectiveCount(Integer effectiveCount) {
        this.effectiveCount = effectiveCount;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}