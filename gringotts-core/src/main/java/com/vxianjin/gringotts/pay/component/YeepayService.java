package com.vxianjin.gringotts.pay.component;

import com.vxianjin.gringotts.pay.model.*;

import java.util.Map;

/**
 * @author zed
 * 易宝支付接口类
 */
public interface YeepayService {
    /**
     * 绑卡请求
     *
     * @param paramMap map
     * @return map
     */
    Map<String, Object> getBindCardRequest(Map<String, String> paramMap);
    /**
     * 绑卡确认
     *
     * @param bindCardConfirmReq req
     * @return result
     */
    ResultModel<Map<String, Object>> getBindCardConfirm(YPBindCardConfirmReq bindCardConfirmReq);
    /**
     * 发送绑卡短验
     *
     * @param paramMap map
     * @return map
     */
    Map<String, Object> getBindCardSmsCode(Map<String, String> paramMap);
    /**
     * 更新用户银行卡信息
     *
     * @param paramMap map
     * @throws  Exception ex
     * @return map
     */
    ResultModel updateUserBankInfo(Map<String, String> paramMap) throws Exception;

    /**
     * 提现请求（代付）
     *
     * @param paramMap map
     * @return map
     */
    Map<String, Object> getWithdrawRequest(Map<String, String> paramMap);


    /**
     * 无短验绑卡请求
     *
     * @param paramMap map
     * @return map
     */
    Map<String, Object> getUnSendBindCardRequest(Map<String, String> paramMap);

    /**
     * 获取商户私钥
     * @return str
     */
    String getHmacKey();



    /**
     * 有短验支付短信请求
     * @param paramMap map
     * @return map
     */
    Map<String, Object> getRechargeSmsCode(Map<String, String> paramMap);

    /**
     * 有短验支付确认请求
     *
     * @param paramMap map
     * @return result
     */
    ResultModel<Map<String, Object>> getRechargeConfirm(Map<String, String> paramMap);


    /**
     * 易宝支付结果查询接口(代发、代付)
     * @param ypBatchPayResultReq req
     * @return result
     */
    PageResultModel<YPBatchPayResultModel> getYBPayResult(YPBatchPayResultReq ypBatchPayResultReq);


    /**
     * 易宝用户还款结果查询接口
     * @param ypRepayRecordReq req
     * @param userId userId
     * @return result
     */
    ResultModel<YPRepayResultModel> getYBRepayResult(YPRepayRecordReq ypRepayRecordReq,String userId);
}
