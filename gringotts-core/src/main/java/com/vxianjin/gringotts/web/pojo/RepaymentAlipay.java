package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RepaymentAlipay implements Serializable {
    /**
     * 该笔请求等待中
     */
    public static final int STATUS_WAIT = 0;
    /**
     * 该笔请求的非成功状态
     */
    public static final int STATUS_FAIL = 1;
    /**
     * 该笔请求的成功状态
     */
    public static final int STATUS_SUC = 2;
    /**
     * 经过还款后还未还完的
     */
    public static final int STATUS_BF = 3;
    private static final long serialVersionUID = -7746891691706504730L;
    private Integer id;
    private Date repayTime;
    private String alipayOrderNo;
    private Integer moneyType;
    private Double moneyAmount;
    private String alipayPhone;
    private String alipayName;
    private String remarkPhone;
    private String remarkName;
    private String alipayRemark;
    private String remark;
    private Integer status;
    private Integer assetRepaymentId;
    private Integer assetRepaymentDetailId;
    private Date createTime;
    private Date updateTime;
    private String repayTimeStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(Date repayTime) {
        this.repayTime = repayTime;
    }

    public String getAlipayOrderNo() {
        return alipayOrderNo;
    }

    public void setAlipayOrderNo(String alipayOrderNo) {
        this.alipayOrderNo = alipayOrderNo == null ? null : alipayOrderNo.trim();
    }

    public Integer getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getAlipayPhone() {
        return alipayPhone;
    }

    public void setAlipayPhone(String alipayPhone) {
        this.alipayPhone = alipayPhone == null ? null : alipayPhone.trim();
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName == null ? null : alipayName.trim();
    }

    public String getRemarkPhone() {
        return remarkPhone;
    }

    public void setRemarkPhone(String remarkPhone) {
        this.remarkPhone = remarkPhone == null ? null : remarkPhone.trim();
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName == null ? null : remarkName.trim();
    }

    public String getAlipayRemark() {
        return alipayRemark;
    }

    public void setAlipayRemark(String alipayRemark) {
        this.alipayRemark = alipayRemark == null ? null : alipayRemark.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getRepayTimeStr() {
        return repayTimeStr;
    }

    public void setRepayTimeStr(String repayTimeStr) {
        if (null != repayTimeStr) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                setRepayTime(dateFormat.parse(repayTimeStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.repayTimeStr = repayTimeStr;
    }
}