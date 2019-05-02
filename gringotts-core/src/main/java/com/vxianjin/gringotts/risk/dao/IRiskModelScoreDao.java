package com.vxianjin.gringotts.risk.dao;


import com.vxianjin.gringotts.risk.pojo.RiskModelScore;

/**
 * @author xiefei
 * @date 2018/05/12
 */
public interface IRiskModelScoreDao {
    int insert(RiskModelScore riskModelScore);

    int deleteByBorrowOrderId(Integer borrowOrderId);
}
