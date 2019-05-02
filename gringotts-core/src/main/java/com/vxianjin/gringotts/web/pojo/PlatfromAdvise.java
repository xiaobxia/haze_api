package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class PlatfromAdvise implements Serializable {
    private static final long serialVersionUID = 9169667055702388361L;//平台反馈
    private String id;
    private String userPhone;
    private String adviseConnectInfo;
    private String adviseContent;
    private Date adviseAddtime;
    private String adviseAddip;
    private String adviseStatus;
    private String feedbackWay;
    private String adviseFeedback;
    private Date feedDate;
    private String feedIp;
    private String hidden;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAdviseConnectInfo() {
        return adviseConnectInfo;
    }

    public void setAdviseConnectInfo(String adviseConnectInfo) {
        this.adviseConnectInfo = adviseConnectInfo;
    }

    public String getAdviseContent() {
        return adviseContent;
    }

    public void setAdviseContent(String adviseContent) {
        this.adviseContent = adviseContent;
    }

    public Date getAdviseAddtime() {
        return adviseAddtime;
    }

    public void setAdviseAddtime(Date adviseAddtime) {
        this.adviseAddtime = adviseAddtime;
    }

    public String getAdviseAddip() {
        return adviseAddip;
    }

    public void setAdviseAddip(String adviseAddip) {
        this.adviseAddip = adviseAddip;
    }

    public String getAdviseStatus() {
        return adviseStatus;
    }

    public void setAdviseStatus(String adviseStatus) {
        this.adviseStatus = adviseStatus;
    }

    public String getFeedbackWay() {
        return feedbackWay;
    }

    public void setFeedbackWay(String feedbackWay) {
        this.feedbackWay = feedbackWay;
    }

    public String getAdviseFeedback() {
        return adviseFeedback;
    }

    public void setAdviseFeedback(String adviseFeedback) {
        this.adviseFeedback = adviseFeedback;
    }

    public Date getFeedDate() {
        return feedDate;
    }

    public void setFeedDate(Date feedDate) {
        this.feedDate = feedDate;
    }

    public String getFeedIp() {
        return feedIp;
    }

    public void setFeedIp(String feedIp) {
        this.feedIp = feedIp;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "PlatfromAdvise [adviseAddip=" + adviseAddip
                + ", adviseAddtime=" + adviseAddtime + ", adviseConnectInfo="
                + adviseConnectInfo + ", adviseContent=" + adviseContent
                + ", adviseFeedback=" + adviseFeedback + ", adviseStatus="
                + adviseStatus + ", feedDate=" + feedDate + ", feedIp="
                + feedIp + ", feedbackWay=" + feedbackWay + ", hidden="
                + hidden + ", id=" + id + ", userPhone=" + userPhone + "]";
    }


}
