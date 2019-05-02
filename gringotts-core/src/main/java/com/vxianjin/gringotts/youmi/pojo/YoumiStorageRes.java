package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */
public class YoumiStorageRes {

    /**
     * 订单号
     */
    private String order_no;
    /**
     * 存管开户url
     */
    private String storage_url;
    /**
     * 备注信息
     */
    private String remark;


    public YoumiStorageRes() {
    }

    public YoumiStorageRes(String order_no, String storage_url, String remark) {
        this.order_no = order_no;
        this.storage_url = storage_url;
        this.remark = remark;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getStorage_url() {
        return storage_url;
    }

    public void setStorage_url(String storage_url) {
        this.storage_url = storage_url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
