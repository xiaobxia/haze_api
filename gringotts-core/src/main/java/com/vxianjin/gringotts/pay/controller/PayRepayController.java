package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.service.PayRepayService;
import com.vxianjin.gringotts.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 借款代扣部分
 * @author  jintian on 2018/7/18.
 */
@Controller
@RequestMapping(value = "/pay")
public class PayRepayController {

    private Logger logger = LoggerFactory.getLogger(PayRepayController.class);

    @Resource
    private PayRepayService payRepayService;

    /**
     * 定时代扣（还款）
     */
    @ResponseBody
    @RequestMapping(value = "auto-withhold")
    public ResponseContent autoWithhold(Integer id) {
        logger.debug("PayRepayController.autoWithhold params:【id:" + id + "】");
        if (id == null) {
            return new ResponseContent("-101", "请求参数非法");
        }
        try {
            return payRepayService.autoWithhold(id);
        } catch (BizException e) {
            return new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        }catch (Exception e){
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

    /**
     * 催收代扣（还款）
     */
    @ResponseBody
    @RequestMapping(value = "collection-withhold/{userId}/{repaymentId}/{money}/{withholdId}/{sign}")
    public ResponseContent collectionWithhold(@PathVariable String userId,
                                              @PathVariable String repaymentId, @PathVariable Long money,
                                              @PathVariable String withholdId, @PathVariable String sign) {
        logger.debug("PayRepayController.collectionWithhold params;【userId：" + userId + " repaymentId:" + repaymentId + " money:" + money
                + " withholdId:" + withholdId + " sign:" + sign + "】");
        //校验请求参数
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(repaymentId)
                || StringUtils.isBlank(withholdId) || StringUtils.isBlank(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数不符合要求");
        }
        try{
            return payRepayService.collectionWithhold(userId, repaymentId, money, withholdId, sign);
        }catch (Exception e){
            return new ResponseContent("-101", "系统异常，请稍后重试");
        }
    }

}
