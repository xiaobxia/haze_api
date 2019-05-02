package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.BorrowContractInfo;

import java.util.List;

public interface IBorrowContractInfoService {
    /**
     * 查询投资人信息表
     *
     * @param params
     * @return
     */
    List<BorrowContractInfo> findBorrowContractInfo(BorrowContractInfo params);
}
