package com.vxianjin.gringotts.pay.controller.fuiou;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.service.YeepayWithdrawService;
import com.vxianjin.gringotts.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 易宝支付代付相关
 * @author jintian on 2018/7/17.
 */
@Controller
@RequestMapping(value = "/fuiou")
public class FuiouWithdrawController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FuiouWithdrawController.class);

    @Resource
    private YeepayWithdrawService yeepayWithdrawService;

    /**
     * 用户提现（代付）回调接口
     */
    @ResponseBody
    @RequestMapping(value = "withdrawCallback")
    public String payWithdrawCallback(HttpServletRequest request) {
        logger.debug("YeepayWithdrawController.payWithdrawCallback params:【reqString:" + request.getParameter("response") + "】");
        String reqString = request.getParameter("response");

        if (StringUtils.isBlank(reqString)) {
            return "数据解析失败";
        }
        return yeepayWithdrawService.payWithdrawCallback(reqString);
    }


    /**
     * 用户提现（代付）回调接口
     */
    @ResponseBody
    @RequestMapping(value = "withdrawCallback_2")
    public String payWithdrawCallbackForTest(@RequestBody String reqString) {
        logger.debug("YeepayWithdrawController.payWithdrawCallback params:【reqString:" + reqString + "】");
        if (StringUtils.isBlank(reqString)) {
            return "数据解析失败";
        }
        return yeepayWithdrawService.payWithdrawCallbackForOnline(reqString);
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
        logger.info("YeepayWithdrawController.payWithdraw userId=" + userId + " borrowId=" + borrowId + " uuid=" + uuid + " sign=" + sign);
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowId) || StringUtils.isBlank(uuid) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return yeepayWithdrawService.payWithdraw(userId, borrowId, uuid, sign);
        } catch (BizException e) {
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        }
    }
}
