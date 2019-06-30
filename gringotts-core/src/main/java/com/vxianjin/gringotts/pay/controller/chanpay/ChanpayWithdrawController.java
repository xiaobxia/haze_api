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

    @ResponseBody
    @RequestMapping(value = "withdraw/{userId}/{borrowId}")
    public ResponseContent payWithdraw(@PathVariable String userId, @PathVariable String borrowId) {
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowId)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return chanpayWithdrawService.testPayWithdraw(userId, borrowId);
        } catch (BizException e) {
            logger.info("ChanPayWithdrawController error:" + e.getErrorMsg() + "message:" + e.getMessage());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.info("ChanPayWithdrawController error message:" + e.getMessage());
            return new ResponseContent("-105", e.getMessage());
        }
    }

}
