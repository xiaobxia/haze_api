package com.vxianjin.gringotts.attach.pojo;

import java.util.Date;

/**
 * 借款用户坐标信息表
 *
 * @author zzc
 */
public class UserInfoTude {
    private Integer id; //主键
    private Integer userId; //用户表主键
    private String presentLatitude; //精度
    private String presentLongitude; //维度
    private String presentAddressDistinct; //详细地址
    private Date appCreatTime; //APP记录时间
    private Date creatTime; //创建时间
    private Date updateTime; //修改时间

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

    public String getPresentLatitude() {
        return presentLatitude;
    }

    public void setPresentLatitude(String presentLatitude) {
        this.presentLatitude = presentLatitude;
    }

    public String getPresentLongitude() {
        return presentLongitude;
    }

    public void setPresentLongitude(String presentLongitude) {
        this.presentLongitude = presentLongitude;
    }

    public String getPresentAddressDistinct() {
        return presentAddressDistinct;
    }

    public void setPresentAddressDistinct(String presentAddressDistinct) {
        this.presentAddressDistinct = presentAddressDistinct;
    }

    public Date getAppCreatTime() {
        return appCreatTime;
    }

    public void setAppCreatTime(Date appCreatTime) {
        this.appCreatTime = appCreatTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
