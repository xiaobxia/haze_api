package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.service.AlipayRepayService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 支付宝还款相关接口
 * @author  wukun on 2018/3/5
 */
@Controller
@RequestMapping(value = "/alipay")
public class AlipayRepayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AlipayRepayController.class);
    /**
     * 阿里支付第四方服务
     */
    @Resource
    private AlipayRepayService service;

    /**
     * 支付宝app支付查询订单支付情况
     *
     * @param orderId id
     */
    @ResponseBody
    @RequestMapping(value = "alipay_query_req")
    public ResponseContent queryalipayRequest(String orderId) throws Exception {
        logger.debug("AlipayRepayController.queryalipayRequest params: 【orderNo:" + orderId + "】");
        return service.queryalipayRequest(orderId);
    }

    /**
     * 支付宝主动续期
     *
     * @param id id
     * @throws Exception ex
     */
    @ResponseBody
    @RequestMapping(value = "alipay_renewal_req")
    public ResponseContent payAlipayRenewalRequest(Integer id, Long money) throws Exception {
        logger.debug("AlipayRepayController.payAlipayRenewalRequest params:【 id:" + id + " money:" + money + "】");
        if (id == null || StringUtils.isBlank(id.toString()) || money == null || money <= 0) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            return service.payAlipayRenewalRequest(id, money);
        } catch (BizException e) {
            logger.error("AlipayRepayController.payAlipayRenewalRequest(支付宝主动续期) has BizException,params:【 id:" + id + " money:" + money + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("AlipayRepayController.payAlipayRenewalRequest(支付宝主动续期) has Exception,params:【 id:" + id + " money:" + money + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 主动请求支付宝还款
     *
     * @param id id
     * @return res
     */
    @ResponseBody
    @RequestMapping(value = "alipay_withhold_req")
    public ResponseContent payAlipayWithholdRequest(Integer id) throws Exception {
        logger.debug("AlipayRepayController.payAlipayWithholdRequest params: 【id:" + id + "】");
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            return service.payAlipayWithholdRequest(id);
        } catch (BizException e) {
            logger.error("AlipayRepayController.payAlipayWithholdRequest(主动请求支付宝还款) has BizException, params: 【id:" + id + "】,errorCode:" + e.getErrorCode() + " errorMsg:" + e.getErrorMsg());
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("AlipayRepayController.payAlipayWithholdRequest(主动请求支付宝还款) has BizException, params: 【id:" + id + "】,error:", e);
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 支付回调地址(支付宝)
     *
     * @param msg msg
     * @return str
     * @author tgy
     */
    @ResponseBody
    @RequestMapping(value = "alipay_notify")
    public String payAlipayCallback(@RequestBody String msg) {
        logger.debug("AlipayRepayController.payAlipayCallback params; 【msg:" + msg + "】");
        return service.payAlipayCallback(msg);
    }
}
