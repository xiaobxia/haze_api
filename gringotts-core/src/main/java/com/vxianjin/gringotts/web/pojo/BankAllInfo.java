package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡
 *
 * @author user
 */
public class BankAllInfo implements Serializable {


    public static final String BANK_STATUS_NO = "0";
    private static final long serialVersionUID = -6154020205761650332L;
    private Integer bankId;
    private String bankName;
    private Integer bankStatus;
    private Date bankUpdatetime;
    private String bankCode;
    private Integer bankSequence;
    private String bankLogoImg1;
    private String bankLogoImg2;
    private String openBank;

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(Integer bankStatus) {
        this.bankStatus = bankStatus;
    }

    public Date getBankUpdatetime() {
        return bankUpdatetime;
    }

    public void setBankUpdatetime(Date bankUpdatetime) {
        this.bankUpdatetime = bankUpdatetime;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getBankSequence() {
        return bankSequence;
    }

    public void setBankSequence(Integer bankSequence) {
        this.bankSequence = bankSequence;
    }

    public String getBankLogoImg1() {
        return bankLogoImg1;
    }

    public void setBankLogoImg1(String bankLogoImg1) {
        this.bankLogoImg1 = bankLogoImg1;
    }

    public String getBankLogoImg2() {
        return bankLogoImg2;
    }

    public void setBankLogoImg2(String bankLogoImg2) {
        this.bankLogoImg2 = bankLogoImg2;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }
}
