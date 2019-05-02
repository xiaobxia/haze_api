package com.vxianjin.gringotts.pay.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.component.YeepayService;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPRepayRecordReq;
import com.vxianjin.gringotts.pay.model.YPRepayResultModel;
import com.vxianjin.gringotts.pay.task.MqMessageSynTask;
import com.vxianjin.gringotts.pay.task.PayStatusSynTask;
import com.vxianjin.gringotts.pay.task.UserQuotaSynTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * 定时任务调用接口
 * @author : chenkai
 * @date  :2018/7/20 14:03
 */
@Controller
@ResponseBody
@RequestMapping(value = "/gringotts/task/")
public class YeepayTaskController {

    private final static Logger logger = LoggerFactory.getLogger(YeepayTaskController.class);

    @Resource
    private PayStatusSynTask payStatusSynTask;

    @Resource
    private MqMessageSynTask mqMessageSynTask;


    @Resource
    private YeepayService yeepayService;

    @Resource
    private UserQuotaSynTask userQuotaSynTask;

    @RequestMapping(value = "pay/status/syn")
    public void executeTask(){
        logger.info("server 调用executeTask 开始执行用户支付状态主动同步定时====================>");
        payStatusSynTask.execute();
    }


    @RequestMapping(value = "mq/syn")
    public void executeMqSynTask(){
        logger.info("server 调用executeMqSynTask 开始执行mq消息状态同步定时====================>");
        mqMessageSynTask.execute();
    }

    @RequestMapping(value = "pay/status/query")
    public ResultModel queryPayStatus(String orderNo,String userId){
        YPRepayRecordReq repayRecordReq = new YPRepayRecordReq();
        repayRecordReq.setMerchantNo(PayConstants.MERCHANT_NO);
        repayRecordReq.setRequestNo(orderNo);
        ResultModel<YPRepayResultModel> resultModel =  yeepayService.getYBRepayResult(repayRecordReq,userId);
        logger.info(MessageFormat.format("用户支付状态,主动查询,查询结果: {0}", JSON.toJSONString(resultModel)));
        return resultModel;
    }

    @RequestMapping("/userQuota/syn")
    public void userQuotaSyn(){
        logger.info("start user quota syn");
        userQuotaSynTask.userQuotaSyn();
        logger.info("end user quota syn");
    }

    @RequestMapping("/everyDayuserQuota/syn")
    public void everyDayuserQuotaSyn(){
        logger.info("start every day user quota syn");
        userQuotaSynTask.everyDayuserQuota();
        logger.info("end every day user quota syn");
    }
}

