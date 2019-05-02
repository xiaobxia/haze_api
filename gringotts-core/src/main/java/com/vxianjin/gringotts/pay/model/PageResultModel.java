package com.vxianjin.gringotts.pay.model;

import java.util.List;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 15:15
 * @Description:
 */
public class PageResultModel<T> extends PageResult{

    private boolean succ;

    private String errorCode;

    private String errorMessage;

    private List<T> data;

    public PageResultModel(boolean succ) {
        this.succ = succ;
    }

    public PageResultModel(boolean succ, String errorCode, String errorMessage, List<T> data) {
        this.succ = succ;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
