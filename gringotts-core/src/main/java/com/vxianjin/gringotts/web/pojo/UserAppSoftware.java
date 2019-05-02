package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class UserAppSoftware implements Serializable {
    private static final long serialVersionUID = 215352228437594316L;
    private String id;
    private String appName;  //应用名字
    private String packageName; //应用包名
    private String versionName; //应用版本名
    private String versionCode;  //应用版本号
    private String userId;  //用户uid

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserAppSoftware [appName=" + appName + ", id=" + id
                + ", packageName=" + packageName + ", userId=" + userId
                + ", versionCode=" + versionCode + ", versionName="
                + versionName + "]";
    }


}
