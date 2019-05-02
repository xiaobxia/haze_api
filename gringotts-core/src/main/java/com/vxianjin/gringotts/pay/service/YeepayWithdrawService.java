package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;

/**
 * 易宝支付代付处理
 * @author  jintian on 2018/7/17.
 */
public interface YeepayWithdrawService {


    /**
     * 用户提现（代付）回调接口
     * @param  reqStr req
     * @return  str
     */
    String payWithdrawCallback(String reqStr);

    /**
     * 用户提现（代付）请求接口
     * @param userId   借款用户id
     * @param borrowId 借款订单id
     * @param uuid     此次交易的随机编号
     * @param sign     加密签名，用于数据校验，以防数据被篡改
     * @throws BizException ex
     * @return  ResponseContent res
     */
    ResponseContent payWithdraw(String userId, String borrowId, String uuid, String sign) throws BizException;

    /**
     * 用户提现（代付）回调接口预生产环境
     * @param reqString req
     * @return str
     */
    String payWithdrawCallbackForOnline(String reqString);
}
