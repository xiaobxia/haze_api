package com.vxianjin.gringotts.web.pojo;//

import java.io.Serializable;
import java.math.BigDecimal;

public class RiskCreditUser implements Serializable {
    private static final long serialVersionUID = -8947781196222305570L;
    private Integer id;// 主键ID
    private Integer userId;// 用户主键
    private String userName;// 用户真实姓名
    private String cardNum;// 用户身份证号
    private Integer age;// 年龄
    private Integer sex;// 性别
    private Integer assetId;  //借款申请Id
    private String userPhone;
    private BigDecimal moneyAmount;// 借款金额（元）
    private BigDecimal amountAddsum;// 累计添加金额(元)
    private Integer education;

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public BigDecimal getAmountAddsum() {
        return amountAddsum;
    }

    public void setAmountAddsum(BigDecimal amountAddsum) {
        this.amountAddsum = amountAddsum;
    }

    @Override
    public String toString() {
        return "RiskCreditUser(userId=" + userId + ",userName=" + userName + ",cardNum=" + cardNum + "," +
                "age=" + age + ",sex=" + sex + ",assetId=" + assetId + ",userPhone=" + userPhone + ",moneyAmount=" + moneyAmount + ",amountAddsum=" + amountAddsum + ",education=" + education + ")";
    }
}
