package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.Orders;
import org.springframework.stereotype.Repository;


@Repository
public interface IOrdersDao {

    /**
     * 插入借点钱订单信息，用于匹配双方订单号
     *
     * @param orders
     * @return
     */
    int insertJdq(Orders orders);
}
