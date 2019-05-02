package com.vxianjin.gringotts.youmi.pojo;


/**
 * 描述:
 * 准入接口返回
 *
 * @author zed
 * @since 2019-01-30 10:22 AM
 */
public class YoumiCheckUserRes {
    /**
     * 借款期限
     */
    private int result;
    /**
     * 最大可贷额度（单位: 分）
     */
    private int max_amount;
    /**
     * 最小可贷额度（单位: 分）
     */
    private int min_amount;
    /**
     * 可贷期限（例如: [7, 14, 30]）
     */
    private int[] terms;
    /**
     * 期限类型（1：按天 2：按月 3：按年）
     */
    private int term_type;
    /**
     * 0：标准流程 1：简化流程
     */
    private int loan_mode;
    /**
     * 其他原因拒绝借款时，此字段说明具体原因
     */
    private String remark;
    /**
     * 如当前没有借款权限，需告知用户在什么时候才可以借款，精确到天即可（例如：2018-12-30）
     */
    private String can_loan_time;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(int max_amount) {
        this.max_amount = max_amount;
    }

    public int getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(int min_amount) {
        this.min_amount = min_amount;
    }

    public int[] getTerms() {
        return terms;
    }

    public void setTerms(int[] terms) {
        this.terms = terms;
    }

    public int getTerm_type() {
        return term_type;
    }

    public void setTerm_type(int term_type) {
        this.term_type = term_type;
    }

    public int getLoan_mode() {
        return loan_mode;
    }

    public void setLoan_mode(int loan_mode) {
        this.loan_mode = loan_mode;
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
}

