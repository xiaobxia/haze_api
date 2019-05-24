package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.RiskCreditUser;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserLimitRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从core中提取出来
 * Created by wukun on 2018/1/30
 */
public interface IBorrowOrderService {
    /**
     * 根据条件查找借款
     *
     * @param params
     * @return
     */
    List<BorrowOrder> findAll(HashMap<String, Object> params);

    /**
     * 根据主键查找一条
     *
     * @return
     */
    BorrowOrder findOneBorrow(Integer id);

    /**
     * 代付后更新借款信息
     *
     * @param borrowOrder
     * @param code
     * @param desc
     */
    void updateLoanNew(BorrowOrder borrowOrder, String code, String desc);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 插入 对象
     */
    void insert(BorrowOrder borrowOrder);

    /**
     * 更新 象
     */
    void updateById(BorrowOrder borrowOrder);

    /**
     * 更新 象
     */
    void authBorrowState(BorrowOrder borrowOrder);

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    PageConfig<BorrowOrder> findPage(HashMap<String, Object> params);

    //提额需求
    Map<String, Object> saveLoanV2(Map<String, String> params, User user);

    /*Map<String, Object> saveLoan(Map<String, String> params, User user);*/

//    /**
//     * 计算用户借款服务费
//     *
//     * @param money 单位：分
//     * @return
//     */
//    Map<String, Integer> calculateApr(Integer money, Integer period,BigDecimal apr);

    //************************用户额度管理

    /**
     * 提額
     *
     * @param user
     */
    void addUserLimit(User user);

    /**
     * 修改额度
     *
     * @param mapParam userId，newAmountMax
     */
    void changeUserLimit(HashMap<String, Object> mapParam);

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    PageConfig<UserLimitRecord> finduserLimitPage(HashMap<String, Object> params);

    /**
     * 查询用户最近借款信息,状态大于20
     *
     * @return
     */
    BorrowOrder selectBorrowOrderUseId(Integer userId);

    /**
     * 检查当前用户是否存在未还款完成的订单
     *
     * @param userId
     * @return 1：是；0：否
     */
    Integer checkBorrow(Integer userId);

    /**
     * 更新risk
     *
     * @param riskCreditUser
     */
    void updateRiskCreditUserById(RiskCreditUser riskCreditUser);

//    /**
//     * 根据用户查询申请列表
//     *
//     * @param userId
//     * @return
//     */
//    List<BorrowOrder> findByUserId(Integer userId);

    /**
     * 统计借款记录数
     *
     * @param params
     * @return
     */
    int findParamsCount(HashMap<String, Object> params);


    /**
     * 查询当前用户最近一次审核失败的订单
     *
     * @return
     */
    Map<String, String> findAuditFailureOrderByUserId(String userId);

    /**
     * 查询用户最近借款信息 任何状态 最新为准
     *
     * @return
     */
    BorrowOrder selectBorrowOrderNowUseId(Integer userId);

    /**
     * 更新订单状态
     * @param id id
     */
    void updateBorrowStatus(String id,String status);

    /**
     * 获取待AI验证订单
     * @return list
     */
    List<BorrowOrder> getWaitAiList();

    int getRepaidCount(int userId);

    boolean getExtendStatus(int userId);

    Map<String, Object> extend(Integer orderId);
}
