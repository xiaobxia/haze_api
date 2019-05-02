package com.vxianjin.gringotts.pay.model;

/**
 * @Author: chenkai
 * @Date: 2018/7/19 16:06
 * @Description: 充值记录查询model
 */
public class YPRepayRecordReq {

    //商户编号
    private String merchantNo;
    //充值请求号
    private String requestNo;
    //易宝流水号
    private String ybOrderId;
    //签名
    private String sign;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getYbOrderId() {
        return ybOrderId;
    }

    public void setYbOrderId(String ybOrderId) {
        this.ybOrderId = ybOrderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
