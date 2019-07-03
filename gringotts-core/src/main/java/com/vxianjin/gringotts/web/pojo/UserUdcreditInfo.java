package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑白名单实体
 * 
 * @author fully
 * @version 1.0.0
 * @date 2019-07-03 20:22:42
 */
 public class UserUdcreditInfo implements Serializable {

    private static final long serialVersionUID = 3353244800345351867L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 活体会话ID
     */
    private String livingSession;

    /**
     * 活体图片URL
     */
    private String livingImageUrl;

    /**
     * 头像会话ID
     */
    private String headerSession;

    /**
     * 头像图片URL
     */
    private String headerImageUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    /**
     * 获取主键Id
     *
     * @return id
     */
    public Integer getId(){
        return id;
    }

    /**
     * 设置主键Id
     *
     * @param id 要设置的主键Id
     */
    public void setId(Integer id){
        this.id = id;
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public Integer getUserId(){
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 要设置的用户ID
     */
    public void setUserId(Integer userId){
        this.userId = userId;
    }

    /**
     * 获取活体会话ID
     *
     * @return 活体会话ID
     */
    public String getLivingSession(){
        return livingSession;
    }

    /**
     * 设置活体会话ID
     *
     * @param livingSession 要设置的活体会话ID
     */
    public void setLivingSession(String livingSession){
        this.livingSession = livingSession;
    }

    /**
     * 获取活体图片URL
     *
     * @return 活体图片URL
     */
    public String getLivingImageUrl(){
        return livingImageUrl;
    }

    /**
     * 设置活体图片URL
     *
     * @param livingImageUrl 要设置的活体图片URL
     */
    public void setLivingImageUrl(String livingImageUrl){
        this.livingImageUrl = livingImageUrl;
    }

    /**
     * 获取头像会话ID
     *
     * @return 头像会话ID
     */
    public String getHeaderSession(){
        return headerSession;
    }

    /**
     * 设置头像会话ID
     *
     * @param headerSession 要设置的头像会话ID
     */
    public void setHeaderSession(String headerSession){
        this.headerSession = headerSession;
    }

    /**
     * 获取头像图片URL
     *
     * @return 头像图片URL
     */
    public String getHeaderImageUrl(){
        return headerImageUrl;
    }

    /**
     * 设置头像图片URL
     *
     * @param headerImageUrl 要设置的头像图片URL
     */
    public void setHeaderImageUrl(String headerImageUrl){
        this.headerImageUrl = headerImageUrl;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 要设置的创建时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return 修改时间
     */
    public Date getUpdateTime(){
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 要设置的修改时间
     */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }

}