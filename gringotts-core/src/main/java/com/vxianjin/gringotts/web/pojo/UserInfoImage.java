package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class UserInfoImage implements Serializable {
    private static final long serialVersionUID = 2141531489255123398L;
    public static HashMap<Object, String> typeMap = new HashMap<Object, String>();

    static {
        typeMap.put(1, "身份证");
        typeMap.put(2, "学历证明");
        typeMap.put(3, "工作证明");
        typeMap.put(4, "收入证明");
        typeMap.put(5, "财产证明");
        typeMap.put(6, "工牌照片");
        typeMap.put(7, "个人名片");
        typeMap.put(8, "银行卡");
        typeMap.put(9, "好房贷房产证");
        typeMap.put(100, "好房贷房产证");
    }

    private String id;
    private String userId;
    private String type;
    private String status;
    private String picName;
    private String url;
    private Date createdDate;
    private Date updatedDate;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
