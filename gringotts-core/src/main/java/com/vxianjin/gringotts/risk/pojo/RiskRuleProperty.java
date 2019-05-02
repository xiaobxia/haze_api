package com.vxianjin.gringotts.risk.pojo;

/**
 * 类描述：规则标识号与用户征信表的属性映射 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-12-15 下午01:48:07 <br>
 */
public class RiskRuleProperty {
    private Integer Integer;
    private String rule;// 规则唯一标识，如a11
    private String property;// 用户征信对象的属性名
    private String description;// 描述

    public Integer getInteger() {
        return Integer;
    }

    public void setInteger(Integer integer) {
        Integer = integer;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
