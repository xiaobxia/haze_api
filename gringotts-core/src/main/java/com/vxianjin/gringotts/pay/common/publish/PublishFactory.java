package com.vxianjin.gringotts.pay.common.publish;

import com.vxianjin.gringotts.pay.common.enums.EventTypeEnum;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: chenkai
 * @Date: 2018/7/17 11:36
 * @Description: 消息发送工厂类
 */
public class PublishFactory {

    public static PublishAdapter getPublishAdapter(String type)  throws PayException {
        if (StringUtils.isBlank(type)){
            throw new PayException("消息类型不允许为空");
        }

        switch (EventTypeEnum.valueOf(type)){
            case REPAY:
                return new RepayPublishComponent();
            case PAY:
                return new PayPublishComponent();
            default:
                return null;
        }
    }
}
