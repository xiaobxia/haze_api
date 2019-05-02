package com.vxianjin.gringotts.youmi.pojo;

import java.util.List;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */


public class YoumiLoanRepay {
    /**
     * 进件单号
     */
    private String order_no;
    /**
     * 0：仅还当期 1：多期 2全额还款
     */
    private Integer repay_type;
    /**
     * 还款期数
     */
    private List<Integer> repay_periods;
    /**
     * 扣款验证码，当已发送验证码之后再次请求提交
     */
    private String verify_code;
    /**
     * 银行卡号
     */
    private String card_no;

    public YoumiLoanRepay() {
    }

    public YoumiLoanRepay(String order_no, Integer repay_type, List<Integer> repay_periods, String verify_code, String card_no) {
        this.order_no = order_no;
        this.repay_type = repay_type;
        this.repay_periods = repay_periods;
        this.verify_code = verify_code;
        this.card_no = card_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getRepay_type() {
        return repay_type;
    }

    public void setRepay_type(Integer repay_type) {
        this.repay_type = repay_type;
    }

    public List<Integer> getRepay_periods() {
        return repay_periods;
    }

    public void setRepay_periods(List<Integer> repay_periods) {
        this.repay_periods = repay_periods;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }
}
