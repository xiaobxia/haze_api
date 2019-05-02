package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.risk.dao.IRiskOrdersDao;
import com.vxianjin.gringotts.risk.pojo.RiskOrders;
import com.vxianjin.gringotts.web.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiskOrdersService implements IRiskOrdersService {
    @Autowired
    private IRiskOrdersDao ordersDao;


    @Override
    public int insert(RiskOrders orders) {
        if (StringUtils.isBlank(orders.getAddIp())) {
            orders.setAddIp(RequestUtils.getIpAddr());
        }
        return ordersDao.insert(orders);
    }


    @Override
    public int update(RiskOrders orders) {
        return ordersDao.update(orders);
    }


    @Override
    public RiskOrders findById(Integer id) {
        return ordersDao.findById(id);
    }

    @Override
    public RiskOrders selectCreditReportByBorrowId(Integer borrowId) {
        return ordersDao.selectCreditReportByBorrowId(borrowId);
    }
}
