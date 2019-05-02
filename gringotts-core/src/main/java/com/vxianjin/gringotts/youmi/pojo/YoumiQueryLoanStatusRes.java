package com.vxianjin.gringotts.youmi.pojo;

import java.util.List;

/**
 *@Author Created by Wzw  
 *@Date on 2019/1/31 0031 10:06
 */

public class YoumiQueryLoanStatusRes {
    /**
     * 订单号
     */
    private String order_no;

    /**
     * 订单状态
     */
    private String order_status;

    /**
     *审批后的可借金额（单位，分）
     */
    private String amount;
    /**
     * 审批后的可借周期
     */
    private String term;
    /**
     * 1：按天 2：按月 3：按年
     */
    private String term_type;
    /**
     * 审批状态备注
     */
    private String remark;
    /**
     * 可以再次申请借款的时间，此时间之前将不在请求用户过滤接口（值如：2018-12-25）
     */
    private String can_loan_time;
    /**
     * 订单状态变更时间(10位时间戳)
     */
    private String updated_at;
    /**
     * 分期的筛选额度信息（分期产品需传入）二维数组
     */
    private List<String> installment_info;
    /**
     * 分期的筛选期限
     */
    private Integer installment_term;
    /**
     * 分期筛选额度间隔（单位/分）
     */
    private Integer installment_interval;
    /**
     * 分期的筛选额度（单位/分）。 200000，500000为可筛选额度， 200000为最小值，500000为最大值；如果额度固定不可选，则传相同的值即可，如：200000，200000
     */
    private String installment_amount;

    public YoumiQueryLoanStatusRes() {
    }

    public YoumiQueryLoanStatusRes(String order_no, String order_status, String amount, String term, String term_type, String remark, String can_loan_time, String updated_at, List<String> installment_info, Integer installment_term, Integer installment_interval, String installment_amount) {
        this.order_no = order_no;
        this.order_status = order_status;
        this.amount = amount;
        this.term = term;
        this.term_type = term_type;
        this.remark = remark;
        this.can_loan_time = can_loan_time;
        this.updated_at = updated_at;
        this.installment_info = installment_info;
        this.installment_term = installment_term;
        this.installment_interval = installment_interval;
        this.installment_amount = installment_amount;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm_type() {
        return term_type;
    }

    public void setTerm_type(String term_type) {
        this.term_type = term_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCan_loan_time() {
        return can_loan_time;
    }

    public void setCan_loan_time(String can_loan_time) {
        this.can_loan_time = can_loan_time;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<String> getInstallment_info() {
        return installment_info;
    }

    public void setInstallment_info(List<String> installment_info) {
        this.installment_info = installment_info;
    }

    public Integer getInstallment_term() {
        return installment_term;
    }

    public void setInstallment_term(Integer installment_term) {
        this.installment_term = installment_term;
    }

    public Integer getInstallment_interval() {
        return installment_interval;
    }

    public void setInstallment_interval(Integer installment_interval) {
        this.installment_interval = installment_interval;
    }

    public String getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(String installment_amount) {
        this.installment_amount = installment_amount;
    }
}
