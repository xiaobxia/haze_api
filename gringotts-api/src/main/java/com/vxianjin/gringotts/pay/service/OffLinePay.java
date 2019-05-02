package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.pay.model.OffPayResponse;

/**
 * @author jintian
 * @date 10:08
 */
public interface OffLinePay {

    /**
     * 线下续期
     *
     * @param renewalRecordJsonString 续期信息json字符串
     * @param serverUser              server端操作人
     * @return
     */
    public OffPayResponse offLineRenewal(String renewalRecordJsonString, String serverUser);


    /**
     * 线下还款
     *
     * @param repaymentDetailJsonStr 还款信息json字符串
     * @param serverUser             server端操作人
     * @return
     */
    public OffPayResponse offLineRepay(String repaymentDetailJsonStr, String serverUser);
}
