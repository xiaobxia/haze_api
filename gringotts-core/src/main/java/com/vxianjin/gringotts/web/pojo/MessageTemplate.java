package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class MessageTemplate implements Serializable {
    private static final long serialVersionUID = 8942091232534815397L;
    private Integer id;

    private String operatorType;

    private String province;

    private String template;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType == null ? null : operatorType.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template == null ? null : template.trim();
    }
}