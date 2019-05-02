package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.pay.pojo.OrderLogModel;

/**
 * @Author: chenkai
 * @Date: 2018/8/21 14:09
 * @Description:
 */
public interface OrderLogService {

    /**
     * 新增订单修改日志
     * @param orderLogModel
     */
    public void addNewOrderChangeLog(OrderLogModel orderLogModel);
}
