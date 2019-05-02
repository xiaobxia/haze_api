package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BorrowOrderLoan;

import java.util.HashMap;
import java.util.List;

public interface IBorrowOrderLoanDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BorrowOrderLoan record);

    int insertSelective(BorrowOrderLoan record);

    BorrowOrderLoan selectByParam(HashMap<String, Object> params);

    BorrowOrderLoan selectByPrimaryKey(Integer id);

    List<BorrowOrderLoan> findParams(HashMap<String, Object> params);

    int updateByPrimaryKeySelective(BorrowOrderLoan record);

    int updateByPrimaryKey(BorrowOrderLoan record);

    /**
     * 根据订单号批量修改
     *
     * @param record
     * @return
     */
    int updateByYurref(BorrowOrderLoan record);

    /**
     * 根据条件更新
     *
     * @param params
     * @return
     */
    int updateByParams(HashMap<String, Object> params);


}