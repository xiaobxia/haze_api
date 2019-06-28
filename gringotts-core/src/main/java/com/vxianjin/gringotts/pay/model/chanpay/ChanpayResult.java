package com.vxianjin.gringotts.pay.model.chanpay;

import lombok.Data;

@Data
public class ChanpayResult {

    private String TrxId;

    private String OrderTrxid;

    private String Status;

    private String RetCode;

    private String RetMsg;

    private String AppRetcode;

    private String AppRetMsg;

    private String Extension;
}
