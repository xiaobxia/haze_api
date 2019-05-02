package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.common.ResponseContent;

/**
 * Created by jintian on 2018/7/18.
 */
public interface PayRepayService {

    /**
     * 定时代扣（还款）
     * 走的易宝，直接用易宝代码
     */
    public ResponseContent autoWithhold(Integer id) throws Exception;

    /**
     * 催收代扣（还款）
     * 走的易宝，直接用易宝代码
     */
    public ResponseContent collectionWithhold(String userId, String repaymentId, Long money,
                                              String withholdId, String sign) throws Exception;
}
