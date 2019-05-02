package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BorrowContractInfo;

import java.util.List;

public interface IBorrowContractInfoDao {
    /**
     * 查询投资人信息表
     *
     * @param params
     * @return
     */
    List<BorrowContractInfo> findBorrowContractInfo(BorrowContractInfo params);
}