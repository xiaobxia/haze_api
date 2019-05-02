package com.vxianjin.gringotts.web.pojo.risk;

public class ZhimiContact {

    private String contact_name;
    private String contact_phone;
    private String update_time;

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public ZhimiContact(String contact_name, String contact_phone, String update_time) {
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.update_time = update_time;
    }

    public ZhimiContact() {
    }

}
