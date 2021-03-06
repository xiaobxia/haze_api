package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.BorrowOrderLoanPerson;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IBorrowOrderLoanPersonDaoTest extends AbstractDaoTest {

    @Autowired
    IBorrowOrderLoanPersonDao borrowOrderLoanPersonDao;

    @Override
    public void compileMapper() {
        BorrowOrderLoanPerson loan;

        int id = 1;
        loan = borrowOrderLoanPersonDao.selectByPrimaryKey(id);
        assert loan != null;
        loan.setPayRemark(loan.getPayRemark() + "1");
        Date d1 = loan.getUpdatedAt();
        borrowOrderLoanPersonDao.updateByPrimaryKey(loan);
        loan = borrowOrderLoanPersonDao.selectByPrimaryKey(id);
        Date d2 = loan.getUpdatedAt();
        Assert.assertTrue(String.format("更新时间戳:%d,%d", d1.getTime(), d2.getTime()), d1.getTime() != d2.getTime());

    }
}
