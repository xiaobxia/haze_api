package com.vxianjin.gringotts.pay.model.chanpay;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CjtDsfT10000Notify extends BaseNotify{

    private String outer_trade_no;//商户网站提现唯一订单号

    private String inner_trade_no;//支付平台提现交易号

    private String withdrawal_amount;//提现金额

    private String withdrawal_status;//提现状态

    private String uid;//用户ID

    private String return_code;//返回码

    private String fail_reason;//失败原因

    private String gmt_withdrawal;//提现时间

}
