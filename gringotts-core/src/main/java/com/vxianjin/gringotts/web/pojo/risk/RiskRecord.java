package com.vxianjin.gringotts.web.pojo.risk;

import java.util.Date;

/**
 * Created by lucy on 2019/5/3.
 */
public class RiskRecord {
    private Integer id;//主键id
    private Integer userId;//用户id
    private String requestId;//流水号
    private Integer returnCode;//返回编码
    private String returnInfo;//接口请求结果
    private String gxbReportUrl;//公信宝运营商报告URL
    private Integer score;//风控分数
    private Date createTime;//创建时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getGxbReportUrl() {
        return gxbReportUrl;
    }

    public void setGxbReportUrl(String gxbReportUrl) {
        this.gxbReportUrl = gxbReportUrl;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RiskRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", requestId='" + requestId + '\'' +
                ", returnCode=" + returnCode +
                ", returnInfo='" + returnInfo + '\'' +
                ", gxbReportUrl='" + gxbReportUrl + '\'' +
                ", score=" + score +
                ", createTime=" + createTime +
                '}';
    }
}
