package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.BorrowOrderLoan;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IBorrowOrderLoanDaoTest extends AbstractDaoTest {
    @Autowired
    IBorrowOrderLoanDao borrowOrderLoanDao;

    @Override
    public void compileMapper() {
        BorrowOrderLoan loan;

        int id = 1;
        loan = borrowOrderLoanDao.selectByPrimaryKey(id);
        assert loan != null;
        loan.setPayRemark(loan.getPayRemark() + "1");
        Date d1 = loan.getUpdatedAt();
        borrowOrderLoanDao.updateByPrimaryKey(loan);
        loan = borrowOrderLoanDao.selectByPrimaryKey(id);
        Date d2 = loan.getUpdatedAt();
        Assert.assertTrue(String.format("更新时间戳:%d,%d", d1.getTime(), d2.getTime()), d1.getTime() != d2.getTime());

    }
}
