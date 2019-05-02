package com.vxianjin.gringotts.pay.dao;

import com.vxianjin.gringotts.web.pojo.Repayment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRepaymentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Repayment record);

    int insertSelective(Repayment record);

    Repayment selectByPrimaryKey(Integer id);

    int queryCountByAssetOrderId(String assetOrderId);

    List<Repayment> findParams(Map<String, Object> map);

    List<Repayment> findRepaymentParams(Map<String, Object> map);

    int findParamsCount(HashMap<String, Object> params);

    /**
     * 根据借款主键查询还款信息
     *
     * @param borrowId
     * @return
     */
    List<Repayment> findAllByBorrowId(Integer borrowId);

    Map<String, Object> findBorrowLoanTerm(Integer borrowId);

    List<Map<String, Object>> findMyLoan(Map<String, Object> map);

    int updateByPrimaryKeySelective(Repayment record);

    int updateByPrimaryKeyWithBLOBs(Repayment record);

    int updateByPrimaryKey(Repayment record);

    int updateRenewalByPrimaryKey(Repayment re);
}