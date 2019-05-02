package com.vxianjin.gringotts.youmi.pojo;

import java.io.Serializable;

/**
 * 描述:
 * 返回接口封装
 *
 * @author zed
 * @since 2019-01-30 10:23 AM
 */
public class ResultModel<Model> implements Serializable {

    private int code =200;
    private String msg = "success";
    private Model data;

    public ResultModel() {
    }

    public ResultModel(Model data) {

        this.data = data;
    }

    public ResultModel(int code, String msg, Model data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Model getData() {
        return data;
    }

    public void setData(Model data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

