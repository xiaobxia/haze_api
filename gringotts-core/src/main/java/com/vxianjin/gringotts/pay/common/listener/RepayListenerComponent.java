package com.vxianjin.gringotts.pay.common.listener;

import com.vxianjin.gringotts.pay.model.event.RepayEvent;
import com.vxianjin.gringotts.web.util.SendSmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: chenkai
 * @Date: 2018/7/17 10:59
 * @Description: 还款短信发送
 */
@Component
public class RepayListenerComponent implements ApplicationListener<RepayEvent> {

    private final static Logger logger = LoggerFactory.getLogger(PayListenerComponent.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void onApplicationEvent(RepayEvent baseEvent) {
        System.out.println("RepayListenerComponent 接收消息======================>");
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                logger.info(MessageFormat.format("用户 : {0}, {1}发送短信通知===>",baseEvent.getUserPhone(),baseEvent.getType()));
                SendSmsUtil.sendSmsDiyCL(baseEvent.getUserPhone(), baseEvent.getMsg());
            }
        });
    }
}
