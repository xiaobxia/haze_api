package com.vxianjin.gringotts.web.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BackMessageCenter {

    /**
     * 消息类型
     */
    public static final Map<Integer, String> NOTICE_TYPE = new HashMap<Integer, String>();
    /**
     * 站内信
     */
    public static final Integer MESSAGE = 1;
    /**
     * 邮件
     */
    public static final Integer EMAIL = 2;
    /**
     * 短信
     */
    public static final Integer SMS = 3;

    /**
     * 消息状态
     */
    public static final Map<Integer, String> STATUS = new HashMap<Integer, String>();
    /**
     * 未读
     */
    public static final Integer STATUS_UNREAD = 0;
    /**
     * 已读
     */
    public static final Integer STATUS_READED = 1;
    /**
     * 发送成功
     */
    public static final Integer STATUS_SUCCESS = 2;
    /**
     * 发送失败
     */
    public static final Integer STATUS_FAILD = 3;

    static {
        NOTICE_TYPE.put(MESSAGE, "站内信");
        NOTICE_TYPE.put(EMAIL, "邮件");
        NOTICE_TYPE.put(SMS, "手机短信");
        STATUS.put(STATUS_UNREAD, "未读");
        STATUS.put(STATUS_READED, "已读");
        STATUS.put(STATUS_SUCCESS, "发送成功");
        STATUS.put(STATUS_FAILD, "发送失败");
    }

    private Integer id;

    private Integer sendUserId;

    private Integer receiveUserId;

    private String messageTitle;

    private Integer messageStatus;

    private String messageContent;

    private Date messageSendDatetime;

    private String messageSendIp;

    private Integer noticeTypeId;
    private BackUser sendUser;
    private User receiveUser;

    private String viewNoticeType;

    private String viewMsgStatus;

    private String messageAddress;

    private String receiveAccount;// 发送人账号

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getMessageAddress() {
        return messageAddress;
    }

    public void setMessageAddress(String messageAddress) {
        this.messageAddress = messageAddress;
    }

    public String getViewMsgStatus() {
        return viewMsgStatus;
    }

    public void setViewMsgStatus(String viewMsgStatus) {
        this.viewMsgStatus = viewMsgStatus;
    }

    public String getViewNoticeType() {
        return viewNoticeType;
    }

    public void setViewNoticeType(String viewNoticeType) {
        this.viewNoticeType = viewNoticeType;
    }

    public BackUser getSendUser() {
        return sendUser;
    }

    public void setSendUser(BackUser sendUser) {
        this.sendUser = sendUser;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Integer receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle == null ? null : messageTitle.trim();
    }

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
        setViewMsgStatus(STATUS.get(messageStatus));
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent
                .trim();
    }

    public Date getMessageSendDatetime() {
        return messageSendDatetime;
    }

    public void setMessageSendDatetime(Date messageSendDatetime) {
        this.messageSendDatetime = messageSendDatetime;
    }

    public String getMessageSendIp() {
        return messageSendIp;
    }

    public void setMessageSendIp(String messageSendIp) {
        this.messageSendIp = messageSendIp == null ? null : messageSendIp
                .trim();
    }

    public Integer getNoticeTypeId() {
        return noticeTypeId;
    }

    public void setNoticeTypeId(Integer noticeTypeId) {
        this.noticeTypeId = noticeTypeId;
        setViewNoticeType(NOTICE_TYPE.get(noticeTypeId));
    }
}