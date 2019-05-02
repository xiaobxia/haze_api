package com.vxianjin.gringotts.youmi.pojo;

/**
 *@author  Created by Wzw
 *@date  on 2019/1/30 0030 17:51
 */

public class YoumiValidBankListRes {
    /**
     * 银行编码（按照下面枚举列表传输）
     */
    private String bank_code;

    /**
     * 银行说明
     */
    private String bank_title;

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_title() {
        return bank_title;
    }

    public void setBank_title(String bank_title) {
        this.bank_title = bank_title;
    }

    public YoumiValidBankListRes(String bank_code, String bank_title) {
        this.bank_code = bank_code;
        this.bank_title = bank_title;
    }

    public YoumiValidBankListRes() {
    }
}
