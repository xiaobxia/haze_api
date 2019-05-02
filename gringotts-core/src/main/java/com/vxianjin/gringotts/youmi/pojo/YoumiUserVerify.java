package com.vxianjin.gringotts.youmi.pojo;

/**
 * 描述:
 * youmi userVerify
 *
 * @author zed
 * @since 2019-01-30 11:03 AM
 */
public class YoumiUserVerify {
    /**
     *  运营商原始数据（聚信立）
     */
    private YoumiOperatorVerify operator_verify;
    /**
     * 运营商基础报告（聚信立）
     */
    private YoumiOperatorReportVerify operator_report_verify;

    public YoumiOperatorVerify getOperator_verify() {
        return operator_verify;
    }

    public void setOperator_verify(YoumiOperatorVerify operator_verify) {
        this.operator_verify = operator_verify;
    }

    public YoumiOperatorReportVerify getOperator_report_verify() {
        return operator_report_verify;
    }

    public void setOperator_report_verify(YoumiOperatorReportVerify operator_report_verify) {
        this.operator_report_verify = operator_report_verify;
    }
}

