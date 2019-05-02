package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.web.pojo.RepaymentDetail;

import java.util.HashMap;
import java.util.List;

/**
 * 从core提取出来
 * Created by wukun on 2018/1/30
 */
public interface RepaymentDetailService {
    public RepaymentDetail selectByPrimaryKey(Integer id);

    /**
     * 根据订单号查询对象
     *
     * @param orderId
     */
    public RepaymentDetail selectByOrderId(String orderId);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    public boolean deleteByPrimaryKey(Integer id);

    /**
     * 插入 对象
     *
     * @param detail
     */
    public boolean insert(RepaymentDetail detail);

    /**
     * 插入 对象
     *
     * @param detail
     */
    public boolean insertSelective(RepaymentDetail detail);

    /**
     * 更新 象
     *
     * @param detail
     */
    public boolean updateByPrimaryKey(RepaymentDetail detail);

    /**
     * 更新 象
     *
     * @param detail
     */
    public boolean updateByPrimaryKeySelective(RepaymentDetail detail);

    /**
     * 更新还款详情的状态和备注
     * @param detailId 详情编号
     * @param status 状态
     * @param remark 备注信息
     * @return
     */
    public boolean updateDetailStatusAndRemark(int detailId,int status,String remark);

    /**
     * 查询支付未回调的还款详情
     *
     * @param params
     * @return
     */
    List<RepaymentDetail> queryOrderResult(HashMap<String, Object> params);


    /**
     * 捞取用户还款正在处理中的数据
     */
    List<RepaymentDetail> queryOrderResultForSYN(HashMap<String, Object> params);
}
