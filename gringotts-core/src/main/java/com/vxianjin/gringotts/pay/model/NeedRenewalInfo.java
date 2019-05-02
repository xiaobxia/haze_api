package com.vxianjin.gringotts.pay.model;

import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.Repayment;
import com.vxianjin.gringotts.web.pojo.User;

/**
 * 续期相关信息
 * Created by jintian on 2018/7/18.
 */
public class NeedRenewalInfo {

    /**
     * 借款订单信息
     */
    private BorrowOrder borrowOrder;

    /**
     * 还款信息
     */
    private Repayment repayment;

    /**
     * 待还总金额
     */
    private Long waitRepay;

    /**
     * 待还滞纳金
     */
    private Long waitLate;

    /**
     * 待还本金
     */
    private Long waitAmount;

    /**
     * 总服务费
     */
    private Long allCount;

    /**
     * 用户
     */
    private User user;

    /**
     * 续期费用
     */
    private String renewalFee;

    /**
     * 服务费
     */
    private Integer loanApr;

    public BorrowOrder getBorrowOrder() {
        return borrowOrder;
    }

    public void setBorrowOrder(BorrowOrder borrowOrder) {
        this.borrowOrder = borrowOrder;
    }

    public Repayment getRepayment() {
        return repayment;
    }

    public void setRepayment(Repayment repayment) {
        this.repayment = repayment;
    }

    public Long getWaitRepay() {
        return waitRepay;
    }

    public void setWaitRepay(Long waitRepay) {
        this.waitRepay = waitRepay;
    }

    public Long getWaitLate() {
        return waitLate;
    }

    public void setWaitLate(Long waitLate) {
        this.waitLate = waitLate;
    }

    public Long getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(Long waitAmount) {
        this.waitAmount = waitAmount;
    }

    public Long getAllCount() {
        return allCount;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(String renewalFee) {
        this.renewalFee = renewalFee;
    }

    public Integer getLoanApr() {
        return loanApr;
    }

    public void setLoanApr(Integer loanApr) {
        this.loanApr = loanApr;
    }
}
