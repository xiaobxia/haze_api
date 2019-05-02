package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.pay.dao.IRenewalRecordDao;
import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.RenewalRecord;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IRenewalRecordDaoTest extends AbstractDaoTest {
    @Autowired
    IRenewalRecordDao renewalRecordDao;

    @Override
    public void compileMapper() {
        RenewalRecord item;

        int id = 22;
        item = renewalRecordDao.selectByPrimaryKey(id);
        assert item != null;
        assert item.getRenewalType() != null;
        assert item.getUpdatedAt() != null;
        item.setRemark(item.getRemark() + "1");
        Date d1 = item.getUpdatedAt();
        int rows = renewalRecordDao.updateByPrimaryKeyWithBLOBs(item);
        assert rows > 0 : "Update failed";
        item = renewalRecordDao.selectByPrimaryKey(id);
        Date d2 = item.getUpdatedAt();
        assert item.getRenewalType() != null;
        assert item.getUpdatedAt() != null;

        Assert.assertTrue(String.format("更新时间戳:%d,%d", d1.getTime(), d2.getTime()), d1.getTime() < d2.getTime());

    }
}
