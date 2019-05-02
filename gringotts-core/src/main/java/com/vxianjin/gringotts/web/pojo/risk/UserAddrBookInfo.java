package com.vxianjin.gringotts.web.pojo.risk;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 21:06
 */

public class UserAddrBookInfo {
    /**
     * 首字母小写+id
     */
    private String consumerNo;
    /**
     * 用户通讯录
     */
    private String data;
    /**
     * 紧急联系人
     */
    private String contactInfo;

    public UserAddrBookInfo() {
    }

    public UserAddrBookInfo(String consumerNo, String data, String contactInfo) {
        this.consumerNo = consumerNo;
        this.data = data;
        this.contactInfo = contactInfo;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}

