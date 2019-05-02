package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class Advertisement implements Serializable {
    private static final long serialVersionUID = -610529619490294404L;
    private int id;
    private String showType = "0";// 渠道类型，0表示PC端，1表示Android端，2表示IOS端，3表示其他
    private String channelType = "0";// 频道类型，0表示首页，1表示关于我们，2表示其他
    private String columnType = "0";// 栏目类型，0表示banner轮播页，1表示滚动消息设置
    private String startTime = "0";// 活动开始时间
    private String endTime = "0";// 活动结束时间
    private String presentWay = "0";// 发布方式，0表示立即发布，1表示定时发布
    private String url;// 图片路径
    private String reurl;// 链接
    private String title;// 标题
    private String userLevel = "0";// 用户群体，0表示全部，1表示V1，以此类推
    private String status = "0";// 弹窗，0表示弹窗，1表示不弹窗
    private String sort;// 排序

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPresentWay() {
        return presentWay;
    }

    public void setPresentWay(String presentWay) {
        this.presentWay = presentWay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReurl() {
        return reurl;
    }

    public void setReurl(String reurl) {
        this.reurl = reurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
