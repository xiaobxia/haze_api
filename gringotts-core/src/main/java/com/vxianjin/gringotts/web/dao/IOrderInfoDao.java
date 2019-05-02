package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.OrderInfo;
import com.vxianjin.gringotts.web.pojo.OrderInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface IOrderInfoDao {
    long countByExample(OrderInfoExample example);

    int deleteByExample(OrderInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    List<OrderInfo> selectByExample(OrderInfoExample example);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderInfo record, @Param("example") OrderInfoExample example);

    int updateByExample(@Param("record") OrderInfo record, @Param("example") OrderInfoExample example);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);


    /**
     * 根据订单ID更新订单
     *
     * @param order
     */
    int updateByOrderId(OrderInfo order);

    /**
     * 根据本方ord或易旨ord获取订单信息
     *
     * @return
     */
    OrderInfo selectByOrdOrYzOrd(HashMap<String, String> params);
}