package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */


public class YoumiBindCardListRes {

    /**
     * 参考银行卡列表接口bank_code枚举值
     */
    private String bank_code;
    /**
     * 银行名称
     */
    private String bank_name;
    /**
     * 银行卡号
     */
    private String card_no;
    /**
     * 预留手机号
     */
    private String card_phone;
    /**
     * 是否是默认卡（默认卡最多只能有一张，走存管流程时，需要将用户在第三方存管时绑定的银行卡设置为默认卡）
     */
    private Boolean is_default;
    /**
     * 备注信息
     */
    private String remark;

    public YoumiBindCardListRes() {
    }

    public YoumiBindCardListRes(String bank_code, String bank_name, String card_no, String card_phone, Boolean is_default, String remark) {
        this.bank_code = bank_code;
        this.bank_name = bank_name;
        this.card_no = card_no;
        this.card_phone = card_phone;
        this.is_default = is_default;
        this.remark = remark;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
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

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
