package com.vxianjin.gringotts.pay.controller.fuiou;

import com.alibaba.fastjson.JSON;
import com.fuiou.util.MD5;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.model.YeepayRepayReq;
import com.vxianjin.gringotts.pay.model.YeepayRepaySmsReq;
import com.vxianjin.gringotts.pay.model.YeepaySmsReSendResp;
import com.vxianjin.gringotts.pay.service.FuiouRepayService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 富友支付代扣相关
 * @author fully on 2019/5/7.
 */
@Controller
@RequestMapping(value = "/fuiou")
public class FuiouRepayController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FuiouRepayController.class);

    @Resource
    private FuiouRepayService fuiouRepayService;

    /**
     * 富友回调校验
     * @param req
     * @return
     */
    private boolean verifySign(HttpServletRequest req) {
        String version = req.getParameter("VERSION");
        String type = req.getParameter("TYPE");
        String responseCode = req.getParameter("RESPONSECODE");
        String responseMsg = req.getParameter("RESPONSEMSG");
        String mchntCd = req.getParameter("MCHNTCD");
        String mchntOrderId = req.getParameter("MCHNTORDERID");
        String orderId = req.getParameter("ORDERID");
        String bankCard = req.getParameter("BANKCARD");
        String amt = req.getParameter("AMT");
        String sign = req.getParameter("SIGN");

        // 校验签名
        String signPain = new StringBuffer().append(type).append("|").append(version).append("|").append(responseCode)
                .append("|").append(mchntCd).append("|").append(mchntOrderId).append("|").append(orderId).append("|")
                .append(amt).append("|").append(bankCard).append("|").append(FuiouConstants.API_MCHNT_KEY).toString();
        return MD5.MD5Encode(signPain).equals(sign);
    }

    /**
     * 富友订单回调（还款）--- Fuiou
     */
    @RequestMapping(value = "withholdCallback/{userId}")
    public void payWithholdCallback(HttpServletRequest req, @PathVariable String userId) throws Exception {

        logger.info("FuiouRepayController.payWithholdCallback params: 【req:" + JSON.toJSONString(req.getParameterMap()) + "  userId:" + userId + "】");

        if (verifySign(req)) {//验签成功
            Map<String, String> parameterMap = new HashMap();
            parameterMap.put("VERSION", req.getParameter("VERSION"));
            parameterMap.put("TYPE", req.getParameter("TYPE"));
            parameterMap.put("RESPONSECODE", req.getParameter("RESPONSECODE"));
            parameterMap.put("RESPONSEMSG", req.getParameter("RESPONSEMSG"));
            parameterMap.put("MCHNTCD", req.getParameter("MCHNTCD"));
            parameterMap.put("MCHNTORDERID", req.getParameter("MCHNTORDERID"));
            parameterMap.put("ORDERID", req.getParameter("ORDERID"));
            parameterMap.put("BANKCARD", req.getParameter("BANKCARD"));
            parameterMap.put("AMT", req.getParameter("AMT"));
            parameterMap.put("SIGN", req.getParameter("SIGN"));

            fuiouRepayService.payWithholdCallback(parameterMap);
        }
    }

    /**
     * 续期回调（续期）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "renewalWithholdCallback/{userId}")
    public String payRenewalWithholdCallback(HttpServletRequest request, @PathVariable String userId) {
        logger.debug("FuiouRepayController.payRenewalWithholdCallback params: 【req：" + JSON.toJSONString(request.getParameter("response")) + " userId:" + userId + "】");
        try {
            if (verifySign(request)) {
                Map<String, String> parameterMap = new HashMap();
                parameterMap.put("VERSION", request.getParameter("VERSION"));
                parameterMap.put("TYPE", request.getParameter("TYPE"));
                parameterMap.put("RESPONSECODE", request.getParameter("RESPONSECODE"));
                parameterMap.put("RESPONSEMSG", request.getParameter("RESPONSEMSG"));
                parameterMap.put("MCHNTCD", request.getParameter("MCHNTCD"));
                parameterMap.put("MCHNTORDERID", request.getParameter("MCHNTORDERID"));
                parameterMap.put("ORDERID", request.getParameter("ORDERID"));
                parameterMap.put("BANKCARD", request.getParameter("BANKCARD"));
                parameterMap.put("AMT", request.getParameter("AMT"));
                parameterMap.put("SIGN", request.getParameter("SIGN"));
                return fuiouRepayService.payRenewalWithholdCallback(parameterMap).getCode();
            }
            return "FAIL";
        } catch (Exception e) {
            logger.error("FuiouRepayController.payRenewalWithholdCallback(续期回调) has error ,params : 【req：" + JSON.toJSONString(request.getParameter("response")) + " userId:" + userId + "】,error: ", e);
            return "FAIL";
        }
    }

    /**
     * 主动支付（还款）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "repayWithholdConfirm")
    public ResponseContent repaymentWithholdConfirm(Integer id, String smsCode, String requestNo, String payPwd, String bankId) {

        logger.debug("FuiouRepayController.repaymentWithholdConfirm params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + " bankId:" + bankId + "】");
        ResponseContent result = null;
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        if (StringUtils.isBlank(payPwd)) {
            return new ResponseContent("-101", "请输入交易密码");
        }
        try {
            result = fuiouRepayService.repaymentWithholdConfirm(id, payPwd, bankId);
        } catch (BizException e) {
            logger.error("FuiouRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】,errorCode:" + e.getErrorCode() + " eroroMsg:" + e.getErrorMsg());
            result = new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("FuiouRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
        return result;
    }

    /**
     * 续期代扣（首笔验）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "renewal-withhold")
    public ResponseContent renewalWithhold(Integer id, String payPwd, Long money, String bankId, String sgd) {
        logger.debug("FuiouRepayController.renewalWithhold params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】");
        if (id == null || money <= 0) {
            return new ResponseContent("-101", "请求参数非法");
        }
        if (StringUtils.isBlank(payPwd)) {
            return new ResponseContent("-101", "请输入交易密码");
        }
        try {
            return fuiouRepayService.renewalWithhold(id, payPwd, money, bankId, sgd);
        } catch (BizException e) {
            logger.error("FuiouRepayController.renewalWithhold(续期代扣) has BizException, params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("FuiouRepayController.renewalWithhold(续期代扣) has BizException, params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 查询代扣支付结果（还款）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "query-withhold")
    public ResponseContent queryWithhold(Integer id, String orderNo) {
        logger.debug("FuiouRepayController.queryWithhold params: 【id:" + id + " orderNo:" + orderNo + "】");
        return fuiouRepayService.queryWithhold(id, orderNo);
    }

    /**
     * 查询代扣支付结果（还款）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "query-renewalWithhold")
    public ResponseContent queryRenewalWithhold(Integer id, String orderNo) {
        logger.debug("FuiouRepayController.queryRenewalWithhold params:【id:" + id + " orderNo:" + orderNo + "】");
        return fuiouRepayService.queryRenewalWithhold(id, orderNo);
    }

    /**
     * 定时代扣（还款）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "auto-withhold")
    public ResponseContent autoWithhold(Integer id) {
        logger.debug("FuiouRepayController.autoWithhold params:【id:" + id + "】");
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            return fuiouRepayService.autoWithhold(id);
        } catch (BizException e) {
            logger.error("FuiouRepayController.autoWithhold(定时代扣) has BizException, params:【id:" + id + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("FuiouRepayController.autoWithhold(定时代扣) has exception, params:【id:" + id + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 催收代扣（还款）--- Fuiou
     */
    @ResponseBody
    @RequestMapping(value = "collection-withhold/{userId}/{repaymentId}/{money}/{withholdId}/{sign}")
    public ResponseContent collectionWithhold(@PathVariable String userId, @PathVariable String repaymentId, @PathVariable Long money,
                                              @PathVariable String withholdId, @PathVariable String sign) {
        logger.debug("FuiouRepayController.collectionWithhold params:【userId:" + userId + " repaymentId:" + repaymentId + " money:" + money + " withholdId:" + withholdId + " sign:" + sign + "】");
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(repaymentId) || StringUtils.isBlank(withholdId) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return fuiouRepayService.collectionWithhold(userId, repaymentId, money, withholdId, sign);
        } catch (Exception e) {
            logger.error("FuiouRepayController.collectionWithhold params:【userId:" + userId + " repaymentId:" + repaymentId + " money:" + money + " withholdId:" + withholdId + " sign:" + sign + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

}
