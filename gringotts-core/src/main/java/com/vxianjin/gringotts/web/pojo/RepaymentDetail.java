package com.vxianjin.gringotts.web.pojo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RepaymentDetail implements Serializable {
    //  1、支付宝、2、银行卡主动还款、3、银行卡自动扣款、4、对公银行卡转账、5、线下还款
    public static final int TYPE_ALIPAY = 1;
    public static final int TYPE_BANK_CARD = 2;
    public static final int TYPE_BANK_CARD_AUTO = 3;
    public static final int TYPE_BANK_CARD_TRANSFER = 4;
    public static final int TYPE_OFF_LINE = 5;
    public static final int TYPE_OFF_LINE_DEDUCTION = 6;

    /**
     * 还款类型
     */
    public static final int TYPE_NORMALPAY = 1;
    public static final int TYPE_OVERDUEPAY = 2;
    public static final int TYPE_PARTPAY = 3;
    public static final int TYPE_REDUCEPAY = 4;
    public static final int TYPE_OTHERPAY = 5;

    /**
     * 还款渠道
     */
    public static final int CHANNEL_YIBAO = 7;
    public static final int CHANNEL_ALIPAY1 = 8;
    public static final int CHANNEL_ALIPAY2 = 9;
    public static final int CHANNEL_WECHAT = 10;
    public static final int CHANNEL_CARDWITHDRAW = 11;
    public static final int CHANNEL_EMPLOYEE = 12;
    public static final int CHANNEL_OTHER = 13;

    /**
     * 该笔请求等待中
     */
    public static final int STATUS_WAIT = 0;
    /**
     * 该笔请求的非成功状态
     */
    public static final int STATUS_OTHER = 1;
    /**
     * 该笔请求的成功状态
     */
    public static final int STATUS_SUC = 2;

    public static final Map<Integer, String> REPAY_TYPE = getRepayChannel();

    public static final Map<Integer, String> REPAY_CHANNEL = getRepayType();

    private static final long serialVersionUID = 3656170645532557621L;

    static {
//        REPAY_TYPE.put(TYPE_ALIPAY, "支付宝还款");
//        REPAY_TYPE.put(TYPE_BANK_CARD, "银行卡主动还款");
//        REPAY_TYPE.put(TYPE_BANK_CARD_AUTO, "银行卡自动扣款");
//        REPAY_TYPE.put(TYPE_BANK_CARD_TRANSFER, "对公银行卡转账");
//        REPAY_TYPE.put(TYPE_OFF_LINE, "线下还款");
//        REPAY_TYPE.put(TYPE_OFF_LINE_DEDUCTION, "线下协商免还款");
    }


    private Integer id;

    private Integer assetOrderId;

    private Integer assetRepaymentId;

    private Integer userId;

    private Integer repaymentType;

    private Long trueRepaymentMoney;

    private String remark;

    private String orderId;

    private Date createdAt;

    private Date updatedAt;

    private Integer cardId;

    private String adminUsername;

    private Integer status;

    private String repaymentImg;

    private Long returnMoney; //退款金额

    // 扩展字段
    private String createdAtStr;

    private String realname;
    private String userPhone;
    private Integer customerType;
    private Long repaymentPrincipal;
    private Long repaymentInterest;
    private Long repaymentAmount;
    private Long repaymentedAmount;
    private Date creditRepaymentTime;
    private Date repaymentTime;
    private Date repaymentRealTime;
    private String backOrderId;


    private BigDecimal trueRepaymentMoneyBig;

    //还款渠道
    private Integer repaymentChannel;

    /**
     * 获取支付类型
     *
     * @return
     */
    public static Map<Integer, String> getRepayType() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(TYPE_NORMALPAY, "正常还款");
        map.put(TYPE_OVERDUEPAY, "逾期还款");
        map.put(TYPE_PARTPAY, "部分还款");
        map.put(TYPE_REDUCEPAY, "减免还款");
        map.put(TYPE_OTHERPAY, "其他");
        return map;
    }

    /**
     * 获取支付渠道
     *
     * @return
     */
    public static Map<Integer, String> getRepayChannel() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        //原来还款类型渠道
        //		map.put(TYPE_ALIPAY, "支付宝还款");
        //		map.put(TYPE_BANK_CARD, "银行卡主动还款");
        //		map.put(TYPE_BANK_CARD_AUTO, "银行卡自动扣款");
        //		map.put(TYPE_BANK_CARD_TRANSFER, "对公银行卡转账");
        //		map.put(TYPE_OFF_LINE, "线下还款");
        //		map.put(TYPE_OFF_LINE_DEDUCTION, "线下协商免还款");
        //		map.put(CHANNEL_YIBAO,"易宝支付");
        //		map.put(CHANNEL_ALIPAY1,"线上支付宝转账");

        //只走线下
        map.put(CHANNEL_ALIPAY2, "线下支付宝转账");
        map.put(CHANNEL_WECHAT, "微信转账");
        map.put(CHANNEL_CARDWITHDRAW, "银行卡转账");
        map.put(CHANNEL_EMPLOYEE, "员工代收");
        map.put(CHANNEL_OTHER, "其他");
        return map;
    }


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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Long getTrueRepaymentMoney() {
        return trueRepaymentMoney;
    }

    public void setTrueRepaymentMoney(Long trueRepaymentMoney) {
        this.trueRepaymentMoney = trueRepaymentMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername == null ? null : adminUsername.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRepaymentImg() {
        return repaymentImg;
    }

    public void setRepaymentImg(String repaymentImg) {
        this.repaymentImg = repaymentImg == null ? null : repaymentImg.trim();
    }

    public String getCreatedAtStr() {
        return createdAtStr;
    }

    public void setCreatedAtStr(String createdAtStr) {
        if (StringUtils.isNotBlank(createdAtStr)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date createdAt = dateFormat.parse(createdAtStr);
                setCreatedAt(createdAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.createdAtStr = createdAtStr;
    }

    public BigDecimal getTrueRepaymentMoneyBig() {
        return trueRepaymentMoneyBig;
    }

    public void setTrueRepaymentMoneyBig(BigDecimal trueRepaymentMoneyBig) {
        if (null != trueRepaymentMoneyBig) {
            setTrueRepaymentMoney(trueRepaymentMoneyBig.multiply(new BigDecimal(100)).longValue());
        }
        this.trueRepaymentMoneyBig = trueRepaymentMoneyBig;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public Long getRepaymentPrincipal() {
        return repaymentPrincipal;
    }

    public void setRepaymentPrincipal(Long repaymentPrincipal) {
        this.repaymentPrincipal = repaymentPrincipal;
    }

    public Long getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(Long repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public Long getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Long repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Long getRepaymentedAmount() {
        return repaymentedAmount;
    }

    public void setRepaymentedAmount(Long repaymentedAmount) {
        this.repaymentedAmount = repaymentedAmount;
    }

    public Date getCreditRepaymentTime() {
        return creditRepaymentTime;
    }

    public void setCreditRepaymentTime(Date creditRepaymentTime) {
        this.creditRepaymentTime = creditRepaymentTime;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Date getRepaymentRealTime() {
        return repaymentRealTime;
    }

    public void setRepaymentRealTime(Date repaymentRealTime) {
        this.repaymentRealTime = repaymentRealTime;
    }

    public Long getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Long returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getBackOrderId() {
        return backOrderId;
    }

    public void setBackOrderId(String backOrderId) {
        this.backOrderId = backOrderId;
    }

    public Integer getRepaymentChannel() {
        return repaymentChannel;
    }

    public void setRepaymentChannel(Integer repaymentChannel) {
        this.repaymentChannel = repaymentChannel;
    }
}