package com.vxianjin.gringotts.youmi.pojo;

/**
 *@author  Created by Wzw
 *@date  on 2019/1/30 0030 17:51
 */

public class YoumiBindCard {
    /**
     * 进件单号
     */
    private String order_no;
    /**
     * 银行编码
     */
    private String bank_code;
    /**
     * 银行卡号
     */
    private String card_no;
    /**
     * 预留手机号
     */
    private String card_phone;
    /**
     * 注册手机号
     */
    private String user_phone;
    /**
     * 用户姓名
     */
    private String user_name;
    /**
     * 用户身份证
     */
    private String id_card;
    /**
     * 验证码
     */
    private String verify_code;
    /**
     * 银行卡类型 1 信用卡 2 借记卡
     */
    private String card_type;

    public YoumiBindCard() {
    }

    public YoumiBindCard(String order_no, String bank_code, String card_no, String card_phone, String user_phone, String user_name, String id_card, String verify_code, String card_type) {
        this.order_no = order_no;
        this.bank_code = bank_code;
        this.card_no = card_no;
        this.card_phone = card_phone;
        this.user_phone = user_phone;
        this.user_name = user_name;
        this.id_card = id_card;
        this.verify_code = verify_code;
        this.card_type = card_type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_phone() {
        return card_phone;
    }

    public void setCard_phone(String card_phone) {
        this.card_phone = card_phone;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
}
