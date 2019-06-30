package com.vxianjin.gringotts.pay.service.chanpay;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.model.chanpay.CjtDsfT10000Notify;

import java.util.Map;

/**
 * 易宝支付代付处理
 * @author  jintian on 2018/7/17.
 */
public interface ChanpayWithdrawService {


    /**
     * 用户提现（代付）回调接口
     * @param  cjtDsfT10000Notify req
     * @return  str
     */
    void payWithdrawCallback(CjtDsfT10000Notify cjtDsfT10000Notify);

    /**
     * 用户提现（代付）请求接口
     * @param userId   借款用户id
     * @param borrowId 借款订单id
     * @param uuid     此次交易的随机编号
     * @param sign     加密签名，用于数据校验，以防数据被篡改
     * @throws BizException ex
     * @return  ResponseContent res
     */
    ResponseContent payWithdraw(String userId, String borrowId, String uuid, String sign) throws Exception;

    ResponseContent testPayWithdraw(String userId, String borrowId) throws Exception;

}
