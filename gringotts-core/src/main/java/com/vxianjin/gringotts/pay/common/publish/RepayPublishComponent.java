package com.vxianjin.gringotts.pay.common.publish;

import com.vxianjin.gringotts.pay.model.event.RepayEvent;
import org.springframework.context.ApplicationContext;

/**
 * @Author: chenkai
 * @Date: 2018/7/17 11:26
 * @Description: 还款(代扣)消息发送组件
 */
public class RepayPublishComponent implements PublishAdapter{

    @Override
    public void publishMsg(ApplicationContext applicationContext,String type, String message,String phone) {
        applicationContext.publishEvent(new RepayEvent(this,type,message,phone));
    }
}
