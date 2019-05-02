package com.vxianjin.gringotts.common;

/**
 * 定义返回状态
 * 从OSS签移到api中
 *
 * @author dongyukai 2017-9-22
 * @author dongyukai 2017-10-12
 * @version 1.1
 */
public enum ResponseStatus {

    /**
     * 成功
     */
    SUCCESS("0", "成功"),
    /**
     * 失败
     */
    FAILD("-1", "服务器异常，请稍后再试"),
    /**
     * 系统错误
     */
    ERROR("500", "系统错误"),
    /**
     * 未登录
     */
    LOGIN("-2", "登录状态失效"),


    FREQUENT("-3", "请求过于频繁，请稍后再试");

    /**
     * 名称
     */
    private final String name;
    /**
     * 值
     */
    private final String value;

    ResponseStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 获取值
     *
     * @return
     */
    public String getValue() {
        return value;
    }
}
