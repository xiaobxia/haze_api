package com.vxianjin.gringotts.web.pojo;

import java.util.Date;

public class Orders {
    /**
     * 该笔请求的非成功状态
     */
    public static final String STATUS_OTHER = "1";
    /**
     * 成功推送到第三方
     */
    public static final String STATUS_SEND_SUC = "2";
    /**
     * 成功响应第三方
     */
    public static final String STATUS_RESPONSE_SUC = "3";
    /**
     * 成功接收第三方但为成功响应
     */
    public static final String STATUS_RESPONSE_FAIL = "4";

    private Integer id;
    private String userId;
    private String orderType;
    private String orderNo;
    private String act;
    private String reqParams;
    private String returnParams;
    private Date notifyTime;
    private String notifyParams;
    private Date addTime;
    private String addIp;
    private Date updateTime;
    private String status;

    public Orders(Integer id, Integer userId, String status) {
        super();
        this.id = id;
        this.userId = String.valueOf(userId);
        this.status = status;
    }

    public Orders(String userId, String orderType, String orderNo, String act) {
        super();
        this.userId = userId;
        this.orderType = orderType;
        this.orderNo = orderNo;
        this.act = act;
    }

    public Orders() {
        super();
    }

    public String getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(String notifyParams) {
        this.notifyParams = notifyParams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }

    public String getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(String returnParams) {
        this.returnParams = returnParams;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddIp() {
        return addIp;
    }

    public void setAddIp(String addIp) {
        this.addIp = addIp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RiskOrders [userId=" + userId + ", orderType=" + orderType + ", act=" + act + ", addIp=" + addIp + ", addTime=" + addTime + ", id="
                + id + ", notifyParams=" + notifyParams + ", notifyTime=" + notifyTime + ", orderNo=" + orderNo + ", reqParams=" + reqParams
                + ", returnParams=" + returnParams + ", status=" + status + ", updateTime=" + updateTime + "]";
    }

}
