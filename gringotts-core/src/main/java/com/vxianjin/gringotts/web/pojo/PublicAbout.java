package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

public class PublicAbout implements Serializable {
    private static final long serialVersionUID = 7383107153773448809L;//关于我们
    private String id;
    private String aboutContent;
    private String aboutIntroduce;
    private String aboutServiceTel;
    private String aboutOfficialQqGroup;
    private Date aboutAddTime;
    private Date aboutUpdateTime;
    private String aboutIp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAboutContent() {
        return aboutContent;
    }

    public void setAboutContent(String aboutContent) {
        this.aboutContent = aboutContent;
    }

    public String getAboutIntroduce() {
        return aboutIntroduce;
    }

    public void setAboutIntroduce(String aboutIntroduce) {
        this.aboutIntroduce = aboutIntroduce;
    }

    public String getAboutServiceTel() {
        return aboutServiceTel;
    }

    public void setAboutServiceTel(String aboutServiceTel) {
        this.aboutServiceTel = aboutServiceTel;
    }

    public String getAboutOfficialQqGroup() {
        return aboutOfficialQqGroup;
    }

    public void setAboutOfficialQqGroup(String aboutOfficialQqGroup) {
        this.aboutOfficialQqGroup = aboutOfficialQqGroup;
    }

    public Date getAboutAddTime() {
        return aboutAddTime;
    }

    public void setAboutAddTime(Date aboutAddTime) {
        this.aboutAddTime = aboutAddTime;
    }

    public Date getAboutUpdateTime() {
        return aboutUpdateTime;
    }

    public void setAboutUpdateTime(Date aboutUpdateTime) {
        this.aboutUpdateTime = aboutUpdateTime;
    }

    public String getAboutIp() {
        return aboutIp;
    }

    public void setAboutIp(String aboutIp) {
        this.aboutIp = aboutIp;
    }

    @Override
    public String toString() {
        return "PublicAbout [aboutAddTime=" + aboutAddTime + ", aboutContent="
                + aboutContent + ", aboutIntroduce=" + aboutIntroduce
                + ", aboutIp=" + aboutIp + ", aboutOfficialQqGroup="
                + aboutOfficialQqGroup + ", aboutServiceTel=" + aboutServiceTel
                + ", aboutUpdateTime=" + aboutUpdateTime + ", id=" + id + "]";
    }


}
