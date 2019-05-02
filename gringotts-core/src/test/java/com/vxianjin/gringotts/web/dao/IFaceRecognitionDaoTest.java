package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.AbstractDaoTest;
import com.vxianjin.gringotts.web.pojo.FaceRecognition;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IFaceRecognitionDaoTest extends AbstractDaoTest {

    @Autowired
    IFaceRecognitionDao faceRecognitionDao;

    @Override
    public void compileMapper() {
        FaceRecognition item;

        int id = 434487;
        item = faceRecognitionDao.selectByUserId(id);
        assert item != null;
        assert item.getCreateTime() != null : "create time is null";
        assert item.getUpdatetime() != null : "update time is null";
        assert item.getStatus() != null : "status is null";
        item.setCreateTime(new Date());
        Date d1 = item.getUpdatetime();
        faceRecognitionDao.updateFaceRecognition(item);
        item = faceRecognitionDao.selectByUserId(id);
        Date d2 = item.getUpdatetime();
        Assert.assertTrue(String.format("更新时间戳:%d,%d", d1.getTime(), d2.getTime()), d1.getTime() != d2.getTime());

    }
}
