package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从core中提取出来
 */
public interface RepaymentService {

    /**
     * 根据条件查找借款
     */
    public List<Repayment> findAll(HashMap<String, Object> params);

    /**
     * 根据条件查找一条
     *
     * @param params
     * @return
     */
    public Repayment findOneRepayment(Map<String, Object> params);

    public List<RepaymentDetail> findDetailsByRepId(Integer id);

    public List<Map<String, Object>> findMyLoan(Map<String, Object> params);

    public Repayment selectByPrimaryKey(Integer id);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    public boolean deleteByPrimaryKey(Integer id);

    /**
     * 插入 对象
     *
     * @param repayment
     */
    public boolean insert(Repayment repayment);

    /**
     * 插入 对象
     *
     * @param repayment
     */
    public boolean insertSelective(Repayment repayment);

    /**
     * 更新 象
     *
     * @param repayment
     */
    public boolean updateByPrimaryKey(Repayment repayment);

    /**
     * 更新 象
     *
     * @param repayment
     */
    public boolean updateByPrimaryKeySelective(Repayment repayment);

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    public PageConfig<Repayment> findPage(HashMap<String, Object> params);

    /**
     * 还款
     *
     * @param repayment
     * @param detail
     * @param action 还款动作
     * @return
     */
    public void repay(Repayment repayment, RepaymentDetail detail,String action);


    /**
     * 同步还款
     */
    public void synRepay(final Repayment re,final RepaymentDetail detail,final User user);
    /**
     * 续期
     *
     * @param repayment
     * @param rr
     * @return
     */
    public void renewal(Repayment repayment, RenewalRecord rr);

    /**
     * 查询还款信息
     *
     * @param borrowId
     * @return
     */
    public List<Repayment> findAllByBorrowId(Integer borrowId);

    public void updateRenewalByPrimaryKey(Repayment record);

    /**
     * 插入对象
     *
     * @param borrowOrder
     * @return
     */
    public boolean insertByBorrorOrder(BorrowOrder borrowOrder);

    public List<Repayment> findParams(Map<String, Object> params);

    public int findParamsCount(HashMap<String, Object> params);



    public Map<String, Object> findBorrowLoanTerm(Integer borrowId);

    /**
     * 待还销账分页查询
     *
     * @param params
     * @return
     */
    PageConfig<Repayment> findWriteOffPage(HashMap<String, Object> params);



    public void synReapymentDetailStatus(RepaymentDetail repaymentDetail);
}
