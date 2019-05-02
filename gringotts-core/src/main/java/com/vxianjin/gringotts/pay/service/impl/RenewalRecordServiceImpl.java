package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.pay.dao.IRenewalRecordDao;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.web.pojo.RenewalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class RenewalRecordServiceImpl implements RenewalRecordService {

    @Autowired
    private IRenewalRecordDao renewalRecordDao;

    @Override
    public RenewalRecord selectByPrimaryKey(Integer id) {
        return renewalRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteByPrimaryKey(Integer id) {
        return renewalRecordDao.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insert(RenewalRecord repayment) {
        return renewalRecordDao.insert(repayment) > 0;
    }

    @Override
    public boolean insertSelective(RenewalRecord repayment) {
        return renewalRecordDao.insertSelective(repayment) > 0;
    }

    @Override
    public boolean updateByPrimaryKey(RenewalRecord repayment) {
        return renewalRecordDao.updateByPrimaryKey(repayment) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(RenewalRecord repayment) {
        return renewalRecordDao.updateByPrimaryKeySelective(repayment) > 0;
    }


    @Override
    public List<RenewalRecord> findParams(Map<String, Object> params) {
        return renewalRecordDao.findParams(params);
    }

    @Override
    public RenewalRecord getRenewalRecordByOrderId(String orderId) {

        return renewalRecordDao.getRenewalRecordByOrderId(orderId);
    }
}
