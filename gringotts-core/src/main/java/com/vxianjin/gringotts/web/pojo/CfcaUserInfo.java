package com.vxianjin.gringotts.web.pojo;

/**
 * 云法通用户信息
 *
 * @author tgy
 * @version [版本号, 2018年2月6日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CfcaUserInfo {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 云法通用户ID
     */
    private String cfcaUserId;
    /**
     * 主体类型 0债权人 1债务人
     */
    private Integer debtType;
    /**
     * 客户类型 0企业 1个人
     */
    private Integer customerType;
    /**
     * 云法通身份证正面文件名
     */
    private String cfcaIdcardImages;
    /**
     * 云法通身份证反面文件名
     */
    private String cfcaIdcardImages2;
    /**
     * 是否需要签署 0需要 1免签
     */
    //private Integer signSwitch;

    //------- 注意：下列属性非本实体，作dto使用,否则为Null---------------

    /**
     * 用户信息
     */
    private User user;

    /**
     * 银行卡信息
     */
    private UserCardInfo cardInfo;

    //------- 注意：上列属性非本实体，作dto使用,否则为Null---------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCfcaUserId() {
        return cfcaUserId;
    }

    public void setCfcaUserId(String cfcaUserId) {
        this.cfcaUserId = cfcaUserId == null ? null : cfcaUserId.trim();
    }

    public Integer getDebtType() {
        return debtType;
    }

    public void setDebtType(Integer debtType) {
        this.debtType = debtType;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getCfcaIdcardImages() {
        return cfcaIdcardImages;
    }

    public void setCfcaIdcardImages(String cfcaIdcardImages) {
        this.cfcaIdcardImages = cfcaIdcardImages == null ? null : cfcaIdcardImages.trim();
    }

    public String getCfcaIdcardImages2() {
        return cfcaIdcardImages2;
    }

    public void setCfcaIdcardImages2(String cfcaIdcardImages2) {
        this.cfcaIdcardImages2 = cfcaIdcardImages2 == null ? null : cfcaIdcardImages2.trim();
    }

//	public Integer getSignSwitch() {
//		return signSwitch;
//	}
//
//	public void setSignSwitch(Integer signSwitch) {
//		this.signSwitch = signSwitch;
//	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(UserCardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
}