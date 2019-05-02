package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class BorrowOrderDevice implements Serializable {
    private static final long serialVersionUID = -2347191789566709358L;
    private Integer id;

    private Integer userId;

    private Integer assetBorrowOrderId;

    private String deviceContent;

    private String requestParams;

    private String returnParams;
    private Date createAt;
    private Date updateAt;
    private String status;
    private String remark;

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
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

    public Integer getAssetBorrowOrderId() {
        return assetBorrowOrderId;
    }

    public void setAssetBorrowOrderId(Integer assetBorrowOrderId) {
        this.assetBorrowOrderId = assetBorrowOrderId;
    }

    public String getDeviceContent() {
        return deviceContent;
    }

    public void setDeviceContent(String deviceContent) {
        this.deviceContent = deviceContent;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(String returnParams) {
        this.returnParams = returnParams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
