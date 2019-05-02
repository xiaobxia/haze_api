package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IBorrowContractInfoDao;
import com.vxianjin.gringotts.web.pojo.BorrowContractInfo;
import com.vxianjin.gringotts.web.service.IBorrowContractInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowContractInfoService implements IBorrowContractInfoService {

    @Autowired
    private IBorrowContractInfoDao borrowCcontractInfoDao;

    @Override
    public List<BorrowContractInfo> findBorrowContractInfo(
            BorrowContractInfo params) {
        return borrowCcontractInfoDao.findBorrowContractInfo(params);
    }

}
