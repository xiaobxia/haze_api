package com.vxianjin.gringotts.youmi.pojo;

import org.springframework.web.bind.annotation.PostMapping;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:36
 */

public class YoumiApplyLoad {

    /**
     * 进件单号
     */
    private String order_no;

    /**
     * 借款金额 (分)
     */
    private String amount;

    /**
     * 借款期限
     */
    private String period;

    /**
     * 借款单位（1：天 2：月 3：年）
     */
    private Integer term_type;

    public YoumiApplyLoad() {
    }

    public YoumiApplyLoad(String order_no, String amount, String period, Integer term_type) {
        this.order_no = order_no;
        this.amount = amount;
        this.period = period;
        this.term_type = term_type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getTerm_type() {
        return term_type;
    }

    public void setTerm_type(Integer term_type) {
        this.term_type = term_type;
    }
}
