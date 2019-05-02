package com.vxianjin.gringotts.pay.service.impl;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.service.PayWithdrawService;
import com.vxianjin.gringotts.pay.service.YeepayWithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 20:34
 * @Description:
 */
@Service
public class PayWithdrawServiceImpl implements PayWithdrawService {

    private final static Logger logger = LoggerFactory.getLogger(PayWithdrawServiceImpl.class);


    @Autowired
    private YeepayWithdrawService yeepayWithdrawService;


    /**
     * 用户提现（代付）回调接口
     * 和易宝代付代码一样
     */
    @Override
    public String payWithdrawCallback(String reqString) {
        return yeepayWithdrawService.payWithdrawCallback(reqString);
    }

    /**
     * 用户提现（代付）请求接口
     * 和易宝代付代码一样
     *
     * @param userId   借款用户id
     * @param borrowId 借款订单id
     * @param uuid     此次交易的随机编号
     * @param sign     加密签名，用于数据校验，以防数据被篡改
     */
    @Override
    public ResponseContent payWithdraw(
            String userId, String borrowId,
            String uuid, String sign) throws BizException {
        return yeepayWithdrawService.payWithdraw(userId, borrowId, uuid, sign);
    }

}
