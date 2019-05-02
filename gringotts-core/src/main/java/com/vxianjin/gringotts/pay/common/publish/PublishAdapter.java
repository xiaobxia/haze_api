package com.vxianjin.gringotts.pay.common.publish;

import org.springframework.context.ApplicationContext;

/**
 * @Author: chenkai
 * @Date: 2018/7/17 11:26
 * @Description: 消息发送适配器，顶层接口
 */

public interface PublishAdapter {

    void publishMsg(ApplicationContext applicationContext, String type, String message,
                    String phone);
}
