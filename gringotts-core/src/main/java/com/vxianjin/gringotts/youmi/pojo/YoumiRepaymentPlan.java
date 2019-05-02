package com.vxianjin.gringotts.youmi.pojo;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */


public class YoumiRepaymentPlan {
    /**
     * 还款期号
     */
    private String period_no;
    /**
     * 本期还款利息 单位: 分
     */
    private Integer principal;
    /**
     * 本期还款利息 单位: 分
     */
    private Integer interest;
    /**
     * 本期服务费用 单位: 分
     */
    private Integer service_fee;
    /**
     * 本期账单状态，0：未出账，199: 未还款 ，200: 还款成功， 201: 部分还款， 400：还款失败
     */
    private Integer bill_status;
    /**
     * 本期还款总额 单位: 分
     */
    private Integer total_amount;
    /**
     * 	本期已还金额 单位: 分
     */
    private Integer already_paid;
    /**
     * 实际起息时间(10位时间戳)
     */
    private Integer loan_time;
    /**
     * 最迟还款时间（10位时间戳，精确到秒超过该时间就算逾期）
     */
    private Integer due_time;
    /**
     * 可以还款时间(10位时间戳)
     */
    private Integer can_pay_time;
    /**
     * 实际完成还款时间(10位时间戳)
     */
    private Integer finish_pay_time;
    /**
     * 逾期天数
     */
    private Integer overdue_day;
    /**
     * 逾期费用 单位: 分
     */
    private Integer overdue_fee;
    /**
     * 本期费用描述，如：第三期还款费用明细
     */
    private String period_fee_desc;
    /**
     * 还款支付方式; 如: 0.未还款 1. 主动还款 2.系统扣款 3. 支付宝转账 4. 银行转账 5.线下或其它方式
     */
    private Integer pay_type;

    public YoumiRepaymentPlan() {
    }

    public YoumiRepaymentPlan(String period_no, Integer principal, Integer interest, Integer service_fee, Integer bill_status, Integer total_amount, Integer already_paid, Integer loan_time, Integer due_time, Integer can_pay_time, Integer finish_pay_time, Integer overdue_day, Integer overdue_fee, String period_fee_desc, Integer pay_type) {
        this.period_no = period_no;
        this.principal = principal;
        this.interest = interest;
        this.service_fee = service_fee;
        this.bill_status = bill_status;
        this.total_amount = total_amount;
        this.already_paid = already_paid;
        this.loan_time = loan_time;
        this.due_time = due_time;
        this.can_pay_time = can_pay_time;
        this.finish_pay_time = finish_pay_time;
        this.overdue_day = overdue_day;
        this.overdue_fee = overdue_fee;
        this.period_fee_desc = period_fee_desc;
        this.pay_type = pay_type;
    }

    public String getPeriod_no() {
        return period_no;
    }

    public void setPeriod_no(String period_no) {
        this.period_no = period_no;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    public Integer getInterest() {
        return interest;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Integer getService_fee() {
        return service_fee;
    }

    public void setService_fee(Integer service_fee) {
        this.service_fee = service_fee;
    }

    public Integer getBill_status() {
        return bill_status;
    }

    public void setBill_status(Integer bill_status) {
        this.bill_status = bill_status;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getAlready_paid() {
        return already_paid;
    }

    public void setAlready_paid(Integer already_paid) {
        this.already_paid = already_paid;
    }

    public Integer getLoan_time() {
        return loan_time;
    }

    public void setLoan_time(Integer loan_time) {
        this.loan_time = loan_time;
    }

    public Integer getDue_time() {
        return due_time;
    }

    public void setDue_time(Integer due_time) {
        this.due_time = due_time;
    }

    public Integer getCan_pay_time() {
        return can_pay_time;
    }

    public void setCan_pay_time(Integer can_pay_time) {
        this.can_pay_time = can_pay_time;
    }

    public Integer getFinish_pay_time() {
        return finish_pay_time;
    }

    public void setFinish_pay_time(Integer finish_pay_time) {
        this.finish_pay_time = finish_pay_time;
    }

    public Integer getOverdue_day() {
        return overdue_day;
    }

    public void setOverdue_day(Integer overdue_day) {
        this.overdue_day = overdue_day;
    }

    public Integer getOverdue_fee() {
        return overdue_fee;
    }

    public void setOverdue_fee(Integer overdue_fee) {
        this.overdue_fee = overdue_fee;
    }

    public String getPeriod_fee_desc() {
        return period_fee_desc;
    }

    public void setPeriod_fee_desc(String period_fee_desc) {
        this.period_fee_desc = period_fee_desc;
    }

    public Integer getPay_type() {
        return pay_type;
    }

    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }
}
