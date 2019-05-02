package com.vxianjin.gringotts.youmi.pojo;

/**
 * 描述:
 * youmi UserInfo
 *
 * @author zed
 * @since 2019-01-30 11:02 AM
 */
public class YoumiUserInfo {
    /**
     * 用户姓名
     */
    private String user_name;
    /**
     * 用户手机号
     */
    private String user_phone;
    /**
     * 用户身份证号
     */
    private String id_number;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }
}

