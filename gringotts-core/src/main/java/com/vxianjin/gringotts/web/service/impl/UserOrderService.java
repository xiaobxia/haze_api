package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.dao.IUserOrderDao;
import com.vxianjin.gringotts.web.pojo.UserOrder;
import com.vxianjin.gringotts.web.service.IUserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class UserOrderService implements IUserOrderService {

    @Autowired
    private IUserOrderDao userOrderDao;
    @Autowired
    private IPaginationDao paginationDao;


    @SuppressWarnings("unchecked")
    public PageConfig<UserOrder> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "UserOrder");
        return paginationDao.findPage("findAll", "findAllCount", params, "web");
    }

    @Override
    public int insert(UserOrder userOrder) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(UserOrder userOrder) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(UserOrder userOrder) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public UserOrder findById(Integer id) {
        return userOrderDao.findById(id);
    }

    @Override
    public UserOrder findBankById(Integer id) {
        // TODO Auto-generated method stub
        return userOrderDao.findById(id);
    }

}
