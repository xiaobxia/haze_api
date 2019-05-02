package com.vxianjin.gringotts.web.pojo.risk;

/**
 * 描述:
 * 用户风控信息
 *
 * @author zed
 * @since 2019-01-31 8:38 PM
 */
public class UserRiskInfo {
    /**
     * 首字母小写+id
     */
    private String consumerNo;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 邮箱
     */
    private String email;
    /**
     * qq号
     */
    private String qq;
    /**
     * 现居住地址
     */
    private String address;
    /**
     * 渠道ID
     */
    private Integer channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 是否为新用户 0 新  1 老
     */
    private Integer userType;
    /**
     * 行业code
     */
    private String tradeCode;
    /**
     * 行业名称
     */
    private String tradeName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司详细地址
     */
    private String companyAddrDetail;
    /**
     * 性别 1男0女
     */
    private Integer gender;
    /**
     * 省份
     */
    private String province;

    public UserRiskInfo() {
    }

    public UserRiskInfo(String consumerNo, String realName, String phone, Integer age, String idNo, String email, String qq, String address, Integer channelId, String channelName, Integer userType, String tradeCode, String tradeName, String companyName, String companyAddrDetail, Integer gender, String province) {
        this.consumerNo = consumerNo;
        this.realName = realName;
        this.phone = phone;
        this.age = age;
        this.idNo = idNo;
        this.email = email;
        this.qq = qq;
        this.address = address;
        this.channelId = channelId;
        this.channelName = channelName;
        this.userType = userType;
        this.tradeCode = tradeCode;
        this.tradeName = tradeName;
        this.companyName = companyName;
        this.companyAddrDetail = companyAddrDetail;
        this.gender = gender;
        this.province = province;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddrDetail() {
        return companyAddrDetail;
    }

    public void setCompanyAddrDetail(String companyAddrDetail) {
        this.companyAddrDetail = companyAddrDetail;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}

