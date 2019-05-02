package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */

public class YoumiStorage {
    /**
     * 订单号
     */
    private String order_no;

    /**
     * 回传url
     */
    private String back_url;

    public YoumiStorage() {
    }

    public YoumiStorage(String order_no, String back_url) {
        this.order_no = order_no;
        this.back_url = back_url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getBack_url() {
        return back_url;
    }

    public void setBack_url(String back_url) {
        this.back_url = back_url;
    }
}
