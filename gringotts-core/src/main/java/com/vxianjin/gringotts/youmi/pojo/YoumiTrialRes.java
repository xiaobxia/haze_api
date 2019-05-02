package com.vxianjin.gringotts.youmi.pojo;

import java.util.List;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 14:32
 */

public class YoumiTrialRes {
    /**
     * 总服务费用
     */
    private Integer service_fee;
    /**
     * 服务费说明（例如：包含30%风控服务费，40%信息认证费）
     */
    private String service_fee_desc;
    /**
     * 利息
     */
    private Integer interest_fee;
    /**
     * 实际到帐金额
     */
    private Integer actual_amount;
    /**
     * 总还款额
     */
    private Integer repay_amount;
    /**
     * 预计还款计划
     */
    private List<YoumiRepayPlan> repayPlans;

    public YoumiTrialRes() {
    }

    public YoumiTrialRes(Integer service_fee, String service_fee_desc, Integer interest_fee, Integer actual_amount, Integer repay_amount, List<YoumiRepayPlan> repayPlans) {
        this.service_fee = service_fee;
        this.service_fee_desc = service_fee_desc;
        this.interest_fee = interest_fee;
        this.actual_amount = actual_amount;
        this.repay_amount = repay_amount;
        this.repayPlans = repayPlans;
    }

    public Integer getService_fee() {
        return service_fee;
    }

    public void setService_fee(Integer service_fee) {
        this.service_fee = service_fee;
    }

    public String getService_fee_desc() {
        return service_fee_desc;
    }

    public void setService_fee_desc(String service_fee_desc) {
        this.service_fee_desc = service_fee_desc;
    }

    public Integer getInterest_fee() {
        return interest_fee;
    }

    public void setInterest_fee(Integer interest_fee) {
        this.interest_fee = interest_fee;
    }

    public Integer getActual_amount() {
        return actual_amount;
    }

    public void setActual_amount(Integer actual_amount) {
        this.actual_amount = actual_amount;
    }

    public Integer getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(Integer repay_amount) {
        this.repay_amount = repay_amount;
    }

    public List<YoumiRepayPlan> getRepayPlans() {
        return repayPlans;
    }

    public void setRepayPlans(List<YoumiRepayPlan> repayPlans) {
        this.repayPlans = repayPlans;
    }
}
