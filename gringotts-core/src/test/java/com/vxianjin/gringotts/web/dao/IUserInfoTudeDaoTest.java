package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.attach.dao.IUserInfoTudeDao;
import com.vxianjin.gringotts.web.AbstractDaoTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: chenjunqi
 * @Date: 2018/6/27
 * @Description:
 */
public class IUserInfoTudeDaoTest extends AbstractDaoTest {

    @Autowired
    IUserInfoTudeDao userInfoTudeDao;

    @Override
    public void compileMapper() {
        assert true : "Missing basic services";
    }
}
