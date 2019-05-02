package com.vxianjin.gringotts.pay.common.convert;
import com.vxianjin.gringotts.pay.model.OrderChangeLog;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;


/**
 * @Author: chenkai
 * @Date: 2018/8/21 15:51
 * @Description: 类转换工具类
 */
public class OrderLogConvert {

    public static OrderChangeLog convertFromOrderLogModel(OrderLogModel logModel){

        if (logModel == null){
            return null;
        }
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setUserId(logModel.getUserId());
        orderChangeLog.setBorrowId(logModel.getBorrowId());
        orderChangeLog.setOperateId(logModel.getOperateId());
        orderChangeLog.setOperateType(logModel.getOperateType());
        orderChangeLog.setAction(logModel.getAction());
        orderChangeLog.setBeforeStatus(logModel.getBeforeStatus());
        orderChangeLog.setAfterStatus(logModel.getAfterStatus());
        orderChangeLog.setRemark(logModel.getRemark());
        return orderChangeLog;
    }
}
