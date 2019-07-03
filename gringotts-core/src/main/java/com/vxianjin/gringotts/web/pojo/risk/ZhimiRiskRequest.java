package com.vxianjin.gringotts.web.pojo.risk;

import java.util.List;
import java.util.Map;

public class ZhimiRiskRequest {
    private String model_name;
    private String product;
    private String channel;
    private String apply_time;
    private String mobile;
    private String name;
    private String idcard;
    private String phone_os;
    private String user_address;
    private String udcredit_portrait;
    private List<ZhimiEmergencyContact> e_contacts;
    private Map<String, String> carrier_data;
    private List<ZhimiContact> contact;

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone_os() {
        return phone_os;
    }

    public void setPhone_os(String phone_os) {
        this.phone_os = phone_os;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUdcredit_portrait() {
        return udcredit_portrait;
    }

    public void setUdcredit_portrait(String udcredit_portrait) {
        this.udcredit_portrait = udcredit_portrait;
    }

    public List<ZhimiEmergencyContact> getE_contacts() {
        return e_contacts;
    }

    public void setE_contacts(List<ZhimiEmergencyContact> e_contacts) {
        this.e_contacts = e_contacts;
    }

    public Map<String, String> getCarrier_data() {
        return carrier_data;
    }

    public void setCarrier_data(Map<String, String> carrier_data) {
        this.carrier_data = carrier_data;
    }

    public List<ZhimiContact> getContact() {
        return contact;
    }

    public void setContact(List<ZhimiContact> contact) {
        this.contact = contact;
    }

    public ZhimiRiskRequest(String model_name, String product, String channel, String apply_time, String mobile, String name, String idcard, String phone_os, String user_address, List<ZhimiEmergencyContact> e_contacts, Map<String, String> carrier_data, List<ZhimiContact> contact) {
        this.model_name = model_name;
        this.product = product;
        this.channel = channel;
        this.apply_time = apply_time;
        this.mobile = mobile;
        this.name = name;
        this.idcard = idcard;
        this.phone_os = phone_os;
        this.user_address = user_address;
        this.e_contacts = e_contacts;
        this.carrier_data = carrier_data;
        this.contact = contact;
    }

    public ZhimiRiskRequest() {
    }
}
