package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YeepayRepayReq;
import com.vxianjin.gringotts.pay.model.YeepayRepaySmsReq;
import com.vxianjin.gringotts.pay.model.YeepaySmsReSendResp;
import com.vxianjin.gringotts.web.pojo.RenewalRecord;
import com.vxianjin.gringotts.web.pojo.Repayment;
import com.vxianjin.gringotts.web.pojo.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 易宝支付代扣相关处理
 * @author zed
 */
public interface FuiouRepayService {


    /**
     * 代扣回调（还款）
     * @return res
     * @param callbackResult
     */

    ResultModel payWithholdCallback(Map<String, String> callbackResult);

    /**
     * 续期回调（续期）
     *
     * @param req req
     * @return res
     */
    @Transactional(rollbackFor = Exception.class)
    ResultModel payRenewalWithholdCallback(Map<String, String> callbackResult);

    /**
     * 主动支付（还款）
     *
     * @param id id
     * @param payPwd pwd
     */
    @Transactional(rollbackFor = Exception.class)
    ResponseContent repaymentWithholdConfirm(Integer id, String payPwd, String bankId) throws Exception;

    /**
     * 续期代扣（一般充值）
     * @param id id
     * @param payPwd pwd
     * @param money money
     * @param bankIdStr bank
     * @param sgd sgd
     * @return res
     * @throws Exception ex
     */
    ResponseContent renewalWithhold(Integer id, String payPwd, Long money, String bankIdStr, String sgd) throws Exception;


    /**
     * 定时代扣（还款）
     * @param id id
     * @return res
     * @throws Exception ex
     */
    ResponseContent autoWithhold(Integer id) throws Exception;

    /**
     * 查询代扣支付结果（还款）
     * @param id id
     * @param orderNo no
     * @return res
     */
    ResponseContent queryWithhold(Integer id, String orderNo);

    /**
     * 查询代扣支付结果（还款）
     * @param id id
     * @param orderNo no
     * @return res
     */
    ResponseContent queryRenewalWithhold(Integer id, String orderNo);

    /**
     * 催收代扣（还款）
     * @param userId userId
     * @param repaymentId repayId
     * @param money money
     * @param withholdId id
     * @param sign sign
     * @return res
     * @throws Exception e
     */
    ResponseContent collectionWithhold(String userId, String repaymentId, Long money,
                                       String withholdId, String sign) throws Exception;


    /**
     * 易宝代扣请求处理
     *
     * @param repayment    还款信息
     * @param user         用户
     * @param withholdType 代扣类型(定时，主动，催收)
     * @param money        金额
     * @param debitType type
     * @param remark remark
     * @return res
     */
    ResponseContent fuiouWithhold(Repayment repayment, User user, int withholdType, Long money, String debitType, String remark) throws Exception;

    /**
     * 易宝续期处理
     *
     * @param renewalRecord 续期订单
     * @param user user
     * @param money money
     * @param bankId bank
     * @return res
     * @throws Exception ex
     */
    ResponseContent fuiouRenewalWithhold(RenewalRecord renewalRecord, User user, Long money, Integer bankId) throws Exception;

    /**
     * 易宝主动请求支付处理
     *
     * @param requestNo 请求订单号
     * @param repayment 还款信息
     * @param user      用户
     * @param money     还款金额
     * @param remark    备注
     * @param bankId    银行卡编号
     * @return res
     * @throws Exception ex
     */
    ResponseContent newRecharge(String requestNo, Repayment repayment, User user, Long money, String remark, Integer bankId) throws Exception;
}
