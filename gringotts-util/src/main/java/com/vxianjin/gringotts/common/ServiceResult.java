package com.vxianjin.gringotts.common;

/**
 * 类描述：从OSS签移到api中
 * 创建人：董玉凯
 * 创建时间：2017年9月22日
 */
public class ServiceResult {

    public static String SUCCESS = "200";
    /**
     * 返回代码
     */
    private String code = SUCCESS;// 默认成功

    /*
     * 提示信息
     */
    private String msg = "操作成功"; // 默认提示语

    private Object ext = null;

    public ServiceResult(String code) {
        this.code = code;
    }

    public ServiceResult() {
    }

    public ServiceResult(ServiceResult serviceResult) {
        this.code = serviceResult.getCode();
        this.msg = serviceResult.getMsg();
    }

    public ServiceResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServiceResult(String code, String msg, Object ext) {
        this.code = code;
        this.msg = msg;
        this.ext = ext;
    }

    public boolean isSuccessed() {
        return SUCCESS.equals(getCode());
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

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "ServiceResult [code=" + code + ", ext=" + ext + ", msg=" + msg + "]";
    }

}
