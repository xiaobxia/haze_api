package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class InfoReport implements Serializable {

    private static final long serialVersionUID = 8891066939497532136L;

    private Integer id;
    private String identifierId;
    private String deviceId;
    private String installedName;
    private String installedTime;
    private String netType;
    private String uid;
    private String userName;
    private String appMarket;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentifierId() {
        return identifierId;
    }

    public void setIdentifierId(String identifierId) {
        this.identifierId = identifierId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getInstalledName() {
        return installedName;
    }

    public void setInstalledName(String installedName) {
        this.installedName = installedName;
    }

    public String getInstalledTime() {
        return installedTime;
    }

    public void setInstalledTime(String installedTime) {
        this.installedTime = installedTime;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppMarket() {
        return appMarket;
    }

    public void setAppMarket(String appMarket) {
        this.appMarket = appMarket;
    }

    public String toString() {
        return "InfoReport [appMarket=" + appMarket + ", deviceId=" + deviceId
                + ", id=" + id + ", identifierId=" + identifierId
                + ", installedName=" + installedName + ", installedTime="
                + installedTime + ", netType=" + netType + ", uid=" + uid
                + ", userName=" + userName + "]";
    }

}
