package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.pay.dao.IRenewalRecordDao;
import com.vxianjin.gringotts.pay.model.BackExtend;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.web.dao.IBackExtendDao;
import com.vxianjin.gringotts.web.pojo.RenewalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class RenewalRecordServiceImpl implements RenewalRecordService {

    @Autowired
    private IRenewalRecordDao renewalRecordDao;

    @Autowired
    private IBackExtendDao backExtendDao;

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

    @Override
    public boolean borrowOrderRenewalRecordFlag(Integer borrowOrderId) {
        int renewalCount = renewalRecordDao.borrowOrderRenewalRecordCount(borrowOrderId);//该笔订单的续期次数
        BackExtend backExtend = backExtendDao.selectByBorrowOrderId(borrowOrderId);//该订单对应的续期配置参数对象

        if (backExtend != null) {
            return renewalCount >= backExtend.getExtendCount();//续期次数大于配置的参数对象，返回true，不可继续操作续期，直接返回前台，不走后续
        }
        return false;
    }
}
