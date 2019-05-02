package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserLoginLog implements Serializable {
    private static final long serialVersionUID = 1046203012428407773L;
    private String id;
    private String userId;
    private String userName;
    private String password;
    private Date loginTime;
    private String loginIp;
    private Date effTime;
    private String token;
    private String equipmentNumber;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getEffTime() {
        return effTime;
    }

    public void setEffTime(Date effTime) {
        this.effTime = effTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    @Override
    public String toString() {
        return "UserLoginLog [effTime=" + effTime + ", equipmentNumber="
                + equipmentNumber + ", id=" + id + ", loginIp=" + loginIp
                + ", loginTime=" + loginTime + ", password=" + password
                + ", token=" + token + ", userId=" + userId + ", userName="
                + userName + "]";
    }


}
