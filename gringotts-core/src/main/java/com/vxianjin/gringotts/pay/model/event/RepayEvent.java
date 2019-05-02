package com.vxianjin.gringotts.pay.model.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: chenkai
 * @Date: 2018/8/6 11:15
 * @Description:
 */
public class RepayEvent  extends ApplicationEvent {
    private static final long serialVersionUID = -8498120898657970028L;

    private String            type;
    //消息内容
    private String            msg;
    //发送手机号
    private String            userPhone;

    public RepayEvent(Object source, String type, String msg,String userPhone) {
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
