package com.vxianjin.gringotts.pay.controller.chanpay;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.model.chanpay.CjtDsfT10000Notify;
import com.vxianjin.gringotts.pay.service.chanpay.ChanpayWithdrawService;
import com.vxianjin.gringotts.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 畅捷通支付代付相关
 * @author jintian on 2018/7/17.
 */
@Controller
@RequestMapping(value = "/chanpay")
public class ChanpayWithdrawController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ChanpayWithdrawController.class);

    @Resource
    private ChanpayWithdrawService chanpayWithdrawService;

    /**
     * 提现回调(畅捷通代收付)
     *
     * @param cjtDsfT10100Notify
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "withdrawCallback" ,method = RequestMethod.POST)
    public String chanpayRepaymentNotify(CjtDsfT10000Notify cjtDsfT10100Notify) {
        try {
            cjtDsfT10100Notify.set_input_charset("UTF-8");
            chanpayWithdrawService.payWithdrawCallback(cjtDsfT10100Notify);
        } catch (Exception e) {
            logger.error("提现回调(畅捷通代收付)", e);
        }
        return "success";
    }


    /**
     * 用户提现（代付）请求接口
     *
     * @param userId   借款用户id
     * @param borrowId 借款订单id
     * @param uuid     此次交易的随机编号
     * @param sign     加密签名，用于数据校验，以防数据被篡改
     */
    @ResponseBody
    @RequestMapping(value = "withdraw/{userId}/{borrowId}/{uuid}/{sign}")
    public ResponseContent payWithdraw(@PathVariable String userId, @PathVariable String borrowId, @PathVariable String uuid, @PathVariable String sign) {
        logger.info("ChanPayWithdrawController.payWithdraw userId=" + userId + " borrowId=" + borrowId + " uuid=" + uuid + " sign=" + sign);
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowId) || StringUtils.isBlank(uuid) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return chanpayWithdrawService.payWithdraw(userId, borrowId, uuid, sign);
        } catch (BizException e) {
            logger.info("ChanPayWithdrawController error:" + e.getErrorMsg() + "message:" + e.getMessage());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.info("ChanPayWithdrawController error message:" + e.getMessage());
            return new ResponseContent("-105", e.getMessage());
        }
    }

    /*public static void main(String[] args) {
     *//*CjtDsfT10000Notify cjtDsfT10000Notify = new CjtDsfT10000Notify();
        cjtDsfT10000Notify.setNotify_id("9def501916f648f1ae811c09f4bf3ba8");
        cjtDsfT10000Notify.setNotify_type("withdrawal_status_sync");
        cjtDsfT10000Notify.setNotify_time("20190619180258");
        cjtDsfT10000Notify.set_input_charset("UTF-8");
        cjtDsfT10000Notify.setVersion("1.0");
        cjtDsfT10000Notify.setOuter_trade_no("hEjj34RAZ0");
        cjtDsfT10000Notify.setInner_trade_no("102156093331354582454");
        cjtDsfT10000Notify.setWithdrawal_amount("0.01");
        cjtDsfT10000Notify.setWithdrawal_status("WITHDRAWAL_FAIL");
        cjtDsfT10000Notify.setUid("");
        cjtDsfT10000Notify.setReturn_code("S0001");
        cjtDsfT10000Notify.setFail_reason("持卡人身份信息验证失败");
        cjtDsfT10000Notify.setFail_reason("持卡人身份信息验证失败");
        cjtDsfT10000Notify.setGmt_withdrawal("20190619163736");
        cjtDsfT10000Notify.setSign("YOTimhygmANxJmn1Vf5geU1q7HLVjZcVtLILqzolT6Rl+U7ZwEymykoxrcZsXvMHxNjYFDZ7vKxRUnt1snDph+tXEZec71EgTw54j7OqZlEIEkUV3As/Gt6Ogt96g5uLjcHbxAZWRSU+PHNb4VLKQl7KzKOIQSJzm1fVHpEBmI4=");
        boolean verify = ChanPayUtil.verify(GsonUtil.toJson(cjtDsfT10000Notify), BaseConstant.MERCHANT_PUBLIC_KEY);
        System.out.println(verify);*//*

        String result = "{\"AcceptStatus\":\"S\",\"AcctName\":\"P398344330\",\"AcctNo\":\"P371937930\",\"AppRetMsg\":\"交易处理中\",\"AppRetcode\":\"01019999\",\"CorpName\":\"畅捷支付测试联调（新代收付）\",\"Fee\":\"0.00\",\"FlowNo\":\"136J25IH2B913A20\",\"InputCharset\":\"UTF-8\",\"OriginalErrorMessage\":\"接收成功\",\"OriginalRetCode\":\"000001\",\"OutTradeNo\":\"2019061920160075508569\",\"PartnerId\":\"200001160096\",\"PlatformErrorMessage\":\"交易受理成功\",\"PlatformRetCode\":\"0000\",\"Sign\":\"owtqWcXkLHxVttdZhFoNp3RN10mQHUVT/W/1Sd1kzzou1yq8g4BtZ5sRB1fwC2sXVOoUFyyOwbbFLSAeJCJXLqTbnzQ78PSzKXKd2qRpsXp7G2njdxynM2OxQnAO/nt0FvHL/BCGkCHpjRjJIxE9qgWXA///47x3k6FY6LAQeIk=\",\"SignType\":\"RSA\",\"TimeStamp\":\"20190619201600\",\"TradeDate\":\"20190619\",\"TradeTime\":\"201600\",\"TransAmt\":\"0.01\",\"TransCode\":\"T10000\"}";
        boolean verify = ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY);
        System.out.println(verify);
    }*/
}
