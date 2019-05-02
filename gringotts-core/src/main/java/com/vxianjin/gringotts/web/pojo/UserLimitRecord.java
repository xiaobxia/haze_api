package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserLimitRecord implements Serializable {
    public static final Map<Integer, String> statusMap = new HashMap<Integer, String>();
    /**
     * 待审核
     */
    public static final Integer STATUS_WAIT = 0;
    /**
     * 审核通过
     */
    public static final Integer STATUS_PASS_SUCC = 1;
    /**
     * 审核失败
     */
    public static final Integer STATUS_PASS_FAIL = 2;
    private static final long serialVersionUID = -5817294158096894072L;

    static {

        statusMap.put(STATUS_WAIT, "待审核");
        statusMap.put(STATUS_PASS_SUCC, "审核通过");
        statusMap.put(STATUS_PASS_FAIL, "审核失败");
    }

    private Integer id;
    private Integer userId;
    private Date createAt;
    private Date lastApplyAt;
    private Integer repaymentSuccCount;
    private Integer repaymentNormCount;
    private Integer repaymentSuccAmount;
    private Integer repaymentNormAmount;
    private Date updatedAt;
    private Integer addAmount;
    private Integer newAmountMin;
    private Integer oldAmountMax;
    private Integer newAmountMax;
    private Integer addReasonType;// 提额原因类型，1:1000，2:2000；3:3000
    private String auditUser;
    private String userPhone;
    private String realname;
    // private User user;
    private String remark;
    private Integer status;
    private String statusName;

    public String getStatusName() {
        return statusName;
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

    public Integer getRepaymentSuccAmount() {
        return repaymentSuccAmount;
    }

    public void setRepaymentSuccAmount(Integer repaymentSuccAmount) {
        this.repaymentSuccAmount = repaymentSuccAmount;
    }

    public Integer getAddReasonType() {
        return addReasonType;
    }

    public void setAddReasonType(Integer addReasonType) {
        this.addReasonType = addReasonType;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getLastApplyAt() {
        return lastApplyAt;
    }

    public void setLastApplyAt(Date lastApplyAt) {
        this.lastApplyAt = lastApplyAt;
    }

    public Integer getRepaymentSuccCount() {
        return repaymentSuccCount;
    }

    public void setRepaymentSuccCount(Integer repaymentSuccCount) {
        this.repaymentSuccCount = repaymentSuccCount;
    }

    public Integer getRepaymentNormCount() {
        return repaymentNormCount;
    }

    public void setRepaymentNormCount(Integer repaymentNormCount) {
        this.repaymentNormCount = repaymentNormCount;
    }

    public Integer getRepaymentNormAmount() {
        return repaymentNormAmount;
    }

    public void setRepaymentNormAmount(Integer repaymentNormAmount) {
        this.repaymentNormAmount = repaymentNormAmount;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(Integer addAmount) {
        this.addAmount = addAmount;
    }

    public Integer getNewAmountMin() {
        return newAmountMin;
    }

    public void setNewAmountMin(Integer newAmountMin) {
        this.newAmountMin = newAmountMin;
    }

    public Integer getOldAmountMax() {
        return oldAmountMax;
    }

    public void setOldAmountMax(Integer oldAmountMax) {
        this.oldAmountMax = oldAmountMax;
    }

    public Integer getNewAmountMax() {
        return newAmountMax;
    }

    public void setNewAmountMax(Integer newAmountMax) {
        this.newAmountMax = newAmountMax;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser == null ? null : auditUser.trim();
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
        this.statusName = statusMap.get(status);
    }
}