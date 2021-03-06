package com.vxianjin.gringotts.pay.controller.fuiou;

import com.fuiou.util.MD5;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.service.FuiouWithdrawService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 易宝支付代付相关
 * @author jintian on 2018/7/17.
 */
@Controller
@RequestMapping(value = "/fuiou")
public class FuiouWithdrawController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FuiouWithdrawController.class);

    @Resource
    private FuiouWithdrawService fuiouWithdrawService;

    /**
     * 富友回调校验
     * @param request
     * @return
     */
    private Map<String, String> verifySign(HttpServletRequest request) {
        String orderno = request.getParameter("orderno");
        String merdt = request.getParameter("merdt");
        String accntno = request.getParameter("accntno");
        String amt = request.getParameter("amt");
        String state = request.getParameter("state");
        String result = request.getParameter("result");
        String reason = request.getParameter("reason");
        String mac = request.getParameter("mac");

        Map<String, String> map = new HashMap<>();
        map.put("orderno", orderno);
        map.put("merdt", merdt);
        map.put("fuorderno", request.getParameter("fuorderno"));
        map.put("accntno", accntno);
        map.put("accntno", request.getParameter("accntnm"));
        map.put("bankno", request.getParameter("bankno"));
        map.put("amt", amt);
        map.put("state", request.getParameter("state"));
        map.put("state", request.getParameter("state"));
        map.put("result", request.getParameter("result"));
        map.put("reason", request.getParameter("reason"));
        map.put("mac", mac);

        // 校验签名
        String signPain = new StringBuffer().append(FuiouConstants.API_MCHNT_CD).append("|").append(FuiouConstants.API_MCHNT_KEY).append("|").append(orderno)
                .append("|").append(merdt).append("|").append(accntno).append("|").append(amt).toString();
        map.put("verify", MD5.MD5Encode(signPain).equals(mac) ? "1" : "0");
        return map;
    }

    /**
     * 用户提现（代付）回调接口
     */
    @ResponseBody
    @RequestMapping(value = "withdrawCallback")
    public String payWithdrawCallback(HttpServletRequest request) {
        logger.debug("FuiouWithdrawController.payWithdrawCallback params:【reqString:" + request.getParameter("response") + "】");

        Map<String, String> map = verifySign(request);

        if (map.get("verify").equals("0")) {
            return "0";
        }
        return fuiouWithdrawService.payWithdrawCallback(map);
    }


    /**
     * 用户提现（代付）回调接口
     */
    /*@ResponseBody
    @RequestMapping(value = "withdrawCallback_2")
    public String payWithdrawCallbackForTest(@RequestBody String reqString) {
        logger.debug("FuiouWithdrawController.payWithdrawCallback params:【reqString:" + reqString + "】");
        if (StringUtils.isBlank(reqString)) {
            return "数据解析失败";
        }
        return fuiouWithdrawService.payWithdrawCallbackForOnline(reqString);
    }*/


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
        logger.info("FuiouWithdrawController.payWithdraw userId=" + userId + " borrowId=" + borrowId + " uuid=" + uuid + " sign=" + sign);
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(borrowId) || StringUtils.isBlank(uuid) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return fuiouWithdrawService.payWithdraw(userId, borrowId, uuid, sign);
        } catch (BizException e) {
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        }
    }
}
