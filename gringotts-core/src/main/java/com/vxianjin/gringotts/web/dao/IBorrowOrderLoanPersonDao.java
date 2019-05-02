package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BorrowOrderLoanPerson;

import java.util.HashMap;
import java.util.List;

public interface IBorrowOrderLoanPersonDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BorrowOrderLoanPerson record);

    int insertSelective(BorrowOrderLoanPerson record);

    BorrowOrderLoanPerson selectByParam(HashMap<String, Object> params);

    BorrowOrderLoanPerson selectByPrimaryKey(Integer id);

    List<BorrowOrderLoanPerson> findParams(HashMap<String, Object> params);

    int updateByPrimaryKeySelective(BorrowOrderLoanPerson record);

    int updateByPrimaryKey(BorrowOrderLoanPerson record);

    /**
     * 根据订单号批量修改
     *
     * @param record
     * @return
     */
    int updateByYurref(BorrowOrderLoanPerson record);

    /**
     * 根据条件更新
     *
     * @param params
     * @return
     */
    int updateByParams(HashMap<String, Object> params);
}