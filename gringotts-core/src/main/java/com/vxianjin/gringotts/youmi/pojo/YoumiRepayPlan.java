package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 14:32
 */

public class YoumiRepayPlan {
    /**
     * 本期应还金额，单位: 分
     */
    private Integer repay_amount;
    /**
     * 本期应还金额描述
     */
    private String repay_amount_desc;
    /**
     * 本期还款本金
     */
    private Integer principal;
    /**
     * 本期服务费
     */
    private Integer service_fee;
    /**
     * 本期利息
     */
    private Integer interest;
    /**
     * 本期应还时间
     */
    private Integer repay_time;

    public YoumiRepayPlan() {
    }

    public YoumiRepayPlan(Integer repay_amount, String repay_amount_desc, Integer principal, Integer service_fee, Integer interest, Integer repay_time) {
        this.repay_amount = repay_amount;
        this.repay_amount_desc = repay_amount_desc;
        this.principal = principal;
        this.service_fee = service_fee;
        this.interest = interest;
        this.repay_time = repay_time;
    }

    public Integer getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(Integer repay_amount) {
        this.repay_amount = repay_amount;
    }

    public String getRepay_amount_desc() {
        return repay_amount_desc;
    }

    public void setRepay_amount_desc(String repay_amount_desc) {
        this.repay_amount_desc = repay_amount_desc;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    public Integer getService_fee() {
        return service_fee;
    }

    public void setService_fee(Integer service_fee) {
        this.service_fee = service_fee;
    }

    public Integer getInterest() {
        return interest;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Integer getRepay_time() {
        return repay_time;
    }

    public void setRepay_time(Integer repay_time) {
        this.repay_time = repay_time;
    }
}
