package com.vxianjin.gringotts.web.pojo;

import java.math.BigDecimal;

/**
 * 云法通合同实体类
 *
 * @author tgy
 * @version [版本号, 2018年2月6日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CfcaContractInfo {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 云法通合同ID
     */
    private String cfcaContractId;
    /**
     * 我方用户ID
     */
    private String userId;
    /**
     * 云法通用户ID
     */
    private String cfcaUserId;
    /**
     * 合同标题
     */
    private String title;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 合同标的金额
     */
    private BigDecimal contractPrice;
    /**
     * 合同签署地
     */
    private String sginRegionName;
    /**
     * 合同管辖地
     */
    private String courtRegionName;
    /**
     * 合同类型
     */
    private String typeName;
    /**
     * 我方生成合同编号
     */
    private String contractNo;
    /**
     * 签署状态 -1为合同不存在 0为未签署 1为正在签署 2为签署成功 4为签署失败 5后台异常签署 6前端异常签署
     */
    private Integer signStatus;
    /**
     * 签署人信息，根据合同模版生成json
     */
    private String signJson;
    /**
     * 合同信息，根据合同模版生成json
     */
    private String contractJson;

    /**
     * 模板信息
     */
    private CfcaContractTemplate template;

    /**
     * 签署URL
     */
    private String url;

    /**
     * 回调URL
     */
    private String linkedUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCfcaContractId() {
        return cfcaContractId;
    }

    public void setCfcaContractId(String cfcaContractId) {
        this.cfcaContractId = cfcaContractId;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public String getSginRegionName() {
        return sginRegionName;
    }

    public void setSginRegionName(String sginRegionName) {
        this.sginRegionName = sginRegionName == null ? null : sginRegionName.trim();
    }

    public String getCourtRegionName() {
        return courtRegionName;
    }

    public void setCourtRegionName(String courtRegionName) {
        this.courtRegionName = courtRegionName == null ? null : courtRegionName.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignJson() {
        return signJson;
    }

    public void setSignJson(String signJson) {
        this.signJson = signJson == null ? null : signJson.trim();
    }

    public String getContractJson() {
        return contractJson;
    }

    public void setContractJson(String contractJson) {
        this.contractJson = contractJson == null ? null : contractJson.trim();
    }

    public CfcaContractTemplate getTemplate() {
        return template;
    }

    public void setTemplate(CfcaContractTemplate template) {
        this.template = template;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkedUrl() {
        return linkedUrl;
    }

    public void setLinkedUrl(String linkedUrl) {
        this.linkedUrl = linkedUrl;
    }
}