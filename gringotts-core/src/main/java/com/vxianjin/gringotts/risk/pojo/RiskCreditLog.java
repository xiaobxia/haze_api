package com.vxianjin.gringotts.risk.pojo;

import java.util.Date;

public class RiskCreditLog {
    private Integer id;
    private Integer userId;
    private String params;
    private String act;
    private String logType;
    private Date addTime;

    public RiskCreditLog(Integer userId, String params, String act,
                         String logType) {
        super();
        this.userId = userId;
        this.params = params;
        this.act = act;
        this.logType = logType;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

}
