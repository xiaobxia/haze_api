package com.vxianjin.gringotts.risk.pojo;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiskManageRule {
    /**
     * 基础规则
     */
    public static Integer RULE_BASE = 1;
    /**
     * 扩展规则
     */
    public static Integer RULE_EXTEND = 2;
    /**
     * 起用
     */
    public static Integer STATUS_USE = 1;
    /**
     * 停用
     */
    public static Integer STATUS_STOP = 2;
    /**
     * 规则类型
     */
    public static Map<Integer, String> ALL_TYPE = new HashMap<Integer, String>();
    /**
     * 启停状态
     */
    public static Map<Integer, String> ALL_STATUS = new HashMap<Integer, String>();
    /**
     * 节点类型状态
     */
    public static Map<Integer, String> ALL_ROOT = new HashMap<Integer, String>();
    /**
     * 子节点
     */
    public static Integer ROOT_TYPE_SON = 1;
    /**
     * 决策树根节点(只能有一个)
     */
    public static Integer ROOT_TYPE_ROOT = 2;
    /**
     * 准入原则根节点(只能有一个)
     */
    public static Integer ROOT_TYPE_ROOT_ZR = 3;
    /**
     * 信用额度根节点(只能有一个)
     */
    public static Integer ROOT_TYPE_ROOT_ED = 4;
    /**
     * 普通项
     */
    public static Integer ATTENTION_TYPE_GENERAL = 1;
    /**
     * 复审关注项
     */
    public static Integer ATTENTION_TYPE_REVIEW = 2;
    /**
     * 节点类型状态
     */
    public static Map<Integer, String> ALL_ATTENTION_TYPE = new HashMap<Integer, String>();

    static {
        ALL_TYPE.put(RULE_BASE, "基础规则");
        ALL_TYPE.put(RULE_EXTEND, "扩展规则");
        ALL_STATUS.put(STATUS_USE, "可用");
        ALL_STATUS.put(STATUS_STOP, "停用");
        ALL_ROOT.put(ROOT_TYPE_SON, "子节点");
        ALL_ROOT.put(ROOT_TYPE_ROOT, "决策树根节点");
        ALL_ROOT.put(ROOT_TYPE_ROOT_ZR, "准入原则根节点");
        ALL_ROOT.put(ROOT_TYPE_ROOT_ED, "借款额度根节点");
        ALL_ATTENTION_TYPE.put(ATTENTION_TYPE_GENERAL, "普通项");
        ALL_ATTENTION_TYPE.put(ATTENTION_TYPE_REVIEW, "复审关注项");
    }

    private Integer id;
    private String ruleName;
    private Integer ruleType;
    private String formula;// 计算的对于扩展规则是表达式，对于基础规则是具体的值
    private String formulaShow;
    private Date addTime;
    private Date updateTime;
    private Integer status;
    private String ruleDesc;
    private String ruleTypeView;
    private String statusView;
    private List<RiskManageRule> child;
    private Integer rootType;
    private String parent;
    private Integer attentionType;

    public RiskManageRule() {
        super();
    }

    public Integer getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(Integer attentionType) {
        this.attentionType = attentionType;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Integer getRootType() {
        return rootType;
    }

    public void setRootType(Integer rootType) {
        this.rootType = rootType;
    }

    public List<RiskManageRule> getChild() {
        return child;
    }

    public void setChild(List<RiskManageRule> child) {
        this.child = child;
    }

    public String getRuleTypeView() {
        return ruleTypeView;
    }

    public void setRuleTypeView(String ruleTypeView) {
        this.ruleTypeView = ruleTypeView;
    }

    public String getStatusView() {
        return statusView;
    }

    public void setStatusView(String statusView) {
        this.statusView = statusView;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        setStatusView(ALL_STATUS.get(status));
        this.status = status;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        setRuleTypeView(ALL_TYPE.get(ruleType));
        this.ruleType = ruleType;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        if (StringUtils.isNotBlank(formula)) {
            formula = formula.toLowerCase();
        }
        this.formula = formula;
    }

    public String getFormulaShow() {
        return formulaShow;
    }

    public void setFormulaShow(String formulaShow) {
        this.formulaShow = formulaShow;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RiskManageRule [addTime=" + addTime + ", child=" + child
                + ", formula=" + formula + ", formulaShow=" + formulaShow
                + ", id=" + id + ", parent=" + parent + ", rootType="
                + rootType + ", ruleDesc=" + ruleDesc + ", ruleName="
                + ruleName + ", ruleType=" + ruleType + ", ruleTypeView="
                + ruleTypeView + ", status=" + status + ", statusView="
                + statusView + ", updateTime=" + updateTime + "]";
    }

}
