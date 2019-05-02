package com.vxianjin.gringotts.base;

import com.vxianjin.gringotts.exception.ResultException;

import java.io.Serializable;

/**
 * @Author: kiro
 * @Date: 2018/7/2
 * @Description:
 */
public class Result<Model> implements Serializable {

    /**
     * 数据条目
     */
    private int limit;
    /**
     * 数据偏移量
     */
    private int offset;
    /**
     * 数据实体
     */
    private Model model;
    /**
     * 消息异常
     */
    private ResultException exception;

    public Result() {
    }

    public Result(Model model) {
        this.model = model;
        this.exception = null;
    }

    public Result(Model model, ResultException exception) {
        this.model = model;
        this.exception = exception;
    }

    public Result(ResultException exception) {
        this.model = null;
        this.exception = exception;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ResultException getException() {
        return exception;
    }

    public void setException(ResultException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "Result{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", model=" + model +
                ", exception=" + exception +
                '}';
    }
}
