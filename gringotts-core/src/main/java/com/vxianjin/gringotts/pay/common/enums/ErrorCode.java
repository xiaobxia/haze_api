package com.vxianjin.gringotts.pay.common.enums;

/**
 * 错误编码，6位(这个应该是全局的，暂时放在pay目录下，后面需要移动或者为单独一个服务)
 * 00开头为系统错误
 * 10开头为代付相关错误
 * 11开头为代扣相关错误
 * <p>
 * 99开头为其他错误
 * 需要的业务错误自己添加
 * Created by jintian on 2018/7/17.
 */
public enum ErrorCode {
    ERROR_100001("100001", "没有找到该笔订单"),
    ERROR_100002("100002", ""),
    ERROR_100003("100003", ""),
    ERROR_100004("100004", ""),
    ERROR_100005("100005", ""),

    ERROR_600001("600001", "请求参数非法"),
    ERROR_0000("0000", "请求成功"),
    ERROR_400("400", "操作异常，请刷新页面重试"),
    ERROR_500("500", "请求异常"),
    ERROR_100006("100006", "");

    private String code;

    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
