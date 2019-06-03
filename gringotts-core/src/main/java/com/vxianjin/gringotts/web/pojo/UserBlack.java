package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑白名单实体
 * 
 * @author fully
 * @version 1.0.0
 * @date 2019-06-03 17:11:35
 */
 public class UserBlack implements Serializable {

    private static final long serialVersionUID = 1248611341961932071L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 电话
     */
    private String userPhone;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 预留字段 暂时默认存0 未删除 1 已删除
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 0 黑名单 1 白名单
     */
    private Integer userType;


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
     * @param 要设置的主键Id
     */
    public void setId(Integer id){
      this.id = id;
    }

    /**
     * 获取姓名
     *
     * @return 姓名
     */
    public String getUserName(){
      return userName;
    }

    /**
     * 设置姓名
     * 
     * @param userName 要设置的姓名
     */
    public void setUserName(String userName){
      this.userName = userName;
    }

    /**
     * 获取电话
     *
     * @return 电话
     */
    public String getUserPhone(){
      return userPhone;
    }

    /**
     * 设置电话
     * 
     * @param userPhone 要设置的电话
     */
    public void setUserPhone(String userPhone){
      this.userPhone = userPhone;
    }

    /**
     * 获取身份证号
     *
     * @return 身份证号
     */
    public String getIdNumber(){
      return idNumber;
    }

    /**
     * 设置身份证号
     * 
     * @param idNumber 要设置的身份证号
     */
    public void setIdNumber(String idNumber){
      this.idNumber = idNumber;
    }

    /**
     * 获取预留字段 暂时默认存0 未删除 1 已删除
     *
     * @return 预留字段 暂时默认存0 未删除 1 已删除
     */
    public Integer getStatus(){
      return status;
    }

    /**
     * 设置预留字段 暂时默认存0 未删除 1 已删除
     * 
     * @param status 要设置的预留字段 暂时默认存0 未删除 1 已删除
     */
    public void setStatus(Integer status){
      this.status = status;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    public String getRemark(){
      return remark;
    }

    /**
     * 设置备注
     * 
     * @param remark 要设置的备注
     */
    public void setRemark(String remark){
      this.remark = remark;
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
     * 获取0 黑名单 1 白名单
     *
     * @return 0 黑名单 1 白名单
     */
    public Integer getUserType(){
      return userType;
    }

    /**
     * 设置0 黑名单 1 白名单
     * 
     * @param userType 要设置的0 黑名单 1 白名单
     */
    public void setUserType(Integer userType){
      this.userType = userType;
    }

}