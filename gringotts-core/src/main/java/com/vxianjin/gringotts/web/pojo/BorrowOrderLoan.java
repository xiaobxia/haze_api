package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BorrowOrderLoan implements Serializable {
    public static final Integer CSZT = 0;
    public static final Integer DFK = 1;
    public static final Integer DKZ = 2;
    public static final Integer FKSB = 3;
    public static final Integer FKCG = 4;
    public static final Map<Integer, String> allstatus = new HashMap<Integer, String>();
    private static final long serialVersionUID = 4321209966429091377L;

    static {

        allstatus.put(CSZT, "初始状态");
        allstatus.put(DFK, "待打款");
        allstatus.put(DKZ, "打款中");
        allstatus.put(FKSB, "打款失败");
        allstatus.put(FKCG, "打款成功");
    }

    private Integer id;
    private Integer userId;
    private Integer assetOrderId;
    private String yurref;
    private Integer loanInterests;
    private Date createdAt;
    private Date updatedAt;
    private Date loanTime;
    private Integer capitalType;
    private Integer status;// 状态：0：初始状态，1：待打款，2：打款中，3；打款失败 ；4打款成功
    private String statusShow;// 状态：0：初始状态，1：待打款，2：打款中，3；打款失败 ；4打款成功
    private String payRemark;
    private String payStatus;

    public Integer getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(Integer capitalType) {
        this.capitalType = capitalType;
    }

    public String getStatusShow() {
        return statusShow;
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

    public String getYurref() {
        return yurref;
    }

    public void setYurref(String yurref) {
        this.yurref = yurref == null ? null : yurref.trim();
    }

    public Integer getLoanInterests() {
        return loanInterests;
    }

    public void setLoanInterests(Integer loanInterests) {
        this.loanInterests = loanInterests;
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

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {

        this.status = status;
        this.statusShow = allstatus.get(status);
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark == null ? null : payRemark.trim();
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }
}