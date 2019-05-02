package com.vxianjin.gringotts.web.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BackNotice {
    /*** 短消息 */
    public static final Map<Integer, String> ALL_MESSAGE = new HashMap<Integer, String>();
    /*** 邮箱 */
    public static final Map<Integer, String> ALL_EMAIL = new HashMap<Integer, String>();
    /**
     * 手机短信
     */
    public static final Map<Integer, String> ALL_PHONE = new HashMap<Integer, String>();
    /**
     * 提醒的状态，1.禁用；2.启用
     */
    public static final Map<Integer, String> ALL_NOTICE_STATUS = new HashMap<Integer, String>();

    /**
     * 短消息发送
     */
    public static final Integer MESSAGE_REQUIRED_SELECTED = 1;
    /**
     * 短消息不发送
     */
    public static final Integer MESSAGE_REQUIRED_UNSELECTED = 2;
    /**
     * 短消息可选已选
     */
    public static final Integer MESSAGE_CHOOSABLE_SELECTED = 3;
    /**
     * 短消息可选未选
     */
    public static final Integer MESSAGE_CHOOSABLE_UNSELECTED = 4;

    /**
     * 邮箱发送
     */
    public static final Integer EMAIL_REQUIRED_SELECTED = 1;
    /**
     * 邮箱不发送
     */
    public static final Integer EMAIL_REQUIRED_UNSELECTED = 2;
    /**
     * 邮箱可选已选
     */
    public static final Integer EMAIL_CHOOSABLE_SELECTED = 3;
    /**
     * 邮箱可选未选
     */
    public static final Integer EMAIL_CHOOSABLE_UNSELECTED = 4;

    /**
     * 手机短信发送
     */
    public static final Integer PHONE_REQUIRED_SELECTED = 1;
    /**
     * 手机短信不发送
     */
    public static final Integer PHONE_REQUIRED_UNSELECTED = 2;
    /**
     * 手机短信可选已选
     */
    public static final Integer PHONE_CHOOSABLE_SELECTED = 3;
    /**
     * 手机短信可选未选
     */
    public static final Integer PHONE_CHOOSABLE_UNSELECTED = 4;

    /**
     * 停用
     */
    public static final Integer NOTICE_STATUS_STOP = 1;
    /**
     * 启用
     */
    public static final Integer NOTICE_STATUS_START = 2;
    /**
     * VIP续费成功
     */
    public static final String VIP_SUC = "VIP_SUC";

    static {
        ALL_MESSAGE.put(MESSAGE_REQUIRED_SELECTED, "发送");
        ALL_MESSAGE.put(MESSAGE_REQUIRED_UNSELECTED, "不发送");
        // ALL_MESSAGE.put(MESSAGE_CHOOSABLE_SELECTED, "可选已选");
        // ALL_MESSAGE.put(MESSAGE_CHOOSABLE_UNSELECTED, "可选未选");

        ALL_EMAIL.put(EMAIL_REQUIRED_SELECTED, "发送");
        ALL_EMAIL.put(EMAIL_REQUIRED_UNSELECTED, "不发送");
        // ALL_EMAIL.put(EMAIL_CHOOSABLE_SELECTED, "可选已选");
        // ALL_EMAIL.put(EMAIL_CHOOSABLE_UNSELECTED, "可选未选");

        ALL_PHONE.put(PHONE_REQUIRED_SELECTED, "发送");
        ALL_PHONE.put(PHONE_REQUIRED_UNSELECTED, "不发送");
        // ALL_PHONE.put(PHONE_CHOOSABLE_SELECTED, "可选已选");
        // ALL_PHONE.put(PHONE_CHOOSABLE_UNSELECTED, "可选未选");

        ALL_NOTICE_STATUS.put(NOTICE_STATUS_STOP, "停用");
        ALL_NOTICE_STATUS.put(NOTICE_STATUS_START, "启用");
    }

    private String viewMessage;

    private String viewEmail;

    private String viewPhone;

    private String viewNoticeStatus;

    private Integer id;

    private String noticeTitle;

    private String noticeCode;

    private Integer noticeStatus;

    private Integer noticeSequence;

    private Integer message;

    private Integer email;

    private Integer phone;

    private Date noticeAddtime;
    private Date noticeUpdatetime;

    public Date getNoticeUpdatetime() {
        return noticeUpdatetime;
    }

    public void setNoticeUpdatetime(Date noticeUpdatetime) {
        this.noticeUpdatetime = noticeUpdatetime;
    }

    public String getViewMessage() {
        return viewMessage;
    }

    public void setViewMessage(String viewMessage) {
        this.viewMessage = viewMessage;
    }

    public String getViewEmail() {
        return viewEmail;
    }

    public void setViewEmail(String viewEmail) {
        this.viewEmail = viewEmail;
    }

    public String getViewPhone() {
        return viewPhone;
    }

    public void setViewPhone(String viewPhone) {
        this.viewPhone = viewPhone;
    }

    public String getViewNoticeStatus() {
        return viewNoticeStatus;
    }

    public void setViewNoticeStatus(String viewNoticeStatus) {
        this.viewNoticeStatus = viewNoticeStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    public String getNoticeCode() {
        return noticeCode;
    }

    public void setNoticeCode(String noticeCode) {
        this.noticeCode = noticeCode == null ? null : noticeCode.trim();
    }

    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
        setViewNoticeStatus(ALL_NOTICE_STATUS.get(noticeStatus));
    }

    public Integer getNoticeSequence() {
        return noticeSequence;
    }

    public void setNoticeSequence(Integer noticeSequence) {
        this.noticeSequence = noticeSequence;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
        setViewMessage(ALL_MESSAGE.get(message));
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
        setViewEmail(ALL_EMAIL.get(email));
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
        setViewPhone(ALL_PHONE.get(phone));
    }

    public Date getNoticeAddtime() {
        return noticeAddtime;
    }

    public void setNoticeAddtime(Date noticeAddtime) {
        this.noticeAddtime = noticeAddtime;
    }

}
