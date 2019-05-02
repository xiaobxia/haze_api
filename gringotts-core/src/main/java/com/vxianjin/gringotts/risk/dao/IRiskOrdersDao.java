package com.vxianjin.gringotts.risk.dao;

import com.vxianjin.gringotts.risk.pojo.RiskOrders;
import org.springframework.stereotype.Repository;


@Repository
public interface IRiskOrdersDao {
    /**
     * 发出请求
     *
     * @param zbNews
     * @return
     */
    int insert(RiskOrders orders);

    /**
     * 更新
     *
     * @param Integer
     * @return
     */
    int update(RiskOrders orders);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    RiskOrders findById(Integer id);


    int insertCreditReport(RiskOrders riskOrders);

    RiskOrders selectCreditReportByBorrowId(Integer assetBorrowId);

}
