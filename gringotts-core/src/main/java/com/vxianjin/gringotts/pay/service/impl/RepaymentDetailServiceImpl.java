package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.dao.IRepaymentDetailDao;
import com.vxianjin.gringotts.web.pojo.RepaymentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service
public class RepaymentDetailServiceImpl implements RepaymentDetailService {

    @Autowired
    private IRepaymentDetailDao repaymentDetailDao;


    @Override
    public RepaymentDetail selectByPrimaryKey(Integer id) {
        return repaymentDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public RepaymentDetail selectByOrderId(String orderId) {
        return repaymentDetailDao.selectByOrderId(orderId);
    }

    @Override
    public boolean deleteByPrimaryKey(Integer id) {
        return repaymentDetailDao.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean insert(RepaymentDetail detail) {
        return repaymentDetailDao.insert(detail) > 0;
    }

    @Override
    @Transactional
    public boolean insertSelective(RepaymentDetail detail) {
        return repaymentDetailDao.insertSelective(detail) > 0;
    }

    @Override
    public boolean updateByPrimaryKey(RepaymentDetail detail) {
        return repaymentDetailDao.updateByPrimaryKey(detail) > 0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(RepaymentDetail detail) {
        return repaymentDetailDao.updateByPrimaryKeySelective(detail) > 0;
    }

    @Override
    public boolean updateDetailStatusAndRemark(int detailId, int status, String remark) {
        RepaymentDetail newDetail = new RepaymentDetail();
        newDetail.setId(detailId);
        newDetail.setStatus(status);
        newDetail.setRemark(remark);
        return updateByPrimaryKeySelective(newDetail);
    }

    @Override
    public List<RepaymentDetail> queryOrderResult(HashMap<String, Object> params) {
        return repaymentDetailDao.queryOrderResult(params);
    }

    @Override
    public List<RepaymentDetail> queryOrderResultForSYN(HashMap<String, Object> params) {
        return repaymentDetailDao.queryOrderResultForSYN(params);
    }

}
