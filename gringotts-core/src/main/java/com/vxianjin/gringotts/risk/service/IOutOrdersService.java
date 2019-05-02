package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.web.pojo.OutOrders;


public interface IOutOrdersService {
    /**
     * 发出请求
     *
     * @param orders
     * @return
     */
    int insert(OutOrders orders);


    /**
     * 更新
     *
     * @param orders
     * @return
     */
    int update(OutOrders orders);

    /**
     * 更新
     *
     * @param orders
     * @return
     */
    int updateByOrderNo(OutOrders orders);

    /**
     * 更新订单状态
     * @param orderNo
     * @param orderStatus
     * @return
     */
    public int updateOrderStatus(String orderNo,String orderStatus);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    OutOrders findById(Integer id);

    /**
     * 查询
     *
     * @param rderNo
     * @return
     */
    OutOrders findByOrderNo(String orderNo);


    /**
     * 通过表名发出请求
     *
     * @param orders
     * @return
     */
    int insertByTablelastName(OutOrders orders);

    /**
     * 通过表名更新
     *
     * @param orders
     * @param TablelastName
     * @return
     */
    int updateByTablelastName(OutOrders orders);

    /**
     * 更新
     *
     * @param orders
     * @return
     */
    int updateByOrderNoByTablelastName(OutOrders orders);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    OutOrders findByIdByTablelastName(Integer id, String TablelastName);

    /**
     * 查询
     *
     * @param rderNo
     * @return
     */
    OutOrders findByOrderNoByTablelastName(String orderNo, String TablelastName);

}
