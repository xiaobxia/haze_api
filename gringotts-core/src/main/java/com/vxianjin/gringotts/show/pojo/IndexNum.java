package com.vxianjin.gringotts.show.pojo;

import java.math.BigDecimal;

public class IndexNum {
    private Integer personTotal;// 全国放贷人数
    private BigDecimal borrowTotal;// 历史总金额
    private Integer waitPerson;// 等待放贷人数
    private BigDecimal waitMoney;// 等待放贷金额
    private BigDecimal borrowToday;// 当天放贷金额
    private Integer borrowTodayCnt;// 当天已放款笔数
    private Integer borrowTotalToday;// 当天申请总数
    private Integer borrowSucToday;// 当天通过审核订单数

    public IndexNum(Integer personTotal, BigDecimal borrowTotal, Integer waitPerson, BigDecimal waitMoney, BigDecimal borrowToday,
                    Integer borrowTodayCnt, Integer borrowTotalToday, Integer borrowSucToday) {
        super();
        this.personTotal = personTotal;
        this.borrowTotal = borrowTotal;
        this.waitPerson = waitPerson;
        this.waitMoney = waitMoney;
        this.borrowToday = borrowToday;
        this.borrowTodayCnt = borrowTodayCnt;
        this.borrowTotalToday = borrowTotalToday;
        this.borrowSucToday = borrowSucToday;
    }

    public IndexNum() {
        super();
    }

    public Integer getBorrowTodayCnt() {
        return borrowTodayCnt;
    }

    public void setBorrowTodayCnt(Integer borrowTodayCnt) {
        this.borrowTodayCnt = borrowTodayCnt;
    }

    public BigDecimal getWaitMoney() {
        return waitMoney;
    }

    public void setWaitMoney(BigDecimal waitMoney) {
        this.waitMoney = waitMoney;
    }

    public Integer getBorrowTotalToday() {
        return borrowTotalToday;
    }

    public void setBorrowTotalToday(Integer borrowTotalToday) {
        this.borrowTotalToday = borrowTotalToday;
    }

    public Integer getBorrowSucToday() {
        return borrowSucToday;
    }

    public void setBorrowSucToday(Integer borrowSucToday) {
        this.borrowSucToday = borrowSucToday;
    }

    public Integer getPersonTotal() {
        return personTotal;
    }

    public void setPersonTotal(Integer personTotal) {
        this.personTotal = personTotal;
    }

    public Integer getWaitPerson() {
        return waitPerson;
    }

    public void setWaitPerson(Integer waitPerson) {
        this.waitPerson = waitPerson;
    }

    public BigDecimal getBorrowTotal() {
        return borrowTotal;
    }

    public void setBorrowTotal(BigDecimal borrowTotal) {
        this.borrowTotal = borrowTotal;
    }

    public BigDecimal getBorrowToday() {
        return borrowToday;
    }

    public void setBorrowToday(BigDecimal borrowToday) {
        this.borrowToday = borrowToday;
    }

}
