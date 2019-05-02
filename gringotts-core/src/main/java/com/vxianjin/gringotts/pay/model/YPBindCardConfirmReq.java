package com.vxianjin.gringotts.pay.model;

import java.util.Map;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 16:59
 * @Description: 
 */
public class YPBindCardConfirmReq {
    //绑卡请求编号
    private String requestNo;
    //绑卡验证码
    private String smsCode;
    //用户id
    private String userId;

    //用户姓名
    private String userName;

    //手机号
    private String phone;

    //银行卡号
    private String cardNo;

    //申请订单号
    private String orderNo;

    //请求data
    private Map<String, String> dataMap;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }
}
