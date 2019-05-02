/**
 * <p>Title: YouGetMoneyMeRepayInfo.java</p>
 *
 * @Package com.vxianjin.gringotts.web.pojo
 * <p>Description: 你借钱我来还实体类</p>
 * <p>Company:现金侠</p>
 * @author lixingxing
 * @version V1.0
 * @since 2017年3月13日 下午5:09:28
 */
package com.vxianjin.gringotts.web.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Description: 你借钱我来还实体类</p>
 * <p>Company:现金侠</p>
 *
 * @author lixingxing
 * @version V1.0
 */
public class YouGetMoneyMeRepayInfo {

    private int id;//自增id

    private String userName;//用户名

    private String userTelephone;//用户手机号

    private BigDecimal loanAmount;//借款金额

    private Date createDate;//创建时间

    private Date updateDate;//更新时间

    private String isDel;//是否删除标志

    private Date freechargeDate;//免单时间

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return userTelephone
     */
    public String getUserTelephone() {
        return userTelephone;
    }

    /**
     * @param userTelephone the userTelephone to set
     */
    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    /**
     * @return loanAmount
     */
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    /**
     * @param loanAmount the loanAmount to set
     */
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return isDel
     */
    public String getIsDel() {
        return isDel;
    }

    /**
     * @param isDel the isDel to set
     */
    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    /**
     * @return freechargeDate
     */
    public Date getFreechargeDate() {
        return freechargeDate;
    }

    /**
     * @param freechargeDate the freechargeDate to set
     */
    public void setFreechargeDate(Date freechargeDate) {
        this.freechargeDate = freechargeDate;
    }


}
