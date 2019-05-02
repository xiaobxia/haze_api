package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class ChannelInfo implements Serializable {

    private static final long serialVersionUID = -4963683518079845025L;
    private Integer id;
    private String channelName;
    private String channelCode;
    private String operatorName;
    private String channelTel;
    private Date createdAt;
    private Date updatedAt;
    private Integer apr;//一级佣金比例
    private String remark;
    private Integer status;
    private String userPhone;
    private User user;
    private String realname;
    private String userTel;
    private String userName;
    private String relPath;
    private Date createTime;


    private String channelProvince;
    private String channelCity;
    private String channelArea;
    private String channelPassword;

    /*渠道商上级*/
    private String channelSuperId;
    private String channelSuperName;
    private String channelSuperCode;

    private Integer rateId;
    private String channelRateName;

    /* 新增的 动态配置推广员  */
    private String apkUrl;
    private String channelTag;
    private String downloadPicUrl;
    private String registerPicUrl;
    private String picCodeNum;
    private String toutiaoConvertId;
    private Long userInfoId;

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public String getDownloadPicUrl() {
        return downloadPicUrl;
    }

    public void setDownloadPicUrl(String downloadPicUrl) {
        this.downloadPicUrl = downloadPicUrl;
    }

    public String getRegisterPicUrl() {
        return registerPicUrl;
    }

    public void setRegisterPicUrl(String registerPicUrl) {
        this.registerPicUrl = registerPicUrl;
    }

    public String getPicCodeNum() {
        return picCodeNum;
    }

    public void setPicCodeNum(String picCodeNum) {
        this.picCodeNum = picCodeNum;
    }

    public String getToutiaoConvertId() {
        return toutiaoConvertId;
    }

    public void setToutiaoConvertId(String toutiaoConvertId) {
        this.toutiaoConvertId = toutiaoConvertId;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getChannelRateName() {
        return channelRateName;
    }

    public void setChannelRateName(String channelRateName) {
        this.channelRateName = channelRateName;
    }

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public String getChannelSuperCode() {
        return channelSuperCode;
    }

    public void setChannelSuperCode(String channelSuperCode) {
        this.channelSuperCode = channelSuperCode;
    }

    public String getChannelSuperName() {
        return channelSuperName;
    }

    public void setChannelSuperName(String channelSuperName) {
        this.channelSuperName = channelSuperName;
    }

    public String getChannelSuperId() {
        return channelSuperId;
    }

    public void setChannelSuperId(String channelSuperId) {
        this.channelSuperId = channelSuperId;
    }

    public String getChannelProvince() {
        return channelProvince;
    }

    public void setChannelProvince(String channelProvince) {
        this.channelProvince = channelProvince;
    }

    public String getChannelCity() {
        return channelCity;
    }

    public void setChannelCity(String channelCity) {
        this.channelCity = channelCity;
    }

    public String getChannelArea() {
        return channelArea;
    }

    public void setChannelArea(String channelArea) {
        this.channelArea = channelArea;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRelPath() {
        return relPath;
    }

    public void setRelPath(String relPath) {
        this.relPath = relPath;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getChannelTel() {
        return channelTel;
    }

    public void setChannelTel(String channelTel) {
        this.channelTel = channelTel;
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

    public Integer getApr() {
        return apr;
    }

    public void setApr(Integer apr) {
        this.apr = apr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }

}
