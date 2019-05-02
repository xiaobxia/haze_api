package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 退款
 *
 * @author Administrator
 */
public class RepaymentReturn implements Serializable {
    // 退款
    public static final int RETURN_TYPE_ALIPAY = 1;
    public static final int RETURN_TYPE_FUYOU = 2;
    public static final int RETURN_TYPE_LIANLIAN = 3;

    public static final Map<Integer, String> RETURN_TYPE = new HashMap<Integer, String>(); // 退款
    private static final long serialVersionUID = 4310440851084502745L;

    static {
        RETURN_TYPE.put(RETURN_TYPE_ALIPAY, "支付宝");
        RETURN_TYPE.put(RETURN_TYPE_FUYOU, "富友");
        RETURN_TYPE.put(RETURN_TYPE_LIANLIAN, "连连");
    }

    private Integer id;
    private Integer assetOrderId;
    private Integer assetRepaymentId;
    private Integer assetRepaymentDetailId;
    private Integer userId;
    private Integer returnType;
    private Long repaymentReturnMoney;
    private String remark;
    private String returnOrderId;
    private String adminUsername;
    private Date createdAt;
    private Date updatedAt;
    private Date returnTime;

    private Integer returnSource;  //退款来源，1：还款；2：续期


    private String returnTimeStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetOrderId() {
        return assetOrderId;
    }

    public void setAssetOrderId(Integer assetOrderId) {
        this.assetOrderId = assetOrderId;
    }

    public Integer getAssetRepaymentId() {
        return assetRepaymentId;
    }

    public void setAssetRepaymentId(Integer assetRepaymentId) {
        this.assetRepaymentId = assetRepaymentId;
    }

    public Integer getAssetRepaymentDetailId() {
        return assetRepaymentDetailId;
    }

    public void setAssetRepaymentDetailId(Integer assetRepaymentDetailId) {
        this.assetRepaymentDetailId = assetRepaymentDetailId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Long getRepaymentReturnMoney() {
        return repaymentReturnMoney;
    }

    public void setRepaymentReturnMoney(Long repaymentReturnMoney) {
        this.repaymentReturnMoney = repaymentReturnMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnOrderId() {
        return returnOrderId;
    }

    public void setReturnOrderId(String returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnTimeStr() {
        return returnTimeStr;
    }

    public void setReturnTimeStr(String returnTimeStr) {
        this.returnTimeStr = returnTimeStr;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReturnSource() {
        return returnSource;
    }

    public void setReturnSource(Integer returnSource) {
        this.returnSource = returnSource;
    }

}