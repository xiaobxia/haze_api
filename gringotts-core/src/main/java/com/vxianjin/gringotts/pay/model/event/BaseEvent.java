package com.vxianjin.gringotts.pay.model.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: chenkai
 * @Date: 2018/7/16 16:30
 * @Description: 短信事件event
 */
public class BaseEvent extends ApplicationEvent {
    private static final long serialVersionUID = -5967854136936843495L;

    private String            type;
    //消息内容
    private String            msg;
    //发送手机号
    private String            userPhone;

    public BaseEvent(Object source, String type, String msg,String userPhone) {
        super(source);
        this.type = type;
        this.msg = msg;
        this.userPhone = userPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
