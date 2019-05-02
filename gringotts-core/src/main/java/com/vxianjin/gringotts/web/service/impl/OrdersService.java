package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IOrdersDao;
import com.vxianjin.gringotts.web.pojo.Orders;
import com.vxianjin.gringotts.web.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrdersService implements IOrdersService {
    @Autowired
    private IOrdersDao ordersDao;


    @Override
    public int insertJdq(Orders orders) {
        return ordersDao.insertJdq(orders);
    }


}
