package com.vxianjin.gringotts.common;

import java.util.Map;

/**
 * 从OSS签移到api中
 *
 * @author dongyukai 2017-09-22
 * @version 1.0
 */
public class ResponseContent {

    public static String SUCCESS = "0";
    /**
     * 返回代码
     */
    private String code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 扩展信息
     */
    private Object ext;

    private Map<String, String> paramsMap;

    public ResponseContent(ServiceResult serviceResult) {
        this.code = serviceResult.getCode();
        this.msg = serviceResult.getMsg();
        this.ext = serviceResult.getMsg();
    }

    public ResponseContent(String code) {
        this.code = code;
    }

    public ResponseContent(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseContent() {
    }

    public ResponseContent(String code, String msg, Object ext) {
        this.code = code;
        this.msg = msg;
        this.ext = ext;
    }

    public boolean isSuccessed() {
        return getCode().equals(SUCCESS);
    }

    public boolean isFail() {
        return !SUCCESS.equals(getCode());
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

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object data) {
        this.ext = data;
    }
}
