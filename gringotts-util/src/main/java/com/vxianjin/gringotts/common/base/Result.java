package com.vxianjin.gringotts.common.base;


import java.io.Serializable;

/**
 * 描述:
 * result
 *
 * @author zed
 * @since 2019-01-15 12:38 PM
 */
public class Result<Model> implements Serializable {
    private Long count;
    private int limit = 1;
    private int offset = 0;
    private Model model;
    private boolean success;
    private int msgCode;
    private String msgInfo;

    public Result() {
    }

    public Result(Model model) {
        this.model = model;
        this.success = true;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }



    public Result(int msgCode, String msgInfo) {
        this.success = false;
        this.msgCode = msgCode;
        this.msgInfo = msgInfo;
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public int getMsgCode() {
        return this.msgCode;
    }

    public String getMsgInfo() {
        return this.msgInfo;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Result{count=" + this.count + ", limit=" + this.limit + ", offset=" + this.offset + ", model=" + this.model + ", success=" + this.success + ", msgCode='" + this.msgCode + '\'' + ", msgInfo='" + this.msgInfo + '\'' + '}';
    }
}

