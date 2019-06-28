package com.vxianjin.gringotts.pay.model.chanpay;

import lombok.Data;
import lombok.ToString;

@Data
public class BaseNotify {

    private String notify_id;//通知ID

    private String notify_type;//通知类型

    private String notify_time;//通知时间

    private String _input_charset;//参数字符集编码

    private String sign;//签名

    private String sign_type;//签名方式

    private String version;//版本号

}
