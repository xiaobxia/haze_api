package com.vxianjin.gringotts.constant;

/**
 * 文件类型枚举类
 *
 * @author tgy
 * @version [版本号, 2018年1月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum FileTypeEnum {
    /**
     * png格式
     */
    PNG("png"),
    /**
     * pdf格式
     */
    PDF("pdf");

    private String value;

    FileTypeEnum(String value) {

        this.value = value;
    }

    /**
     * 根据code查询 Enum
     *
     * @param code
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static FileTypeEnum getEnumFromCode(String code) {
        FileTypeEnum[] enums = FileTypeEnum.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getCode().equals(code)) {
                return enums[i];
            }
        }
        return null;
    }

    public String getCode() {

        return value;
    }
}
