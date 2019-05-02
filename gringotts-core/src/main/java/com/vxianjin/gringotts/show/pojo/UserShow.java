package com.vxianjin.gringotts.show.pojo;

import com.vxianjin.gringotts.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class UserShow {
    private String userId;
    private String imgUrl;// 头像路径
    private String userName;// 用户姓名
    private String age;// 年龄
    private String sex;// 性别
    private BigDecimal borrowMoney;// 借款金额
    private String address;// 用户地址
    private String province;// 用户省份
    private String userPhone;// 用户电话
    private BigDecimal dataPercent;// 资料完整度（用户信息）
    private BigDecimal qzScore;// 反欺诈份数
    private String forbidden;// 禁止项
    private BigDecimal xyScore;// 信用评分
    private String cardNum;
    private String provinceCode;// 省份code
    private Integer status;// 1.审核通过可放款2.拒绝

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        if (status.intValue() == -3 || status.intValue() == -4 || status.intValue() == -5) {
            status = 2;
        } else {
            status = 1;
        }
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        if (StringUtils.isNotBlank(cardNum)) {
            setProvince(Constant.CARD_PROVINCE.get(cardNum.substring(0, 2)));
        }
        this.cardNum = cardNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public BigDecimal getDataPercent() {
        return dataPercent;
    }

    public void setDataPercent(BigDecimal dataPercent) {
        this.dataPercent = dataPercent;
    }

    public BigDecimal getQzScore() {
        return qzScore;
    }

    public void setQzScore(BigDecimal qzScore) {
        this.qzScore = qzScore;
    }

    public String getForbidden() {
        return forbidden;
    }

    public void setForbidden(String forbidden) {
        this.forbidden = forbidden;
    }

    public BigDecimal getXyScore() {
        return xyScore;
    }

    public void setXyScore(BigDecimal xyScore) {
        this.xyScore = xyScore;
    }

}
