package com.vxianjin.gringotts.show.pojo;

import java.math.BigDecimal;

public class Province {
    private String province;
    private String provinceCode;
    private Integer personNum;
    private BigDecimal borrowTotal;
    private BigDecimal borrowToday;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getPersonNum() {
        return personNum;
    }

    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    public BigDecimal getBorrowTotal() {
        return borrowTotal;
    }

    public void setBorrowTotal(BigDecimal borrowTotal) {
        this.borrowTotal = borrowTotal;
    }

    public BigDecimal getBorrowToday() {
        return borrowToday;
    }

    public void setBorrowToday(BigDecimal borrowToday) {
        this.borrowToday = borrowToday;
    }

}
