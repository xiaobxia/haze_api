package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/*
 * 人脸识别状态报告
 */
public class FaceRecognition implements Serializable {
    private static final long serialVersionUID = 6340723208907422395L;
    private Integer id;
    private Integer userId;
    private String le3;
    private String le4;
    private String le5;
    private String le6;
    private String confidence;
    private String status;
    private Date createTime;
    private Date updatetime;

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

    public String getLe3() {
        return le3;
    }

    public void setLe3(String le3) {
        this.le3 = le3;
    }

    public String getLe4() {
        return le4;
    }

    public void setLe4(String le4) {
        this.le4 = le4;
    }

    public String getLe5() {
        return le5;
    }

    public void setLe5(String le5) {
        this.le5 = le5;
    }

    public String getLe6() {
        return le6;
    }

    public void setLe6(String le6) {
        this.le6 = le6;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}
