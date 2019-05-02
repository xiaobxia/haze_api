package com.vxianjin.gringotts.pay.component;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public interface IAliPayService {


    /**
     * 还款支付请求
     *
     * @return
     * @throws Exception
     */
    String payWithhold(HashMap<String, String> params) throws Exception;

    /**
     * 续期支付请求
     *
     * @return
     * @throws Exception
     */
    String payRenewal(HashMap<String, String> params) throws Exception;


    /**
     * 支付回调处理
     *
     * @param respMap
     * @param msg
     * @return
     * @throws Exception
     */
    String payNotify(Map<String, String> respMap, String msg) throws Exception;

    /**
     * 订单查询
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    JSONObject queryResult(String orderId) throws Exception;


    /**
     * 订单查询结果处理
     *
     * @param paraMap
     * @return
     * @throws Exception
     */
    JSONObject queryHandleMth(Map<String, String> paraMap) throws Exception;

}
