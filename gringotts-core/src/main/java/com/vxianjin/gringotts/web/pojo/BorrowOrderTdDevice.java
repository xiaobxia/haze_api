package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 同盾反欺诈设备指纹类
 */
public class BorrowOrderTdDevice implements Serializable {
    private static final long serialVersionUID = -413629745400863860L;
    private Integer id;
    private Integer assetBorrowOrderId;
    private Integer userId;

    private String deviceContent;
    private String fqzContent;

    private String requestParams;
    private String returnParams;

    private String status; //1 未验证(默认状态) 2 已验证（未通过） 3 通过（以验证）
    private String remark;
    private Date createAt;
    private Date updateAt;

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetBorrowOrderId() {
        return assetBorrowOrderId;
    }

    public void setAssetBorrowOrderId(Integer assetBorrowOrderId) {
        this.assetBorrowOrderId = assetBorrowOrderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDeviceContent() {
        return deviceContent;
    }

    public void setDeviceContent(String deviceContent) {
        this.deviceContent = deviceContent;
    }

    public String getFqzContent() {
        return fqzContent;
    }

    public void setFqzContent(String fqzContent) {
        this.fqzContent = fqzContent;
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

    @Override
    public String toString() {
        return "BorrowOrderTdDevice{" +
                "id=" + id +
                ", assetBorrowOrderId=" + assetBorrowOrderId +
                ", userId=" + userId +
                ", deviceContent='" + deviceContent + '\'' +
                ", fqzContent='" + fqzContent + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", returnParams='" + returnParams + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
