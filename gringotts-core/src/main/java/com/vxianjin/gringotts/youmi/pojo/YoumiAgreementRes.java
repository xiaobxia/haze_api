package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */


public class YoumiAgreementRes {

    private Integer contract_pos;

    private String contract_url;

    public YoumiAgreementRes() {
    }

    public YoumiAgreementRes(Integer contract_pos, String contract_url) {
        this.contract_pos = contract_pos;
        this.contract_url = contract_url;
    }

    public Integer getContract_pos() {
        return contract_pos;
    }

    public void setContract_pos(Integer contract_pos) {
        this.contract_pos = contract_pos;
    }

    public String getContract_url() {
        return contract_url;
    }

    public void setContract_url(String contract_url) {
        this.contract_url = contract_url;
    }
}
