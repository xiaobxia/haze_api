package com.vxianjin.gringotts.pay.model.chanpay;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TradeNotify extends BaseNotify{

    private String outer_trade_no;//商户网站提现唯一订单号

    private String inner_trade_no;//支付平台提现交易号

    private String trade_status;//交易状态

    private String trade_amount;//交易金额

    private String gmt_create;//交易创建时间

    private String gmt_payment;//交易支付时间

    private String gmt_close;//交易关闭时间

    private String extension;//扩展参数

    private String status;//支付状态

    private String pay_msg;//支付返回消息

}
