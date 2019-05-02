package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

/**
 * 从OSS签移到api中
 *
 * @author dongyukai 2017-09-22
 * @author dongyukai 2017-10-10
 * @version 1.1
 */
public class BackConfigParams implements Serializable {
    /**
     * configParams表中邮箱的sys_type
     */
    public static final String SMTP = "SMTP";
    /**
     * configParams表中短信的sys_type
     */
    public static final String SMS = "SMS";
    /**
     * configParams表中短信验证各种限制的的sys_type
     */
    public static final String SMSCODE = "SMSCODE";
    /**
     * configParams表中网站参数的sys_type
     */
    public static final String WEBSITE = "WEBSITE";
    /**
     * configParams表中关于我们的sys_type
     */
    public static final String CHANNEL = "CHANNEL";
    /**
     * configParams表中芝麻信用的sys_type
     */
    public static final String ZMXY = "ZMXY";
    /**
     * configParams表中聚信立的sys_type
     */
    public static final String JXL = "JXL";
    /**
     * configParams表中服务费账户的sys_type
     */
    public static final String FEE_ACCOUNT = "FEE_ACCOUNT";
    /**
     * configParams表中同盾的sys_type
     */
    public static final String TD = "TD";
    /**
     * configParams表中白骑士的sys_type
     */
    public static final String BQS = "BQS";
    /**
     * configParams表中算话的sys_type
     */
    public static final String SH = "SH";
    /**
     * configParams表中白骑士的sys_type
     */
    public static final String JYZX = "JYZX";
    /**
     * configParams表中爬虫规则的费率
     */
    public static final String SYS_FEE = "SYS_FEE";
    /**
     * configParams表中富友的配置
     */
    public static final String FUYOU = "FUYOU";
    /**
     * configParams表中连连的配置
     */
    public static final String LIANLIAN = "LIANLIAN";
    /**
     * configParams表中银生的配置
     */
    public static final String YINSHENG = "YINSHENG";
    /**
     * configParams表中催收的配置
     */
    public static final String COLLECTION = "COLLECTION";
    /**
     * configParams表中宜信的费率
     */
    public static final String YX = "YX";
    /**
     * configParams表中APP_URL
     */
    public static final String APP_IMG_URL = "APP_IMG_URL";
    /**
     * 二次调用接口需要间隔的时间
     */
    public static final String INTERVAL = "INTERVAL";
    /**
     * 中智诚请求
     */
    public static final String ZZC = "ZZC";
    /**
     * 中智诚风险名单
     */
    public static final String ZZC_BLACK = "ZZC_BLACK";
    /**
     * 系统级，不可页面编辑，仅能后台配置项
     */
    public static final String SYSTEM = "SYSTEM";
    /**
     * 闪银推送的systype
     */
    public static final String SY_TS = "SY_TS";
    /**
     * 魔蝎的sys_type
     */
    public static final String MX = "MX";

    public static final String YSPAY_SUCCESS_CODE = "0000";

    /**
     * 借点钱的sys_type
     */
    public static final String JDQ = "JDQ";
    /**
     * 推广系统与现金侠交互的的sys_type
     */
    public static final String TG_SERVER = "TG_SERVER";
    /**
     * 推广系统与现金侠交互的的绑卡
     */
    public static final String TG_SERVER_BK = "TG_SERVER_BK";
    private static final long serialVersionUID = -3692330885451140092L;

    static {
    }

    private Integer id;
    private String sysName;
    private String sysValue;
    private String sysValueBig;
    private String sysValueView;
    private String sysKey;
    private String sysType;
    private String inputType;
    private String remark;
    private String limitCode;

    public String getSysValueView() {
        return sysValueView;
    }

    public void setSysValueView(String sysValueView) {
        this.sysValueView = sysValueView;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName == null ? null : sysName.trim();
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue == null ? null : sysValue.trim();
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey == null ? null : sysKey.trim();
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType == null ? null : sysType.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLimitCode() {
        return limitCode;
    }

    public void setLimitCode(String limitCode) {
        this.limitCode = limitCode == null ? null : limitCode.trim();
    }

    public String getSysValueBig() {
        return sysValueBig;
    }

    public void setSysValueBig(String sysValueBig) {
        this.sysValueBig = sysValueBig == null ? null : sysValueBig.trim();
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * 自动匹配，不用关心类型 但是InputType必须有值
     *
     * @param value
     */
    public String getSysValueAuto() {
        String sysValueAuto = null;
        if (inputTypeEnum.textarea.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValueBig();
        } else if (inputTypeEnum.text.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValue();
        } else if (inputTypeEnum.password.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValue();
        } else if (inputTypeEnum.image.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValue();
        }
        return sysValueAuto;
    }

    /**
     * 自动匹配，不用关心类型 但是InputType必须有值
     *
     * @param value
     */
    public void setSysValueAuto(String value) {

        if (inputTypeEnum.textarea.name().equals(this.getInputType())) {
            this.setSysValueBig(value);
        } else if (inputTypeEnum.text.name().equals(this.getInputType())) {
            this.setSysValue(value);
        } else if (inputTypeEnum.password.name().equals(this.getInputType())) {
            this.setSysValue(value);
        } else if (inputTypeEnum.image.name().equals(this.getInputType())) {
            this.setSysValue(value);
        }
    }

    @Override
    public String toString() {
        return "BackConfigParams [id=" + id + ", inputType=" + inputType + ", limitCode=" + limitCode + ", remark=" + remark + ", sysKey=" + sysKey
                + ", sysName=" + sysName + ", sysType=" + sysType + ", sysValue=" + sysValue + ", sysValueBig=" + sysValueBig + ", sysValueView="
                + sysValueView + "]";
    }

    public enum inputTypeEnum {
        text, textarea, password, image
    }

}