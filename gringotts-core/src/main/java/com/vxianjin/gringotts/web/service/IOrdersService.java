package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.Orders;

public interface IOrdersService {
    /**
     * 插入借点钱订单信息，用于匹配双方订单号
     *
     * @param orders
     * @return
     */
    int insertJdq(Orders orders);
}
