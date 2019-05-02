package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付订单
 * @author zed
 */
public class OutOrders implements Serializable {
    /**
     * 该笔请求等待中
     */
    public static final String STATUS_WAIT = "0";
    /**
     * 该笔请求的非成功状态
     */
    public static final String STATUS_OTHER = "1";
    /**
     * 该笔请求的成功状态
     */
    public static final String STATUS_SUC = "2";

    public static final String TYPE_FUYOU = "FUYOU";
    public static final String TYPE_LIANLIAN = "LIANLIAN";
    public static final String TYPE_YINSHENG = "YINSHENG";
    public static final String TYPE_BAOFOO = "BAOFOO";
    public static final String TYPE_RONGBAO = "RONGBAO";
    public static final String TYPE_YEEPAY = "YEEPAY";
    public static final String TYPE_UCFPAY = "UCFPAY";
    public static final String TYPE_ALIPAY = "ALIPAY";

    public static final String ACT_RENEWAL = "RENEWAL";
    public static final String ACT_REPAY = "REPAY";
    public static final String ACT_GET_TOKEN = "GET_TOKEN";
    public static final String ACT_VALIDATE_TOKEN = "VALIDATE_TOKEN";
    public static final String ACT_SIGN_GRANT = "SIGN_GRANT";
    public static final String ACT_GRANT = "GRANT";
    public static final String ACT_REPAY_DEBIT = "REPAY_DEBIT";//主动还款
    public static final String ACT_TASK_DEBIT = "TASK_DEBIT";//定时还款
    public static final String ACT_COLLECTION_DEBIT = "COLLECTIO_DEBIT";//催收还款

    public static final String ACT_DEBIT = "DEBIT";
    public static final String ACT_BANK_CARD_BIN = "BANK_CARD_BIN";
    public static final String act_NTQRYEBP_A = "NTQRYEBP_A";
    public static final String act_AgentRequest_A = "AgentRequest_A";
    public static final String act_NTQRYEBP_B = "act_NTQRYEBP_B";
    public static final String act_AgentRequest_B = "act_AgentRequest_B";
    public static final String act_NTQRYEBP_C = "act_NTQRYEBP_C";
    public static final String act_AgentRequest_C = "act_AgentRequest_C";
    public static final String act_NTQRYEBP_H = "act_NTQRYEBP_H";
    public static final String act_AgentRequest_H = "act_AgentRequest_H";
    public static final String orderType_cmb = "CMB";
    public static final String orderType_KD = "KD_DOCk";
    public static final String orderType_ZCM = "ZCM_DOCk";
    private static final long serialVersionUID = -5492819634488198720L;
    private Integer id;
    private String userId;
    private Integer assetOrderId;
    private String orderType;
    private String orderNo;
    private String act;
    private String tablelastName;//表面后缀
    private String reqParams;
    private String returnParams;
    private Date notifyTime;
    private String notifyParams;
    private Date addTime;
    private String addIp;
    private Date updateTime;
    private String status;

    public String getTablelastName() {
        return tablelastName;
    }

    public void setTablelastName(String tablelastName) {
        this.tablelastName = tablelastName;
    }

    public String getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(String notifyParams) {
        this.notifyParams = notifyParams;
    }

    public Integer getAssetOrderId() {
        return assetOrderId;
    }

    public void setAssetOrderId(Integer assetOrderId) {
        this.assetOrderId = assetOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }

    public String getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(String returnParams) {
        this.returnParams = returnParams;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddIp() {
        return addIp;
    }

    public void setAddIp(String addIp) {
        this.addIp = addIp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RiskOrders [userId=" + userId + ", orderType=" + orderType + ", act=" + act + ", addIp=" + addIp + ", addTime=" + addTime + ", id=" + id + ", notifyParams=" + notifyParams + ", notifyTime=" + notifyTime + ", orderNo=" + orderNo + ", reqParams=" + reqParams + ", returnParams=" + returnParams + ", status=" + status + ", updateTime=" + updateTime + "]";
    }

}
