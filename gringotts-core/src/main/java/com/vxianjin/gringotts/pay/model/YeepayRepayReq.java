package com.vxianjin.gringotts.pay.model;

/**
 * 易宝主动还款请求内容
 * Created by jintian on 2018/7/17.
 */
public class YeepayRepayReq {

    /**
     * 借款编号
     */
    private String borrowId;

    /**
     * 银行卡id
     */
    private String bankId;

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
}
