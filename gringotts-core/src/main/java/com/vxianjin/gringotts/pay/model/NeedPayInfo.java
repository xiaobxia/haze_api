package com.vxianjin.gringotts.pay.model;

import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;

/**
 * 代付相关信息
 * Created by jintian on 2018/7/19.
 */
public class NeedPayInfo {

    private boolean orderSholdPay;

    private UserCardInfo userCardInfo;

    private BorrowOrder borrowOrder;

    private User user;

    public boolean isOrderSholdPay() {
        return orderSholdPay;
    }

    public void setOrderSholdPay(boolean orderSholdPay) {
        this.orderSholdPay = orderSholdPay;
    }

    public UserCardInfo getUserCardInfo() {
        return userCardInfo;
    }

    public void setUserCardInfo(UserCardInfo userCardInfo) {
        this.userCardInfo = userCardInfo;
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
