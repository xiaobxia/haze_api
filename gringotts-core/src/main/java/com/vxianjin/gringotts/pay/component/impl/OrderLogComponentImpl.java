package com.vxianjin.gringotts.pay.component.impl;

import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.OrderLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

/**
 * @Author: chenkai
 * @Date: 2018/8/27 11:40
 * @Description:
 */
@Service
public class OrderLogComponentImpl implements OrderLogComponent {

    private static final Logger logger = LoggerFactory.getLogger(OrderLogComponentImpl.class);

    @Autowired
    private OrderLogService orderLogService;

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNewOrderLog(OrderLogModel orderLogModel) {
        try{
            orderLogService.addNewOrderChangeLog(orderLogModel);
        }catch (PayException e){
            logger.error(MessageFormat.format("订单状态更新日志记录失败,borrowId: {0},repaymentId: {1},失败原因:{2}",orderLogModel.getBorrowId(),orderLogModel.getOperateId(),e.getMessage()));
        }
    }
}
