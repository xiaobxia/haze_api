package com.vxianjin.gringotts.pay.common.constants;


import com.vxianjin.gringotts.pay.common.util.Configuration;

/**
 * @Author: chenkai
 * @Date: 2018/7/17 14:27
 * @Description: 公共的常用变量,三方支付请求配置
 */
public class PayConstants {
    public final static String REPAY_SUCC_SMS = "尊敬的{0}：您的{1}元借款已经还款成功，您的该笔交易将计入您的信用记录，好的记录将有助于提升您的可用额度。";
    /**
     * 商户编号
     */
    public final static String MERCHANT_NO = Configuration.getInstance().getValue("merchantAccount");
    /**
     * 商户私钥
     */
    public final static String MERCHANT_PRIVATE_KEY = Configuration.getInstance().getValue("merchantPrivateKey");
    /**
     * 商户私钥
     */
    public final static String HMAC_KEY = Configuration.getInstance().getValue("hmacKey");
    public final static String YEEPAY_PUBLIC_KEY = Configuration.getInstance().getValue("yeepayPublicKey");
    public final static String GROUP_ID = Configuration.getInstance().getValue("groupId");
    /*===================================yeepay.properties========================================**/
    /**
     * 充值记录查询
     */
    public final static String REPAY_RECORD_QUERY_REQURL = Configuration.getInstance().getValue("payRecordQueryURL");
    /**
     * 批量查询明细的相关变量
     */
    public final static String BATCH_DETAIL_QUERY_REQURL = Configuration.getInstance().getValue("batchDetailQueryURL");
    /**
     * 有短验绑卡请求接口
     */
    public final static String BIND_CARDREQUEST_URL =Configuration.getInstance().getValue("bindCardRequestURL");
    /**
     * 有短验绑卡请求确认接口
     */
    public final static String BIND_CARDCONFIRM_URL = Configuration.getInstance().getValue("bindCardConfirmURL");
    /**
     * 有短验绑卡请求重发短验接口
     */
    public final static String BIND_CARDRESENDSMS_URL =  Configuration.getInstance().getValue("bindCardResendsmsURL");
    /**
     * 无短验充值接口
     */
    public final static String UNSENDBIND_PAY_REQUEST_URL =  Configuration.getInstance().getValue("unSendBindPayRequestURL");
    /**
     * 无短验绑卡接口
     */
    public final static String UNSENDBIND_CARD_REQUEST_URL = Configuration.getInstance().getValue("unSendBindCardRequestURL");

    /**
     * 代发交易请求地址
     */
    public final static String REQUEST_URL = Configuration.getInstance().getValue("requestURL");
    /**
     * 主动充值请求接口
     */
    public final static String AUTOPAY_REQUEST_URL = Configuration.getInstance().getValue("autoPayRequestURL");
    /**
     * 主动充值短信重复接口
     */
    public final static String AUTOPAY_RESENDSMS_URL = Configuration.getInstance().getValue("autoPayResendsmsURL");

    /**
     * 主动充值确认接口
     */
    public final static String AUTOPAY_CONFIRM_URL = Configuration.getInstance().getValue("autoPayConfirmURL");


    /**===================================Yeepay.hmacs=========================================**/
    public final static String SIANG_CERT = Configuration.getInstance().getValue("signCert");
}
