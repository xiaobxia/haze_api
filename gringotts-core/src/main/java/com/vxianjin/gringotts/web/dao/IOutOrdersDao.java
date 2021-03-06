package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.OutOrders;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IOutOrdersDao {
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
     * 根据主键查询
     *
     * @param id
     * @return
     */
    OutOrders findById(Integer id);

    /**
     * 查询
     *
     * @param orderNo
     * @return
     */
    OutOrders findByOrderNo(String orderNo);


    /**
     * 根据富友订单号查询
     * @param fuiouOrderId
     * @return
     */
    OutOrders findByFuiouOrderId(String fuiouOrderId);


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
    OutOrders findByIdByTablelastName(@Param("id") Integer id, @Param("TablelastName") String TablelastName);

    /**
     * 查询
     *
     * @param rderNo
     * @return
     */
    OutOrders findByOrderNoByTablelastName(@Param("orderNo") String orderNo, @Param("TablelastName") String TablelastName);
}
