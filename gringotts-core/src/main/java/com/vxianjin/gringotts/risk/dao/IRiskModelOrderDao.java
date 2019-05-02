package com.vxianjin.gringotts.risk.dao;


import com.vxianjin.gringotts.risk.pojo.RiskModelOrder;

/**
 * @author xiefei
 * @date 2018/05/12
 */
public interface IRiskModelOrderDao {
    int insert(RiskModelOrder riskModelOrder);

    int deleteByBorrowOrderId(Integer borrowOrderId);
}
