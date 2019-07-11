package com.vxianjin.gringotts.pay.controller.chanpay;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.model.chanpay.TradeNotify;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.pay.service.chanpay.ChanpayRepayService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 畅捷通支付代扣相关
 * @author fully on 2019/5/7.
 */
@Controller
@RequestMapping(value = "/chanpay")
public class ChanpayRepayController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ChanpayRepayController.class);

    @Resource
    private ChanpayRepayService chanpayRepayService;

    @Resource
    private RenewalRecordService renewalRecordService;

    /**
     * 畅捷通还款回调--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "withholdCallback/{userId}", method = RequestMethod.POST)
    public String payWithholdCallback(TradeNotify tradeNotify, @PathVariable String userId) throws Exception {
        logger.info("ChanpayRepayController.payWithholdCallback params: 【req:" + JSON.toJSONString(tradeNotify) + "  userId:" + userId + "】");
        try {
            Thread.sleep(5000);//异步回来等一等同步数据入库
            tradeNotify.set_input_charset("UTF-8");
            chanpayRepayService.payWithholdCallback(tradeNotify);
        } catch (Exception e) {
            logger.error("还款回调(畅捷通快捷支付)", e);
        }
        return "success";
    }

    /**
     * 畅捷通续期回调--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "renewalWithholdCallback/{userId}", method = RequestMethod.POST)
    public String payRenewalWithholdCallback(TradeNotify tradeNotify, @PathVariable String userId) throws Exception {
        logger.info("ChanpayRepayController.payRenewalWithholdCallback params: 【req：" + JSON.toJSONString(tradeNotify) + " userId:" + userId + "】");
        try {
            Thread.sleep(5000);//异步回来等一等同步数据入库
            tradeNotify.set_input_charset("UTF-8");
            chanpayRepayService.payRenewalWithholdCallback(tradeNotify);
        } catch (Exception e) {
            logger.error("续期回调(畅捷通快捷支付)", e);
        }
        return "success";
    }

    /**
     * 主动支付（还款）--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "repayWithholdConfirm")
    public ResponseContent repaymentWithholdConfirm(Integer id, String smsCode, String requestNo, String payPwd, String bankId) {

        logger.debug("ChanpayRepayController.repaymentWithholdConfirm params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + " bankId:" + bankId + "】");
        ResponseContent result;
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            result = chanpayRepayService.repaymentWithholdConfirm(id, payPwd, bankId);
        } catch (BizException e) {
            logger.error("ChanpayRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】,errorCode:" + e.getErrorCode() + " eroroMsg:" + e.getErrorMsg());
            result = new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("ChanpayRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + " payPwd:" + payPwd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
        return result;
    }

    /**
     * 续期代扣（首笔验）--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "renewal-withhold")
    public ResponseContent renewalWithhold(Integer id, String payPwd, Long money, String bankId, String sgd) {
        logger.debug("ChanpayRepayController.renewalWithhold params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】");
        if (id == null || money <= 0) {
            return new ResponseContent("-101", "请求参数非法");
        }
        //成立提示
        if (renewalRecordService.borrowOrderRenewalRecordFlag(id)) {
            return new ResponseContent("-101", "续期次数超过限制");
        }

        try {
            return chanpayRepayService.renewalWithhold(id, payPwd, money, bankId, sgd);
        } catch (BizException e) {
            logger.error("ChanpayRepayController.renewalWithhold(续期代扣) has BizException, params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("ChanpayRepayController.renewalWithhold(续期代扣) has BizException, params：【id:" + id + " payPwd：" + payPwd + " money:" + money + " bankId:" + bankId + " sgd:" + sgd + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 查询代扣支付结果（还款）--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "query-withhold")
    public ResponseContent queryWithhold(Integer id, String orderNo) {
        logger.debug("ChanpayRepayController.queryWithhold params: 【id:" + id + " orderNo:" + orderNo + "】");
        return chanpayRepayService.queryWithhold(id, orderNo);
    }

    /**
     * 查询代扣支付结果（还款）--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "query-renewalWithhold")
    public ResponseContent queryRenewalWithhold(Integer id, String orderNo) {
        logger.debug("ChanpayRepayController.queryRenewalWithhold params:【id:" + id + " orderNo:" + orderNo + "】");
        return chanpayRepayService.queryRenewalWithhold(id, orderNo);
    }

    /**
     * 定时代扣（还款）--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "auto-withhold")
    public ResponseContent autoWithhold(Integer id) {
        logger.debug("ChanpayRepayController.autoWithhold params:【id:" + id + "】");
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            return chanpayRepayService.autoWithhold(id);
        } catch (BizException e) {
            logger.error("ChanpayRepayController.autoWithhold(定时代扣) has BizException, params:【id:" + id + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("ChanpayRepayController.autoWithhold(定时代扣) has exception, params:【id:" + id + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 催收代扣（还款）--- Chanpay
     */
    @ResponseBody
    @RequestMapping(value = "collection-withhold/{userId}/{repaymentId}/{money}/{withholdId}/{sign}")
    public ResponseContent collectionWithhold(@PathVariable String userId, @PathVariable String repaymentId, @PathVariable Long money,
                                              @PathVariable String withholdId, @PathVariable String sign) {
        logger.debug("ChanpayRepayController.collectionWithhold params:【userId:" + userId + " repaymentId:" + repaymentId + " money:" + money + " withholdId:" + withholdId + " sign:" + sign + "】");
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(repaymentId) || StringUtils.isBlank(withholdId) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代扣失败,请求参数不符合要求");
        }
        try {
            return chanpayRepayService.collectionWithhold(userId, repaymentId, money, withholdId, sign);
        } catch (Exception e) {
            logger.error("ChanpayRepayController.collectionWithhold params:【userId:" + userId + " repaymentId:" + repaymentId + " money:" + money + " withholdId:" + withholdId + " sign:" + sign + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 主动支付请求（还款）
     */
    @ResponseBody
    @RequestMapping(value = "directRepayWithholdRequest")
    public ResponseContent repaymentWithholdRequest(Integer id, String bankId) {
        logger.debug("ChanpayRepayController.repaymentWithholdRequest params: 【borrowId:{},bankId:{}】", id, bankId);
        try {
            return chanpayRepayService.directRepaymentWithholdRequest(id, bankId);
        } catch (BizException e) {
            logger.error("ChanpayRepayController.payRenewalWithholdCallback(主动支付请求) has BizException,params: 【borrowId:{},bankId:{}】" + " errorCode" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg(), id, bankId);
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("ChanpayRepayController.payRenewalWithholdCallback(主动支付请求) has BizException,params: 【borrowId:{},bankId:{}】,error:{}", id, bankId, e.getMessage());
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 主动支付确认（还款）
     */
    @ResponseBody
    @RequestMapping(value = "directRepayWithholdConfirm")
    public ResponseContent repaymentWithholdConfirm(Integer id, String smsCode, String requestNo) {
        logger.debug("ChanpayRepayController.repaymentWithholdConfirm params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + "】");
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
        try {
            result = chanpayRepayService.directRepaymentWithholdConfirm(id, smsCode, requestNo);
        } catch (BizException e) {
            logger.error("ChanpayRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + "】,errorCode:" + e.getErrorCode() + " eroroMsg:" + e.getErrorMsg());
            result = new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("ChanpayRepayController.repaymentWithholdConfirm(主动支付确认) has BizException, params:【id:" + id + " smsCode:" + smsCode + " requestNo:" + requestNo + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
        return result;
    }

}
