package com.vxianjin.gringotts.pay.model;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 15:17
 * @Description:
 */
public class PageResult {

    private int pageIndex;
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
