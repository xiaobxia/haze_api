package com.vxianjin.gringotts.youmi.pojo;


import java.util.List;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */


public class YoumiAgreement {
    /**
     * 订单号
     */
    private String order_no;
    /**
     * 合同所属的页面
     */
    private Integer contract_page;
    /**
     * 合同在页面内的顺序展示
     */
    private List<Integer> contract_pos;

    public YoumiAgreement() {
    }

    public YoumiAgreement(String order_no, Integer contract_page, List<Integer> contract_pos) {
        this.order_no = order_no;
        this.contract_page = contract_page;
        this.contract_pos = contract_pos;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getContract_page() {
        return contract_page;
    }

    public void setContract_page(Integer contract_page) {
        this.contract_page = contract_page;
    }

    public List<Integer> getContract_pos() {
        return contract_pos;
    }

    public void setContract_pos(List<Integer> contract_pos) {
        this.contract_pos = contract_pos;
    }
}

