package com.vxianjin.gringotts.youmi.pojo;

/**
 * 描述:
 * 有米用户
 *
 * @author zed
 * @since 2019-01-30 10:08 AM
 */
public class YoumiCheckUser {
    private String user_name;

    private String product_id;

    private String md5;

    private String id_card;

    private String phone;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

