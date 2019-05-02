package com.vxianjin.gringotts.pay.dao;

import com.vxianjin.gringotts.web.pojo.RepaymentDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRepaymentDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RepaymentDetail record);

    int insertSelective(RepaymentDetail record);

    RepaymentDetail selectByPrimaryKey(Integer id);

    RepaymentDetail selectByOrderId(String orderId);

    int updateByPrimaryKeySelective(RepaymentDetail record);

    int updateByPrimaryKeyWithBLOBs(RepaymentDetail record);

    int updateByPrimaryKey(RepaymentDetail record);

    List<RepaymentDetail> findParams(Map<String, Object> map);

    /**
     * 查询支付未回调的还款详情
     *
     * @param params
     * @return
     */
    List<RepaymentDetail> queryOrderResult(HashMap<String, Object> params);


    /**
     * 查询支付还未同步的还款
     *
     * @param params
     * @return
     */
    List<RepaymentDetail> queryOrderResultForSYN(HashMap<String,Object> params);
}