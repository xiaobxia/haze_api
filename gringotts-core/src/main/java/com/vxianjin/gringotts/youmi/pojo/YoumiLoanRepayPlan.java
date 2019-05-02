package com.vxianjin.gringotts.youmi.pojo;

import java.util.List;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 19:46
 */

public class YoumiLoanRepayPlan {
    /**
     * 进件单号
     */
    private String order_no;
    /**
     * 还款总额（单位: 分）
     */
    private Integer total_amount;
    /**
     * 总服务费（单位: 分）
     */
    private Integer total_svc_fee;
    /**
     * 	已还金额（单位: 分）
     */
    private Integer already_paid;
    /**
     * 总期数
     */
    private Integer total_period;
    /**
     * 具体还款计划数组
     */
    private List<YoumiRepaymentPlan> repaymentPlans;

    public YoumiLoanRepayPlan() {
    }

    public YoumiLoanRepayPlan(String order_no, Integer total_amount, Integer total_svc_fee, Integer already_paid, Integer total_period, List<YoumiRepaymentPlan> repaymentPlans) {
        this.order_no = order_no;
        this.total_amount = total_amount;
        this.total_svc_fee = total_svc_fee;
        this.already_paid = already_paid;
        this.total_period = total_period;
        this.repaymentPlans = repaymentPlans;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getTotal_svc_fee() {
        return total_svc_fee;
    }

    public void setTotal_svc_fee(Integer total_svc_fee) {
        this.total_svc_fee = total_svc_fee;
    }

    public Integer getAlready_paid() {
        return already_paid;
    }

    public void setAlready_paid(Integer already_paid) {
        this.already_paid = already_paid;
    }

    public Integer getTotal_period() {
        return total_period;
    }

    public void setTotal_period(Integer total_period) {
        this.total_period = total_period;
    }

    public List<YoumiRepaymentPlan> getRepaymentPlans() {
        return repaymentPlans;
    }

    public void setRepaymentPlans(List<YoumiRepaymentPlan> repaymentPlans) {
        this.repaymentPlans = repaymentPlans;
    }
}
