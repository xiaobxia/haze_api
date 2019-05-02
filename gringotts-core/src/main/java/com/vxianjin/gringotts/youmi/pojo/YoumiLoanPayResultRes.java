package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */

public class YoumiLoanPayResultRes {
    /**
     * 订单号
     */
    private String order_no;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 放款失败的说明信息
     */
    private String fail_reason;

    /**
     * 更新时间(10位时间戳)
     */
    private String updated_at;

    public YoumiLoanPayResultRes() {
    }

    public YoumiLoanPayResultRes(String order_no, String status, String fail_reason, String updated_at) {
        this.order_no = order_no;
        this.status = status;
        this.fail_reason = fail_reason;
        this.updated_at = updated_at;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
