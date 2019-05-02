package com.vxianjin.gringotts.web.pojo;

import com.vxianjin.gringotts.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class User implements Serializable {

    public static final String CUSTOMER_OLD = "1";
    public static final String CUSTOMER_NEW = "0";
    public static final Map<String, String> customerTypes = new HashMap<String, String>();
    /***
     * 用户状态
     */
    public static final String USER_STATUS_WHITE = "1";// 正常用户
    public static final String USER_STATUS_BLACK = "2";// 黑名单用户
    public static final String USER_STATUS_THREE = "3";// 被注销用户
    /**
     * 学历
     */
    public static final LinkedHashMap<String, String> EDUCATION_TYPE = new LinkedHashMap<String, String>();
    /**
     * 婚姻状态
     */
    public static final LinkedHashMap<String, String> MARRIAGE_STATUS = new LinkedHashMap<String, String>();
    /**
     * 居住时长
     */
    public static final LinkedHashMap<String, String> RESIDENCE_DATE = new LinkedHashMap<String, String>();
    /**
     * 直系亲属联系人
     */
    public static final LinkedHashMap<String, String> CONTACTS_FAMILY = new LinkedHashMap<String, String>();
    /**
     * 其他联系人
     */
    public static final LinkedHashMap<String, String> CONTACTS_OTHER = new LinkedHashMap<String, String>();
    /**
     * 工作时长
     */
    public static final LinkedHashMap<String, String> WORK_DATE = new LinkedHashMap<String, String>();
    /**
     * 工作类型
     */
    public static final LinkedHashMap<String, String> WORK_TYPE = new LinkedHashMap<String, String>();
    //faceId 错误提示字典匹配
    public static final Map<String, String> FACEID_MSG_TYPE = new HashMap<String, String>();
    /**
     *
     */
    private static final long serialVersionUID = -1155533809738889866L;

    static {
        customerTypes.put(CUSTOMER_OLD, "老用户");
        customerTypes.put(CUSTOMER_NEW, "新用户");
    }

    static {

        EDUCATION_TYPE.put("1", "博士");
        EDUCATION_TYPE.put("2", "硕士");
        EDUCATION_TYPE.put("3", "本科");
        EDUCATION_TYPE.put("4", "大专");
        EDUCATION_TYPE.put("5", "中专");
        EDUCATION_TYPE.put("6", "高中");
        EDUCATION_TYPE.put("7", "初中以下");

        MARRIAGE_STATUS.put("1", "未婚");
        MARRIAGE_STATUS.put("2", "已婚未育");
        MARRIAGE_STATUS.put("3", "已婚已育");
        MARRIAGE_STATUS.put("4", "离异");
        MARRIAGE_STATUS.put("100", "其他");

        RESIDENCE_DATE.put("1", "半年以内");
        RESIDENCE_DATE.put("2", "半年到一年");
        RESIDENCE_DATE.put("3", "一年以上");

        CONTACTS_FAMILY.put("1", "父亲");
        CONTACTS_FAMILY.put("2", "母亲");
        CONTACTS_FAMILY.put("3", "儿子");
        CONTACTS_FAMILY.put("4", "女儿");
        CONTACTS_FAMILY.put("5", "配偶");
        CONTACTS_FAMILY.put("6", "兄弟");
        CONTACTS_FAMILY.put("7", "姐妹");

        CONTACTS_OTHER.put("1", "同学");
        CONTACTS_OTHER.put("2", "亲戚");
        CONTACTS_OTHER.put("3", "同事");
        CONTACTS_OTHER.put("4", "朋友");
        CONTACTS_OTHER.put("5", "其他");

        WORK_DATE.put("1", "一年以内");
        WORK_DATE.put("2", "一到三年");
        WORK_DATE.put("3", "三到五年");
        WORK_DATE.put("4", "五年以上");
        WORK_DATE.put("5", "未知");

        WORK_TYPE.put("1", "上班族");
        WORK_TYPE.put("2", "自由职业");
    }

    static {
        FACEID_MSG_TYPE.put("ID_NUMBER_NAME_NOT_MATCH", "姓名与身份证不匹配");
        FACEID_MSG_TYPE.put("LOW_QUALITY", "人脸图像质量过低");
        FACEID_MSG_TYPE.put("NO_SUCH_ID_NUMBER", "无法识别该身份证号码");
        FACEID_MSG_TYPE.put("INVALID_NAME_FORMAT", "姓名格式错误");
        FACEID_MSG_TYPE.put("INVALID_IDCARD_NUMBER", "身份证号码格式错误");
        FACEID_MSG_TYPE.put("DATA_VALIDATION_ERROR", "图像上传不完整");
        FACEID_MSG_TYPE.put("AUTHORIZATION_ERROR:Limit reached", "当前API的调用量达到上限");
        FACEID_MSG_TYPE.put("AUTHORIZATION_ERROR:Denied", "无此权限，请联系客服");
        FACEID_MSG_TYPE.put("AUTHORIZATION_ERROR:No data source permission", "无此权限，请联系客服");
        FACEID_MSG_TYPE.put("INTERNAL_ERROR", "服务器内部错误，请联系客服");
        FACEID_MSG_TYPE.put("OTHER", "人脸识别实名认证失败！请检查身份证和头像");
    }

    private String id;
    private String userName;
    private String password;
    private String payPassword;
    private String realname;
    private String realnameStatus;
    private int realCount;
    private Date lastFullTime;
    private Date realnameTime;
    private String idNumber;
    private String userSex;
    private int userAge;
    private String userRace;
    //身份证有效期
    private String race;
    private String qq;
    /* 新增的 动态配置推广员  */
    private String apkUrl;
    private String channelTag;
    private String downloadPicUrl;
    private String registerPicUrl;
    private String picCodeNum;
    private String toutiaoConvertId;
    private Long userInfoId;
    private String userPhone;
    private String taobaoAccount;
    private String email;
    private String wechatAccount;
    private String education;
    private String maritalStatus;
    private String presentAddress;
    private String presentAddressDistinct;
    private String presentLatitude;
    private String presentLongitude;
    private String presentPeriod;
    private String companyName;
    private String companyAddress;
    private String companyAddressDistinct;
    private String companyLongitude;
    private String companyLatitude;
    private String companyPhone;
    private String companyPeriod;
    private String firstContactName;
    private String firstContactPhone;
    private String fristContactRelation;
    private String secondContactName;
    private String secondContactPhone;
    private String secondContactRelation;
    private Date createTime;
    private String createIp;
    private Date updateTime;
    private String status;
    private String inviteUserid;
    private String isSave;
    private String headPortrait;
    private String idcardImgZ;
    private String idcardImgF;
    private String customerType;
    private String customerTypeName;
    private String amountMin;
    private String amountMax;
    private String amountAvailable;// 剩余额度
    private String equipmentNumber;// 设备号
    private String zmStatus;// 芝麻认证
    private String zmToken;
    private String jxlStatus;// 聚信认证
    private String userContactSize;
    private String zmScore;
    private String zmScoreTime;
    private String zmIndustyBlack;
    private String zmOver;
    private String zmNoPayOver;
    private String zmIndustyTime;
    private String jxlToken;
    private String jxlTokenTime;
    private String jxlDetail;
    private String newFlag;
    private String jxlDetailTime;
    private String jxlZjDkNum;
    private String jxlBjDkNum;
    private String jxlYjHf;
    private String jxlLink2Days;
    private String jxlLink1Days;
    private String jxlLink2Num;
    private String jxlLink1Num;
    private String jxlLink2Order;
    private String jxlLink1Order;
    private String jxlGjTs;
    private String jxlHtPhoneNum;
    private String jxlAmthNum;
    private String jxlTime;
    private String jxlDetailStatus;
    private String jxlTotal;
    private String jxlGjBl;
    private String jxlLxGjTs;
    private String jxlPhoneRegDays;
    private String jxlRealName;
    private String jxlSamePhoneCard;
    private String historyOverNum;
    private String lastOverDays;
    private String csjy;
    private String userFrom;
    private String amountAddsum;
    private String province;
    // 扩展字段
    private String tgFlag;
    private int clientType;// 客户端类型（1、Android 2、ios 3、wap、4、pc）
    private int BrowerType;// 浏览器类型（1、Android 2、ios 3、pc）
    //同盾运营商相关字段
    private String tdStatus;//同盾认证状态
    private String tdRawData;//同盾运营商数据
    private Date tdVerifyTime;//同盾认证时间
    private Date tdVerifyNextTime;//同盾下一次认证时间
    private String tdTaskId;//同盾任务id
    private String tdFqzData;//同盾反欺诈数据

    private Integer signSwitch;//是否需要签署 0需要 1免签

    /**
     * 工作行业
     */
    private String workIndustry;

    public String getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
        customerTypeName = customerTypes.get(customerType);
    }

    public String getZmToken() {
        return zmToken;
    }

    public void setZmToken(String zmToken) {
        this.zmToken = zmToken;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public int getBrowerType() {
        return BrowerType;
    }

    public void setBrowerType(int browerType) {
        BrowerType = browerType;
    }

    public String getTdFqzData() {
        return tdFqzData;
    }

    public void setTdFqzData(String tdFqzData) {
        this.tdFqzData = tdFqzData;
    }

    public String getTdStatus() {
        return tdStatus;
    }

    public void setTdStatus(String tdStatus) {
        this.tdStatus = tdStatus;
    }

    public String getTdRawData() {
        return tdRawData;
    }

    public void setTdRawData(String tdRawData) {
        this.tdRawData = tdRawData;
    }

    public Date getTdVerifyTime() {
        return tdVerifyTime;
    }

    public void setTdVerifyTime(Date tdVerifyTime) {
        this.tdVerifyTime = tdVerifyTime;
    }

    public Date getTdVerifyNextTime() {
        return tdVerifyNextTime;
    }

    public void setTdVerifyNextTime(Date tdVerifyNextTime) {
        this.tdVerifyNextTime = tdVerifyNextTime;
    }

    public String getTdTaskId() {
        return tdTaskId;
    }

    public void setTdTaskId(String tdTaskId) {
        this.tdTaskId = tdTaskId;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public String getDownloadPicUrl() {
        return downloadPicUrl;
    }

    public void setDownloadPicUrl(String downloadPicUrl) {
        this.downloadPicUrl = downloadPicUrl;
    }

    public String getRegisterPicUrl() {
        return registerPicUrl;
    }

    public void setRegisterPicUrl(String registerPicUrl) {
        this.registerPicUrl = registerPicUrl;
    }

    public String getPicCodeNum() {
        return picCodeNum;
    }

    public void setPicCodeNum(String picCodeNum) {
        this.picCodeNum = picCodeNum;
    }

    public String getToutiaoConvertId() {
        return toutiaoConvertId;
    }

    public void setToutiaoConvertId(String toutiaoConvertId) {
        this.toutiaoConvertId = toutiaoConvertId;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getTgFlag() {
        return tgFlag;
    }

    public void setTgFlag(String tgFlag) {
        this.tgFlag = tgFlag;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAmountAddsum() {
        return amountAddsum;
    }

    public void setAmountAddsum(String amountAddsum) {
        this.amountAddsum = amountAddsum;
    }

    public String getJxlDetailStatus() {
        return jxlDetailStatus;
    }

    public void setJxlDetailStatus(String jxlDetailStatus) {
        this.jxlDetailStatus = jxlDetailStatus;
    }

    public String getJxlTotal() {
        return jxlTotal;
    }

    public void setJxlTotal(String jxlTotal) {
        this.jxlTotal = jxlTotal;
    }

    public String getJxlGjBl() {
        return jxlGjBl;
    }

    public void setJxlGjBl(String jxlGjBl) {
        this.jxlGjBl = jxlGjBl;
    }

    public String getJxlLxGjTs() {
        return jxlLxGjTs;
    }

    public void setJxlLxGjTs(String jxlLxGjTs) {
        this.jxlLxGjTs = jxlLxGjTs;
    }

    public String getJxlPhoneRegDays() {
        return jxlPhoneRegDays;
    }

    public void setJxlPhoneRegDays(String jxlPhoneRegDays) {
        this.jxlPhoneRegDays = jxlPhoneRegDays;
    }

    public String getJxlRealName() {
        return jxlRealName;
    }

    public void setJxlRealName(String jxlRealName) {
        this.jxlRealName = jxlRealName;
    }

    public String getJxlSamePhoneCard() {
        return jxlSamePhoneCard;
    }

    public void setJxlSamePhoneCard(String jxlSamePhoneCard) {
        this.jxlSamePhoneCard = jxlSamePhoneCard;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRealnameStatus() {
        return realnameStatus;
    }

    public void setRealnameStatus(String realnameStatus) {
        this.realnameStatus = realnameStatus;
    }

    public Date getRealnameTime() {
        return realnameTime;
    }

    public void setRealnameTime(Date realnameTime) {
        this.realnameTime = realnameTime;
    }

    public int getRealCount() {
        return realCount;
    }

    public void setRealCount(int realCount) {
        this.realCount = realCount;
    }

    public Date getLastFullTime() {
        return lastFullTime;
    }

    public void setLastFullTime(Date lastFullTime) {
        this.lastFullTime = lastFullTime;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        if (StringUtils.isNotBlank(idNumber) && idNumber.length() > 2 && !idNumber.contains("*")) {
            setProvince(Constant.CARD_PROVINCE.get(idNumber.substring(0, 2)));
            String sex = idNumber.substring(16, 17);
            if (Integer.valueOf(sex) % 2 == 0) {
                sex = "女";
            } else {
                sex = "男";
            }
            setUserSex(sex);
        }
        this.idNumber = idNumber;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserRace() {
        return userRace;
    }

    public void setUserRace(String userRace) {
        this.userRace = userRace;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTaobaoAccount() {
        return taobaoAccount;
    }

    public void setTaobaoAccount(String taobaoAccount) {
        this.taobaoAccount = taobaoAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPresentAddressDistinct() {
        return presentAddressDistinct;
    }

    public void setPresentAddressDistinct(String presentAddressDistinct) {
        this.presentAddressDistinct = presentAddressDistinct;
    }

    public String getPresentLatitude() {
        return presentLatitude;
    }

    public void setPresentLatitude(String presentLatitude) {
        this.presentLatitude = presentLatitude;
    }

    public String getPresentLongitude() {
        return presentLongitude;
    }

    public void setPresentLongitude(String presentLongitude) {
        this.presentLongitude = presentLongitude;
    }

    public String getPresentPeriod() {
        return presentPeriod;
    }

    public void setPresentPeriod(String presentPeriod) {
        this.presentPeriod = presentPeriod;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyAddressDistinct() {
        return companyAddressDistinct;
    }

    public void setCompanyAddressDistinct(String companyAddressDistinct) {
        this.companyAddressDistinct = companyAddressDistinct;
    }

    public String getCompanyLongitude() {
        return companyLongitude;
    }

    public void setCompanyLongitude(String companyLongitude) {
        this.companyLongitude = companyLongitude;
    }

    public String getCompanyLatitude() {
        return companyLatitude;
    }

    public void setCompanyLatitude(String companyLatitude) {
        this.companyLatitude = companyLatitude;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyPeriod() {
        return companyPeriod;
    }

    public void setCompanyPeriod(String companyPeriod) {
        this.companyPeriod = companyPeriod;
    }

    public String getFirstContactName() {
        return firstContactName;
    }

    public void setFirstContactName(String firstContactName) {
        this.firstContactName = firstContactName;
    }

    public String getFirstContactPhone() {
        return firstContactPhone;
    }

    public void setFirstContactPhone(String firstContactPhone) {
        this.firstContactPhone = firstContactPhone;
    }

    public String getFristContactRelation() {
        return fristContactRelation;
    }

    public void setFristContactRelation(String fristContactRelation) {
        this.fristContactRelation = fristContactRelation;
    }

    public String getSecondContactName() {
        return secondContactName;
    }

    public void setSecondContactName(String secondContactName) {
        this.secondContactName = secondContactName;
    }

    public String getSecondContactPhone() {
        return secondContactPhone;
    }

    public void setSecondContactPhone(String secondContactPhone) {
        this.secondContactPhone = secondContactPhone;
    }

    public String getSecondContactRelation() {
        return secondContactRelation;
    }

    public void setSecondContactRelation(String secondContactRelation) {
        this.secondContactRelation = secondContactRelation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInviteUserid() {
        return inviteUserid;
    }

    public void setInviteUserid(String inviteUserid) {
        this.inviteUserid = inviteUserid;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getIdcardImgZ() {
        return idcardImgZ;
    }

    public void setIdcardImgZ(String idcardImgZ) {
        this.idcardImgZ = idcardImgZ;
    }

    public String getIdcardImgF() {
        return idcardImgF;
    }

    public void setIdcardImgF(String idcardImgF) {
        this.idcardImgF = idcardImgF;
    }

    public String getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(String amountMin) {
        this.amountMin = amountMin;
    }

    public String getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(String amountMax) {
        this.amountMax = amountMax;
    }

    public String getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(String amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getZmStatus() {
        return zmStatus;
    }

    public void setZmStatus(String zmStatus) {
        this.zmStatus = zmStatus;
    }

    public String getJxlStatus() {
        return jxlStatus;
    }

    public void setJxlStatus(String jxlStatus) {
        this.jxlStatus = jxlStatus;
    }

    public String getUserContactSize() {
        return userContactSize;
    }

    public void setUserContactSize(String userContactSize) {
        this.userContactSize = userContactSize;
    }

    public String getZmScore() {
        return zmScore;
    }

    public void setZmScore(String zmScore) {
        this.zmScore = zmScore;
    }

    public String getZmScoreTime() {
        return zmScoreTime;
    }

    public void setZmScoreTime(String zmScoreTime) {
        this.zmScoreTime = zmScoreTime;
    }

    public String getZmIndustyBlack() {
        return zmIndustyBlack;
    }

    public void setZmIndustyBlack(String zmIndustyBlack) {
        this.zmIndustyBlack = zmIndustyBlack;
    }

    public String getZmOver() {
        return zmOver;
    }

    public void setZmOver(String zmOver) {
        this.zmOver = zmOver;
    }

    public String getZmNoPayOver() {
        return zmNoPayOver;
    }

    public void setZmNoPayOver(String zmNoPayOver) {
        this.zmNoPayOver = zmNoPayOver;
    }

    public String getZmIndustyTime() {
        return zmIndustyTime;
    }

    public void setZmIndustyTime(String zmIndustyTime) {
        this.zmIndustyTime = zmIndustyTime;
    }

    public String getJxlToken() {
        return jxlToken;
    }

    public void setJxlToken(String jxlToken) {
        this.jxlToken = jxlToken;
    }

    public String getJxlTokenTime() {
        return jxlTokenTime;
    }

    public void setJxlTokenTime(String jxlTokenTime) {
        this.jxlTokenTime = jxlTokenTime;
    }

    public String getJxlDetail() {
        return jxlDetail;
    }

    public void setJxlDetail(String jxlDetail) {
        this.jxlDetail = jxlDetail;
    }

    public String getJxlDetailTime() {
        return jxlDetailTime;
    }

    public void setJxlDetailTime(String jxlDetailTime) {
        this.jxlDetailTime = jxlDetailTime;
    }

    public String getJxlZjDkNum() {
        return jxlZjDkNum;
    }

    public void setJxlZjDkNum(String jxlZjDkNum) {
        this.jxlZjDkNum = jxlZjDkNum;
    }

    public String getJxlBjDkNum() {
        return jxlBjDkNum;
    }

    public void setJxlBjDkNum(String jxlBjDkNum) {
        this.jxlBjDkNum = jxlBjDkNum;
    }

    public String getJxlYjHf() {
        return jxlYjHf;
    }

    public void setJxlYjHf(String jxlYjHf) {
        this.jxlYjHf = jxlYjHf;
    }

    public String getJxlLink2Days() {
        return jxlLink2Days;
    }

    public void setJxlLink2Days(String jxlLink2Days) {
        this.jxlLink2Days = jxlLink2Days;
    }

    public String getJxlLink1Days() {
        return jxlLink1Days;
    }

    public void setJxlLink1Days(String jxlLink1Days) {
        this.jxlLink1Days = jxlLink1Days;
    }

    public String getJxlLink2Num() {
        return jxlLink2Num;
    }

    public void setJxlLink2Num(String jxlLink2Num) {
        this.jxlLink2Num = jxlLink2Num;
    }

    public String getJxlLink1Num() {
        return jxlLink1Num;
    }

    public void setJxlLink1Num(String jxlLink1Num) {
        this.jxlLink1Num = jxlLink1Num;
    }

    public String getJxlLink2Order() {
        return jxlLink2Order;
    }

    public void setJxlLink2Order(String jxlLink2Order) {
        this.jxlLink2Order = jxlLink2Order;
    }

    public String getJxlLink1Order() {
        return jxlLink1Order;
    }

    public void setJxlLink1Order(String jxlLink1Order) {
        this.jxlLink1Order = jxlLink1Order;
    }

    public String getJxlGjTs() {
        return jxlGjTs;
    }

    public void setJxlGjTs(String jxlGjTs) {
        this.jxlGjTs = jxlGjTs;
    }

    public String getJxlHtPhoneNum() {
        return jxlHtPhoneNum;
    }

    public void setJxlHtPhoneNum(String jxlHtPhoneNum) {
        this.jxlHtPhoneNum = jxlHtPhoneNum;
    }

    public String getJxlAmthNum() {
        return jxlAmthNum;
    }

    public void setJxlAmthNum(String jxlAmthNum) {
        this.jxlAmthNum = jxlAmthNum;
    }

    public String getJxlTime() {
        return jxlTime;
    }

    public void setJxlTime(String jxlTime) {
        this.jxlTime = jxlTime;
    }

    public String getHistoryOverNum() {
        return historyOverNum;
    }

    public void setHistoryOverNum(String historyOverNum) {
        this.historyOverNum = historyOverNum;
    }

    public String getLastOverDays() {
        return lastOverDays;
    }

    public void setLastOverDays(String lastOverDays) {
        this.lastOverDays = lastOverDays;
    }

    public String getCsjy() {
        return csjy;
    }

    public void setCsjy(String csjy) {
        this.csjy = csjy;
    }

    public Integer getSignSwitch() {
        return signSwitch;
    }

    public void setSignSwitch(Integer signSwitch) {
        this.signSwitch = signSwitch;
    }

    public String getWorkIndustry() {
        return workIndustry;
    }

    public void setWorkIndustry(String workIndustry) {
        this.workIndustry = workIndustry;
    }

    @Override
    public String toString() {
        return "User [amountAvailable=" + amountAvailable + ", amountMax=" + amountMax + ", amountMin=" + amountMin + ", companyAddress="
                + companyAddress + ", companyAddressDistinct=" + companyAddressDistinct + ", companyLatitude=" + companyLatitude
                + ", companyLongitude=" + companyLongitude + ", companyName=" + companyName + ", companyPeriod=" + companyPeriod + ", companyPhone="
                + companyPhone + ", createIp=" + createIp + ", createTime=" + createTime + ", csjy=" + csjy + ", customerType=" + customerType
                + ", customerTypeName=" + customerTypeName + ", education=" + education + ", email=" + email + ", equipmentNumber=" + equipmentNumber
                + ", firstContactName=" + firstContactName + ", firstContactPhone=" + firstContactPhone + ", fristContactRelation="
                + fristContactRelation + ", headPortrait=" + headPortrait + ", historyOverNum=" + historyOverNum + ", id=" + id + ", idNumber="
                + idNumber + ", idcardImgF=" + idcardImgF + ", idcardImgZ=" + idcardImgZ + ", inviteUserid=" + inviteUserid + ", isSave=" + isSave
                + ", jxlAmthNum=" + jxlAmthNum + ", jxlBjDkNum=" + jxlBjDkNum + ", jxlDetail=" + jxlDetail + ", jxlDetailTime=" + jxlDetailTime
                + ", jxlGjTs=" + jxlGjTs + ", jxlHtPhoneNum=" + jxlHtPhoneNum + ", jxlLink1Days=" + jxlLink1Days + ", jxlLink1Num=" + jxlLink1Num
                + ", jxlLink1Order=" + jxlLink1Order + ", jxlLink2Days=" + jxlLink2Days + ", jxlLink2Num=" + jxlLink2Num + ", jxlLink2Order="
                + jxlLink2Order + ", jxlStatus=" + jxlStatus + ", jxlTime=" + jxlTime + ", jxlToken=" + jxlToken + ", jxlTokenTime=" + jxlTokenTime
                + ", jxlYjHf=" + jxlYjHf + ", jxlZjDkNum=" + jxlZjDkNum + ", lastOverDays=" + lastOverDays + ", maritalStatus=" + maritalStatus
                + ", password=" + password + ", payPassword=" + payPassword + ", presentAddress=" + presentAddress + ", presentAddressDistinct="
                + presentAddressDistinct + ", presentLatitude=" + presentLatitude + ", presentLongitude=" + presentLongitude + ", presentPeriod="
                + presentPeriod + ", qq=" + qq + ", realname=" + realname + ", realnameStatus=" + realnameStatus + ", realnameTime=" + realnameTime
                + ", secondContactName=" + secondContactName + ", secondContactPhone=" + secondContactPhone + ", secondContactRelation="
                + secondContactRelation + ", status=" + status + ", taobaoAccount=" + taobaoAccount + ", updateTime=" + updateTime + ", userAge="
                + userAge + ", userContactSize=" + userContactSize + ", userName=" + userName + ", userPhone=" + userPhone + ", userSex=" + userSex
                + ", wechatAccount=" + wechatAccount + ", zmIndustyBlack=" + zmIndustyBlack + ", zmIndustyTime=" + zmIndustyTime + ", zmNoPayOver="
                + zmNoPayOver + ", zmOver=" + zmOver + ", zmScore=" + zmScore + ", zmScoreTime=" + zmScoreTime + ", zmStatus=" + zmStatus + "]";
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }
}
