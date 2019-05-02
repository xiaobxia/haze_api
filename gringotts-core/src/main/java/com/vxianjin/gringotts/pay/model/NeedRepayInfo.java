package com.vxianjin.gringotts.pay.model;

import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.Repayment;
import com.vxianjin.gringotts.web.pojo.User;

/**
 * 需要还款相关信息（该类为后面主动请求扣款服务的，后续可能需要其他信息可自己扩展）
 * Created by jintian on 2018/7/17.
 */
public class NeedRepayInfo {

    /**
     * 是否需要还款
     */
    private boolean needRepay;

    /**
     * 还款信息
     */
    private Repayment repayment;

    /**
     * 需要还款金额
     */
    private Long money;

    /**
     * 借款订单
     */
    private BorrowOrder borrowOrder;

    private User user;

    public boolean isNeedRepay() {
        return needRepay;
    }

    public void setNeedRepay(boolean needRepay) {
        this.needRepay = needRepay;
    }

    public Repayment getRepayment() {
        return repayment;
    }

    public void setRepayment(Repayment repayment) {
        this.repayment = repayment;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public BorrowOrder getBorrowOrder() {
        return borrowOrder;
    }

    public void setBorrowOrder(BorrowOrder borrowOrder) {
        this.borrowOrder = borrowOrder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
