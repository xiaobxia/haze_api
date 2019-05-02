package com.vxianjin.gringotts.youmi.pojo;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 14:32
 */

public class YoumiTrial {
    /**
     * 订单号
     */
    private String order_no;
    /**
     * 借款金额(单位:分)
     */
    private String amount;
    /**
     * 借款期限
     */
    private Integer term;
    /**
     * 期限类型(1: 按天 2: 按月 3: 按年)
     */
    private Integer term_type;
    /**
     * 产品唯一标识， 如果机构有多个产品，该值可以限定到单个产品
     */
    private String product_id;

    public YoumiTrial() {
    }

    public YoumiTrial(String order_no, String amount, Integer term, Integer term_type, String product_id) {
        this.order_no = order_no;
        this.amount = amount;
        this.term = term;
        this.term_type = term_type;
        this.product_id = product_id;
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

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getTerm_type() {
        return term_type;
    }

    public void setTerm_type(Integer term_type) {
        this.term_type = term_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
