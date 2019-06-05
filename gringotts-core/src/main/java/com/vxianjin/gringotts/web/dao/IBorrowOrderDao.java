package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.BorrowOrderDevice;
import com.vxianjin.gringotts.web.pojo.BorrowOrderTdDevice;
import com.vxianjin.gringotts.web.pojo.RiskCreditUser;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBorrowOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BorrowOrder record);

    int insertSelective(BorrowOrder record);

    BorrowOrder selectByPrimaryKey(Integer id);

    BorrowOrder selectByParam(HashMap<String, Object> params);

    List<BorrowOrder> findParams(HashMap<String, Object> params);

    int updateByPrimaryKeySelective(BorrowOrder record);

    int updateByPrimaryKeySelectiveAndStatus(BorrowOrder record);

    int updateByPrimaryKeyWithBLOBs(BorrowOrder record);

    int updateByPrimaryKey(BorrowOrder record);

    int insertRiskUser(RiskCreditUser risk);

    int updateRiskCreditUserById(RiskCreditUser riskCreditUser);

    int findParamsCount(HashMap<String, Object> params);

    BorrowOrder selectBorrowOrderUseId(Integer userId);

    BorrowOrder selectBorrowOrderNowUseId(Integer userId);

    /**
     * 根据用户查询申请列表
     *
     * @param userId
     * @return
     */
    List<BorrowOrder> findByUserId(Integer userId);

    /**
     * 查询当前用户最近一次申请借款审核失败的订单
     *
     * @param params
     * @return
     */
    BorrowOrder findAuditFailureOrderByUserId(HashMap<String, Object> params);

    /**
     * 插入用户申请借款的设备指纹信息
     *
     * @param borrowOrderDevice
     * @return
     */
    int insertBorrowOrderDevice(BorrowOrderDevice borrowOrderDevice);

    /**
     * 插入用户申请借款的同盾设备指纹信息
     *
     * @param borrowOrderTdDevice
     * @return
     */
    int insertBorrowOrderTdDevice(BorrowOrderTdDevice borrowOrderTdDevice);

    BorrowOrder selectBorrowByParams(Map<String, Object> params);

    /**
     * 更新订单状态
     * @param id id
     * @param status status
     */
    void updateBorrowStatus(@Param("id") String id, @Param("status") String status);

    List<BorrowOrder> getWaitAiList();

    int getRepaidCount(@Param("userId") int userId, @Param("productId") int productId);
}