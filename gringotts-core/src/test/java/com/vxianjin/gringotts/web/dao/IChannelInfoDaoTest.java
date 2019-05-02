package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.ChannelInfo;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IChannelInfoDaoTest extends AbstractDaoTest {

    @Autowired
    IChannelInfoDao channelInfoDao;

    @Override
    public void compileMapper() {
        ChannelInfo item;

        int id = 639;
        item = channelInfoDao.findById(id);
        assert item != null;
        item.setCreatedAt(new Date());
        Date d1 = item.getUpdatedAt();
        channelInfoDao.updateById(item);
        item = channelInfoDao.findById(id);
        Date d2 = item.getUpdatedAt();
        Assert.assertTrue(String.format("更新时间戳:%d,%d", d1.getTime(), d2.getTime()), d1.getTime() != d2.getTime());
    }
}
