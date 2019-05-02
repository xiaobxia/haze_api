package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IBorrowOrderDaoTest extends AbstractDaoTest {

    @Autowired
    IBorrowOrderDao borrowOrderDao;

    @Override
    public void compileMapper() {

        final int primaryId = 228;

        BorrowOrder item = borrowOrderDao.selectByPrimaryKey(primaryId);
        assert item != null : "Data not found";
        item.setCreatedAt(new Date());
        Date d1 = item.getUpdatedAt();
        borrowOrderDao.updateByPrimaryKey(item);
        item = borrowOrderDao.selectByPrimaryKey(primaryId);
        Date d2 = item.getUpdatedAt();

        assert d2.getTime() > d1.getTime() : "Update trigger fault";
        System.out.println("success");
    }
}
