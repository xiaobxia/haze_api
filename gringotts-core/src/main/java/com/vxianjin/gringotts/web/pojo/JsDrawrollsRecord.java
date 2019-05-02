package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖号码记录
 *
 * @author Administrator
 */
public class JsDrawrollsRecord implements Serializable {
    private static final long serialVersionUID = 4167531055569293788L;
    public static Integer startNumber = 10000000;
    private String id;

    private Integer periods;//当前期数

    private String userId;

    private String stepId;//行为ID

    private Integer luckyDraw;//抽奖号码

    private Date addTime;//号码添加时间

    private Date updateTime;//记录更新时间

    private Integer status;//是否参与抽奖(1是，0否，2为开奖)

    private Integer isValid;//（1有效，0无效）

    private String remark;

    /**
     * 1203
     **/
    private String phone;

    private String awardMoney;

    private String stepName;


    private JsLoanPerson jsLoanPerson;


    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsLoanPerson getJsLoanPerson() {
        return jsLoanPerson;
    }

    public void setJsLoanPerson(JsLoanPerson jsLoanPerson) {
        this.jsLoanPerson = jsLoanPerson;
    }

    public String getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(String awardMoney) {
        this.awardMoney = awardMoney;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public Integer getLuckyDraw() {
        return luckyDraw;
    }

    public void setLuckyDraw(Integer luckyDraw) {
        this.luckyDraw = luckyDraw;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}