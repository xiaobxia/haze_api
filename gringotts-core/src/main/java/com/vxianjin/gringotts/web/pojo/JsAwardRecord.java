package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 中奖记录
 *
 * @author Administrator
 */
public class JsAwardRecord implements Serializable {

    /**
     * 未开奖，
     */
    public static final Integer ZT_WKJ = 0;
    /**
     * 已开奖
     */
    public static final Integer ZT_YKJ = 1;
    /**
     * 已发放
     */
    public static final Integer ZT_YFF = 2;
    private static final long serialVersionUID = 4408892896720850611L;
    private String id;
    private Integer periods; //当前期数
    private BigDecimal awardMoney;//当前奖金
    private String userId; //中奖用户
    private String userName;//中奖用户名称
    private String drawrollsId;//中奖号码id
    private Integer drawrollsNumber;//中奖号码
    private Integer threeIndex;//第三方指数
    private String threeName;//第三方名称
    private Date moneyUpdateTime;//奖金更新时间
    private Date darwTime;//开奖时间
    private Date awardTime;//发奖时间
    private String remark;//备注
    private Integer status;// 状态（0：为开奖，1：已开奖，2：已发放）
    private String phone;
    private String statusItem;

    public String getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(String statusItem) {
        this.statusItem = statusItem;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public BigDecimal getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(BigDecimal awardMoney) {
        this.awardMoney = awardMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getDrawrollsId() {
        return drawrollsId;
    }

    public void setDrawrollsId(String drawrollsId) {
        this.drawrollsId = drawrollsId;
    }

    public Integer getDrawrollsNumber() {
        return drawrollsNumber;
    }

    public void setDrawrollsNumber(Integer drawrollsNumber) {
        this.drawrollsNumber = drawrollsNumber;
    }

    public Integer getThreeIndex() {
        return threeIndex;
    }

    public void setThreeIndex(Integer threeIndex) {
        this.threeIndex = threeIndex;
    }

    public String getThreeName() {
        return threeName;
    }

    public void setThreeName(String threeName) {
        this.threeName = threeName == null ? null : threeName.trim();
    }

    public Date getMoneyUpdateTime() {
        return moneyUpdateTime;
    }

    public void setMoneyUpdateTime(Date moneyUpdateTime) {
        this.moneyUpdateTime = moneyUpdateTime;
    }

    public Date getDarwTime() {
        return darwTime;
    }

    public void setDarwTime(Date darwTime) {
        this.darwTime = darwTime;
    }

    public Date getAwardTime() {
        return awardTime;
    }

    public void setAwardTime(Date awardTime) {
        this.awardTime = awardTime;
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
}