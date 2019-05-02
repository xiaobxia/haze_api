package com.vxianjin.gringotts.constant;

/**
 * 我方支持银行卡列表枚举类
 *
 * @author tgy
 * @version [版本号, 2018年4月26日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum QbmBankEnums {

   /* ICBC("1","中国工商银行"),
    CMB("2","招商银行"),
    BOC("3","中国银行"),
    ABC("4","中国农业银行"),
    CCB("5","中国建设银行"),
    BOCO("6","交通银行"),
    PSBC("7","中国邮政"),
    CIB("8","兴业银行"),
    SPDB("9","浦东发展银行"),
    CEB("10","中国光大银行"),
    GDB("11","广东发展银行"),
    SZPA("12","平安银行"),
    CTSH("13","花旗银行"),
    EBCL("14","恒丰银行"),
    CMBC("15","中国民生银行"),
    HX("16","华夏银行"),
    ECITIC("17","中信银行"),
    BCCB("18","北京银行"),
    JSBC("19","江苏银行");*/

    /**
     * 中国工商银行
     */
    ICBC("1", "ICBC"),
    CMB("2", "CMBCHINA"),
    BOC("3", "BOC"),
    ABC("4", "ABC"),
    CCB("5", "CCB"),
    BOCO("6", "BOCO"),
    PSBC("7", "PSBC"),
    CIB("8", "CIB"),
    SPDB("9", "SPDB"),
    CEB("10", "CEB"),
    GDB("11", "GDB"),
    SZPA("12", "SZPA"),
    CTSH("13", "CTSH"),
    HFB("14", "HFB"),
    CMBC("15", "CMBC"),
    HX("16", "HX"),
    ECITIC("17", "ECITIC"),
    BCCB("18", "BCCB"),
    JSBC("19", "JSBC");

    private String code;
    private String value;

    QbmBankEnums(String code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 根据code查询 Enum
     *
     * @param code
     * @return
     */
    public static QbmBankEnums getEnumFromCode(String code) {
        QbmBankEnums[] enums = QbmBankEnums.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getCode().equals(code)) {
                return enums[i];
            }
        }
        return null;
    }

    /**
     * 根据code查询 Enum
     *
     * @param value
     * @return
     */
    public static QbmBankEnums getEnumFromValue(String value) {
        QbmBankEnums[] enums = QbmBankEnums.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getValue().equals(value)) {
                return enums[i];
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
