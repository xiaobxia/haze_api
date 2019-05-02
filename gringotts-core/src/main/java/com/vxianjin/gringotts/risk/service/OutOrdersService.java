package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.web.dao.IOutOrdersDao;
import com.vxianjin.gringotts.web.pojo.OutOrders;
import com.vxianjin.gringotts.web.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutOrdersService implements IOutOrdersService {
    private static Logger loger = LoggerFactory.getLogger(OutOrdersService.class);
    @Autowired
    private IOutOrdersDao ordersDao;


    @Override
    public int insert(OutOrders orders) {
        if (StringUtils.isBlank(orders.getAddIp())) {
            orders.setAddIp(RequestUtils.getIpAddr());
        }
        return ordersDao.insert(orders);
    }


    @Override
    public int update(OutOrders orders) {
        return ordersDao.update(orders);
    }


    @Override
    public int updateByOrderNo(OutOrders orders) {
        return ordersDao.updateByOrderNo(orders);
    }

    @Override
    public int updateOrderStatus(String orderNo, String orderStatus) {
        OutOrders newOutOrders = new OutOrders();
        newOutOrders.setOrderNo(orderNo);
        newOutOrders.setStatus(orderStatus);
        return updateByOrderNo(newOutOrders);
    }


    @Override
    public OutOrders findById(Integer id) {
        return ordersDao.findById(id);
    }


    @Override
    public OutOrders findByOrderNo(String orderNo) {
        return ordersDao.findByOrderNo(orderNo);
    }


    @Override
    public int insertByTablelastName(OutOrders orders) {
        return ordersDao.insertByTablelastName(orders);
    }


    @Override
    public int updateByTablelastName(OutOrders orders) {
        return ordersDao.updateByTablelastName(orders);
    }


    @Override
    public int updateByOrderNoByTablelastName(OutOrders orders) {
        return ordersDao.updateByOrderNoByTablelastName(orders);
    }


    @Override
    public OutOrders findByIdByTablelastName(Integer id, String TablelastName) {
        return ordersDao.findByIdByTablelastName(id, TablelastName);
    }


    @Override
    public OutOrders findByOrderNoByTablelastName(String orderNo, String TablelastName) {
        return ordersDao.findByOrderNoByTablelastName(orderNo, TablelastName);
    }
}
