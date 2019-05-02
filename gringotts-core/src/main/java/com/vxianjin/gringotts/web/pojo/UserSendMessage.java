package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserSendMessage implements Serializable {
    /**
     * 消息状态
     */
    public static final Map<String, String> USER_STATUS = new HashMap<String, String>();
    public static final String STATUS_READED = "1";
    public static final String STATUS_SUCCESS = "2";
    public static final String STATUS_FAILD = "3";
    private static final long serialVersionUID = -8704522232449809347L;

    static {
        USER_STATUS.put(STATUS_READED, "待发送成功");
        USER_STATUS.put(STATUS_SUCCESS, "发送成功");
        USER_STATUS.put(STATUS_FAILD, "发送失败");
    }

    private String id;
    private String phone;
    private String messageContent;
    private Date messageCreateTime;
    private String messageStatus;
    private String sendIp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getMessageCreateTime() {
        return messageCreateTime;
    }

    public void setMessageCreateTime(Date messageCreateTime) {
        this.messageCreateTime = messageCreateTime;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getSendIp() {
        return sendIp;
    }

    public void setSendIp(String sendIp) {
        this.sendIp = sendIp;
    }

    @Override
    public String toString() {
        return "UserSendMessage [id=" + id + ", messageContent="
                + messageContent + ", messageCreateTime=" + messageCreateTime
                + ", messageStatus=" + messageStatus + ", phone=" + phone
                + ", sendIp=" + sendIp + "]";
    }


}
