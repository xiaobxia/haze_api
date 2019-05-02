package com.vxianjin.gringotts.pay.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * @Author: chenkai
 * @Date: 2018/8/21 14:12
 * @Description: 订单变化model
 */
public class OrderLogModel implements Serializable {

    private static final long serialVersionUID = 5721431609543586222L;
    /**
     * 用户id
     */
    private String userId;


    /**
     * 借款id
     */
    private String borrowId;


    /**
     * 业务id
     */
    private String operateId;


    /**
     * 业务类型
     */
    private String operateType;


    /**
     * 状态变化行为
     */
    private String action;


    /**
     * 变化前状态
     */
    private String beforeStatus;


    /**
     * 变化后状态
     */
    private String afterStatus;

    private Date createTime;

    private Date updateTime;

    private String remark;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBeforeStatus() {
        return beforeStatus;
    }

    public void setBeforeStatus(String beforeStatus) {
        this.beforeStatus = beforeStatus;
    }

    public String getAfterStatus() {
        return afterStatus;
    }

    public void setAfterStatus(String afterStatus) {
        this.afterStatus = afterStatus;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public OrderLogModel(){};
    /**
     * @param userId       用户id
     * @param borrowId     借款id
     * @param operateId    操作id
     * @param operateType  操作类型
     * @param action       状态变化行为
     * @param beforeStatus 变化前状态
     * @param afterStatus  变化后状态
     * @param createTime   创建时间
     * @param remark       备注
     */
    public OrderLogModel(String userId, String borrowId, String operateId, String operateType, String action, String beforeStatus, String afterStatus, Date createTime, String remark) {
        this.userId = userId;
        this.borrowId = borrowId;
        this.operateId = operateId;
        this.operateType = operateType;
        this.action = action;
        this.beforeStatus = beforeStatus;
        this.afterStatus = afterStatus;
        this.createTime = createTime;
        this.remark = remark;
    }
}
