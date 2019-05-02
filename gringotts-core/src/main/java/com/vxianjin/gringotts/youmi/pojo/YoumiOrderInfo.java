package com.vxianjin.gringotts.youmi.pojo;

/**
 * 描述:
 * youmi 订单信息
 *
 * @author zed
 * @since 2019-01-30 11:01 AM
 */
public class YoumiOrderInfo {
    /**
     * 进件单号
     */
    private String order_no;
    /**
     * 产品ID
     */
    private String product_id;
    /**
     * 进件单来源
     */
    private String order_from;
    /**
     * 借款金额(单位：分)
     */
    private String amount;
    /**
     * 借款期限
     */
    private int term;
    /**
     * 期限类型(1: 按天 2: 按月 3: 按年)
     */
    private int term_type;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_from() {
        return order_from;
    }

    public void setOrder_from(String order_from) {
        this.order_from = order_from;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getTerm_type() {
        return term_type;
    }

    public void setTerm_type(int term_type) {
        this.term_type = term_type;
    }
}

