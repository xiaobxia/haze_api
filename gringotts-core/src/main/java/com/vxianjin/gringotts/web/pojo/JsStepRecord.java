package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 行为记录
 *
 * @author Administrator
 */
public class JsStepRecord implements Serializable {
    private static final long serialVersionUID = 6651324600560394335L;
    private String id;

    private String stepId;//行为code

    private String userId;

    private Integer effectiveCount;//抽奖次数

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private String remark;

    private Integer isValid;//（1有效，0无效）

    private JsLoanPerson jsLoanPerson;

    private JsStepConfig jsStepConfig;

    private JsAwardRecord jsAwardRecord;

    private BigDecimal awardMoney;//当前奖金    


    // 条件查询需要的字段
    private Date beginDate;    // 开始查询日期
    private Date endDate;    // 结束查询日期

    private String name;
    private String phone;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public JsAwardRecord getJsAwardRecord() {
        return jsAwardRecord;
    }

    public void setJsAwardRecord(JsAwardRecord jsAwardRecord) {
        this.jsAwardRecord = jsAwardRecord;
    }

    public BigDecimal getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(BigDecimal awardMoney) {
        this.awardMoney = awardMoney;
    }

    public JsStepConfig getJsStepConfig() {
        return jsStepConfig;
    }

    public void setJsStepConfig(JsStepConfig jsStepConfig) {
        this.jsStepConfig = jsStepConfig;
    }

    public JsLoanPerson getJsLoanPerson() {
        return jsLoanPerson;
    }

    public void setJsLoanPerson(JsLoanPerson jsLoanPerson) {
        this.jsLoanPerson = jsLoanPerson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId == null ? null : stepId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getEffectiveCount() {
        return effectiveCount;
    }

    public void setEffectiveCount(Integer effectiveCount) {
        this.effectiveCount = effectiveCount;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }


}