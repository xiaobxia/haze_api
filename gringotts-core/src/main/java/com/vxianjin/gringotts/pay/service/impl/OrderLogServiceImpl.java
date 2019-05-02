package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.pay.common.convert.OrderLogConvert;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.dao.OrderChangeLogDao;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.OrderLogService;
import com.vxianjin.gringotts.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: chenkai
 * @Date: 2018/8/21 15:13
 * @Description: 订单状态修改日志记录实现类
 */
@Service("orderLogService")
public class OrderLogServiceImpl implements OrderLogService {

    @Resource
    private OrderChangeLogDao orderChangeLogDao;

    @Override
    public void addNewOrderChangeLog(OrderLogModel orderLogModel) {
        orderLogeModelCheck(orderLogModel);
        try{
            orderChangeLogDao.addNewOrderChangeLog(OrderLogConvert.convertFromOrderLogModel(orderLogModel));
        }catch (Exception e){
            throw new PayException("新增订单日志记录失败");
        }
    }


    private void orderLogeModelCheck(OrderLogModel orderLogModel){
        if (orderLogModel == null){
            throw new PayException("订单日志信息不能为空");
        }

        if (StringUtils.isBlank(orderLogModel.getBorrowId())){
            throw new PayException("借款id不能为空");
        }

        if (StringUtils.isBlank(orderLogModel.getUserId())){
            throw new PayException("用户id不能为空");
        }

        if (StringUtils.isBlank(orderLogModel.getOperateType())){
            throw new PayException("业务类型不能为空");
        }

        if(StringUtils.isBlank(orderLogModel.getAfterStatus())){
            throw new PayException("变化后订单状态不能为空");
        }

        if (!OperateType.BORROW.getCode().equals(orderLogModel.getOperateType())){
            if (StringUtils.isBlank(orderLogModel.getOperateId())){
                throw new PayException("非借款业务，业务id不能为空");
            }
            if (StringUtils.isBlank(orderLogModel.getBeforeStatus())){
                throw new PayException("非借款业务，订单变化前状态不能为空");
            }
        }
    }
}
