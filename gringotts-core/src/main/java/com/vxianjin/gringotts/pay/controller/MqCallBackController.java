package com.vxianjin.gringotts.pay.controller;

import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.service.MqInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * @author : chenkai
 * @date : 2018/8/24 11:01
 */
@Controller
@ResponseBody
@RequestMapping(value = "/gringotts/mq/")
public class MqCallBackController {

    private final static Logger logger = LoggerFactory.getLogger(MqCallBackController.class);

    @Resource
    private MqInfoService mqInfoService;

    @RequestMapping(value = "callback")
    public ResultModel executeMqSynTask(String mqKey, boolean status){
        logger.info(MessageFormat.format("mq 消息消费回调接口======> mqKey: {0},status: {1}",mqKey,status));
        try {
            mqInfoService.updateMqStatus(mqKey,status);
        }catch (Exception e){
            return new ResultModel(false);
        }
        return new ResultModel(true);
    }
}
