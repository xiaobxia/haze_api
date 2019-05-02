package com.vxianjin.gringotts.pay.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.model.*;
import com.vxianjin.gringotts.pay.service.YeepayRepayService;
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

/**
 * 易宝支付代扣相关
 * @author jintian on 2018/7/17.
 */
@Controller
@RequestMapping(value = "/yeepay")
public class YeepayRepayController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(YeepayRepayController.class);

    @Resource
    private YeepayRepayService yeepayRepayService;

    /**
     * 代扣回调（还款）
     */
    @ResponseBody
    @RequestMapping(value = "withholdCallback/{userId}")
    public String payWithholdCallback(HttpServletRequest request, @PathVariable String userId) {

        logger.debug("YeepayRepayController.payWithholdCallback params: 【req:" + JSON.toJSONString(request.getParameter("response")) + "  userId:" + userId + "】");
        try {
            String req = request.getParameter("response");
            return yeepayRepayService.payWithholdCallback(req).getCode();
        } catch (Exception e) {
            logger.error("YeepayRepayController.payWithholdCallback(代扣回调) has error,params : 【req:" + JSON.toJSONString(request.getParameter("response")) + "  userId:" + userId + "】 ,error: ", e);
            return "FAIL";
        }
    }

    /**
     * 续期回调（续期）
     */
    @ResponseBody
    @RequestMapping(value = "renewalWithholdCallback/{userId}")
    public String payRenewalWithholdCallback(HttpServletRequest request, @PathVariable String userId) {
        logger.debug("YeepayRepayController.payRenewalWithholdCallback params: 【req：" + JSON.toJSONString(request.getParameter("response")) + " userId:" + userId + "】");
        try {
            String req = request.getParameter("response");
            return yeepayRepayService.payRenewalWithholdCallback(req).getCode();
        } catch (Exception e) {
            logger.error("YeepayRepayController.payRenewalWithholdCallback(续期回调) has error ,params : 【req：" + JSON.toJSONString(request.getParameter("response")) + " userId:" + userId + "】,error: ", e);
            return "FAIL";
        }
    }


    /**
     * 主动支付请求（还款）
     */
    @ResponseBody
    @RequestMapping(value = "repayWithholdRequest")
    public ResponseContent repaymentWithholdRequest(YeepayRepayReq req) {
        logger.debug("YeepayRepayController.repaymentWithholdRequest params: 【req:" + JSON.toJSONString(req) + "】");
        try {
            return yeepayRepayService.repaymentWithholdRequest(req);
        } catch (BizException e) {
            logger.error("YeepayRepayController.payRenewalWithholdCallback(主动支付请求) has BizException,params: 【req:" + JSON.toJSONString(req) + "】" + " errorCode" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("YeepayRepayController.payRenewalWithholdCallback(主动支付请求) has BizException,params: 【req:" + JSON.toJSONString(req) + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 主动支付确认（还款）
     */
    @ResponseBody
    @RequestMapping(value = "repayWithholdConfirm")
    public ResponseContent repaymentWithholdConfirm(Integer id, String smsCode, String requestNo, String payPwd) {
        logger.debug("YeepayRepayController.repaymentWithholdConfirm params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】");
        ResponseContent result = null;
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        if (StringUtils.isBlank(requestNo)) {
            return new ResponseContent("-101", "请求参数非法");
        }
        if (StringUtils.isBlank(smsCode)) {
            return new ResponseContent("-101", "请输入短信验证码");
        }
        if (StringUtils.isBlank(payPwd)) {
            return new ResponseContent("-101", "请输入交易密码");
        }
        try {
            result = yeepayRepayService.repaymentWithholdConfirm(id, smsCode, requestNo, payPwd);
        } catch (BizException e) {
            logger.error("YeepayRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】,errorCode:" + e.getErrorCode() + " eroroMsg:" + e.getErrorMsg());
            result = new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("YeepayRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
        return result;
    }

    /**
     * 主动支付短信重发（还款）
     */
    @ResponseBody
    @RequestMapping(value = "repayWithholdSmscode")
    public YeepaySmsReSendResp repaymentWithholdResendSmscode(YeepayRepaySmsReq req) {
        logger.debug("YeepayRepayController.repaymentWithholdResendSmscode params: 【req:" + JSON.toJSONString(req) + "】");
        return yeepayRepayService.repaymentWithholdResendSmscode(req);
    }


    /**
     * 主动代扣（还款）
     *
     * @param id     借款编号
     * @param payPwd 支付密码
     * @return res
     */
    @ResponseBody
    @RequestMapping(value = "repay-withhold")
    public ResponseContent repaymentWithhold(Integer id, String payPwd) {
        logger.debug("YeepayRepayController.repaymentWithhold  params: 【borrowId：" + id + " payPwd：" + payPwd + "】");
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        if (StringUtils.isBlank(payPwd)) {
            return new ResponseContent("-101", "请输入交易密码");
        }
        try {
            return yeepayRepayService.repaymentWithhold(id, payPwd);
        } catch (BizException e) {
            logger.error("YeepayRepayController.repaymentWithhold has BizException ,params: 【borrowId：" + id + " payPwd：" + payPwd + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("YeepayRepayController.repaymentWithhold has BizException ,params: 【borrowId：" + id + " payPwd：" + payPwd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 续期代扣（一般充值）
     */
    @ResponseBody
    @RequestMapping(value = "renewal-withhold")
    public ResponseContent renewalWithhold(Integer id, String payPwd, Long money, String bankId, String sgd) {
        logger.debug("YeepayRepayController.renewalWithhold params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】");
        if (id == null || money <= 0) {
            return new ResponseContent("-101", "请求参数非法");
        }
        if (StringUtils.isBlank(payPwd)) {
            return new ResponseContent("-101", "请输入交易密码");
        }
        try {
            return yeepayRepayService.renewalWithhold(id, payPwd, money, bankId, sgd);
        } catch (BizException e) {
            logger.error("YeepayRepayController.renewalWithhold(续期代扣) has BizException, params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("YeepayRepayController.renewalWithhold(续期代扣) has BizException, params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }


    /**
     * 定时代扣（还款）
     */
    @ResponseBody
    @RequestMapping(value = "auto-withhold")
    public ResponseContent autoWithhold(Integer id) {
        logger.debug("YeepayRepayController.autoWithhold params:【id:" + id + "】");
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            return yeepayRepayService.autoWithhold(id);
        } catch (BizException e) {
            logger.error("YeepayRepayController.autoWithhold(定时代扣) has BizException, params:【id:" + id + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("YeepayRepayController.autoWithhold(定时代扣) has exception, params:【id:" + id + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 查询代扣支付结果（还款）
     */
    @ResponseBody
    @RequestMapping(value = "query-withhold")
    public ResponseContent queryWithhold(Integer id, String orderNo) {
        logger.debug("YeepayRepayController.queryWithhold params: 【id:" + id + " orderNo:" + orderNo + "】");
        return yeepayRepayService.queryWithhold(id, orderNo);
    }

    /**
     * 查询代扣支付结果（还款）
     */
    @ResponseBody
    @RequestMapping(value = "query-renewalWithhold")
    public ResponseContent queryRenewalWithhold(Integer id, String orderNo) {
        logger.debug("YeepayRepayController.queryRenewalWithhold params:【id:" + id + " orderNo:" + orderNo + "】");
        return yeepayRepayService.queryRenewalWithhold(id, orderNo);
    }

    /**
     * 催收代扣（还款）
     */
    @ResponseBody
    @RequestMapping(value = "collection-withhold/{userId}/{repaymentId}/{money}/{withholdId}/{sign}")
    public ResponseContent collectionWithhold(@PathVariable String userId, @PathVariable String repaymentId, @PathVariable Long money,
                                              @PathVariable String withholdId, @PathVariable String sign) {
        logger.debug("YeepayRepayController.collectionWithhold params:【userId:" + userId + " repaymentId:" + repaymentId + " money:" + money + " withholdId:" + withholdId + " sign:" + sign + "】");
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(repaymentId) || StringUtils.isBlank(withholdId) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try {
            return yeepayRepayService.collectionWithhold(userId, repaymentId, money, withholdId, sign);
        } catch (Exception e) {
            logger.error("YeepayRepayController.collectionWithhold params:【userId:" + userId + " repaymentId:" + repaymentId + " money:" + money + " withholdId:" + withholdId + " sign:" + sign + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

}
