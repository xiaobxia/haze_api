package com.vxianjin.gringotts.youmi.pojo;

import java.util.List;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 20:00
 */

public class YoumiLoanRepayStatus {
    /**
     * 进件单号
     */
    private String order_no;
    /**
     * 还款期数
     */
    private List<Integer> repay_periods;
    /**
     * 还款支付方式 0.未还款 1. 主动还款 2.系统扣款 3. 支付宝转账 4. 银行转账 5.线下或其它方式
     */
    private Integer repay_type;
    /**
     * 还款金额（单位分）
     */
    private Float repay_amount;
    /**
     * 还款状态
     */
    private Integer repay_status;
    /**
     * 还款失败的原因（失败时必填）
     */
    private String fail_reason;
    /**
     * 还款时间(成功时必填，10位时间戳)
     */
    private Integer success_time;

    public YoumiLoanRepayStatus() {
    }

    public YoumiLoanRepayStatus(String order_no, List<Integer> repay_periods, Integer repay_type, Float repay_amount, Integer repay_status, String fail_reason, Integer success_time) {
        this.order_no = order_no;
        this.repay_periods = repay_periods;
        this.repay_type = repay_type;
        this.repay_amount = repay_amount;
        this.repay_status = repay_status;
        this.fail_reason = fail_reason;
        this.success_time = success_time;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public List<Integer> getRepay_periods() {
        return repay_periods;
    }

    public void setRepay_periods(List<Integer> repay_periods) {
        this.repay_periods = repay_periods;
    }

    public Integer getRepay_type() {
        return repay_type;
    }

    public void setRepay_type(Integer repay_type) {
        this.repay_type = repay_type;
    }

    public Float getRepay_amount() {
        return repay_amount;
    }

    public void setRepay_amount(Float repay_amount) {
        this.repay_amount = repay_amount;
    }

    public Integer getRepay_status() {
        return repay_status;
    }

    public void setRepay_status(Integer repay_status) {
        this.repay_status = repay_status;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }

    public Integer getSuccess_time() {
        return success_time;
    }

    public void setSuccess_time(Integer success_time) {
        this.success_time = success_time;
    }
}
