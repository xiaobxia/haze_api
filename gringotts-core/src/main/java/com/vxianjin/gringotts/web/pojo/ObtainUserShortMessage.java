package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class ObtainUserShortMessage implements Serializable {
    private static final long serialVersionUID = 7961756469759684601L;
    private String id;
    private String messageContent;
    private String messageDate;
    private String phone;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ObtainUserShortMessage [id=" + id + ", messageContent="
                + messageContent + ", messageDate=" + messageDate + ", phone="
                + phone + ", userId=" + userId + "]";
    }


}
