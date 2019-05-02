package com.vxianjin.gringotts.web.pojo;

/**
 * 云法通http请求信息实体类
 *
 * @author tgy
 * @version [版本号, 2018年2月6日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CfcaHttpInfo {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 1 身份证正反面 2 用户主体信息 3合同生成请求
     */
    private Integer reqType;
    /**
     * http请求内容
     */
    private String httpRequest;
    /**
     * http响应内容
     */
    private String httpResponse;

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

    public Integer getReqType() {
        return reqType;
    }

    public void setReqType(Integer reqType) {
        this.reqType = reqType;
    }

    public String getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(String httpRequest) {
        this.httpRequest = httpRequest == null ? null : httpRequest.trim();
    }

    public String getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(String httpResponse) {
        this.httpResponse = httpResponse == null ? null : httpResponse.trim();
    }
}
