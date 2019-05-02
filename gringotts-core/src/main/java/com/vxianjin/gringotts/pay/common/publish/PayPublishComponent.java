package com.vxianjin.gringotts.pay.common.publish;

import com.vxianjin.gringotts.pay.model.event.BaseEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: chenkai
 * @Date: 2018/7/16 18:29
 * @Description: 支付(代收)消息发送组件
 */
@Component
public class PayPublishComponent implements PublishAdapter{

    @Override
    public void publishMsg(ApplicationContext applicationContext,String type, String message,String phone) {
        applicationContext.publishEvent(new BaseEvent(this,type,message,phone));
    }
}
