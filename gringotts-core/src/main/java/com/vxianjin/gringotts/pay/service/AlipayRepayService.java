package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.common.ResponseContent;

import java.util.Map;

/**
 * 支付宝支付
 * Created by jintian on 2018/7/17.
 */
public interface AlipayRepayService {

    /**
     * 支付宝回调处理
     *
     * @param msg 回调请求的参数内容
     */
    public String payAlipayCallback(String msg);

    /**
     * 处理返回信息并验签
     *
     * @param msg
     * @param map
     * @return
     * @throws Exception
     * @author tgy
     */
    public boolean handleMsgAndVerify(String msg, Map<String, String> map) throws Exception;

    /**
     * 支付宝主动续期 处理
     *
     * @param id    借款订单号
     * @param money 续期金额
     */
    public ResponseContent payAlipayRenewalRequest(Integer id, Long money) throws Exception;

    /**
     * 主动请求支付宝还款
     *
     * @param id 订单号
     */
    public ResponseContent payAlipayWithholdRequest(Integer id) throws Exception;

    /**
     * 支付宝app支付查询订单支付情况
     *
     * @param orderId 订单号
     */
    public ResponseContent queryalipayRequest(String orderId) throws Exception;
}
