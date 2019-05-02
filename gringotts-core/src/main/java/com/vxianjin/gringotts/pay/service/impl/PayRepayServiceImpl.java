package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.service.PayRepayService;
import com.vxianjin.gringotts.pay.service.YeepayRepayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 20:36
 * @Description:
 */
@Service
public class PayRepayServiceImpl implements PayRepayService {
    private final static Logger logger = LoggerFactory.getLogger(PayRepayServiceImpl.class);

    @Autowired
    private YeepayRepayService  yeepayRepayService;

    /**
     * 定时代扣（还款）
     * 走的易宝，直接用易宝代码
     */
    @Override
    public ResponseContent autoWithhold(Integer id) throws Exception {
        return yeepayRepayService.autoWithhold(id);
    }

    /**
     * 催收代扣（还款）
     * 走的易宝，直接用易宝代码
     */
    @Override
    public ResponseContent collectionWithhold(String userId, String repaymentId, Long money,
                                              String withholdId, String sign) throws Exception{
        return yeepayRepayService.collectionWithhold(userId, repaymentId, money, withholdId, sign);
    }
}
