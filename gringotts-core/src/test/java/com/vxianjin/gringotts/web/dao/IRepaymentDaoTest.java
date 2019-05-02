package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.pay.dao.IRepaymentDao;
import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.Repayment;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IRepaymentDaoTest extends AbstractDaoTest {
    @Autowired
    IRepaymentDao repaymentDao;

    @Override
    public void compileMapper() {
        Repayment loan;

        int id = 95;
        loan = repaymentDao.selectByPrimaryKey(id);
        assert loan != null;
        loan.setCreatedAt(new Date());
        Date d1 = loan.getUpdatedAt();
        repaymentDao.updateByPrimaryKey(loan);
        loan = repaymentDao.selectByPrimaryKey(id);
        Date d2 = loan.getUpdatedAt();
        Assert.assertTrue(String.format("更新时间戳:%d,%d", d1.getTime(), d2.getTime()), d1.getTime() != d2.getTime());

    }
}
