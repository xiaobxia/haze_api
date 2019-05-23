package com.vxianjin.gringotts.pay.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BorrowProductConfig implements Serializable {

    private static final long serialVersionUID = -8505402554760690199L;

    private Integer id;

    private BigDecimal borrowAmount;

    private BigDecimal totalFeeRate;

    private BigDecimal borrowInterest;

    private BigDecimal turstTrial;

    private BigDecimal platformLicensing;

    private BigDecimal collectChannelFee;

    private BigDecimal accountManagerFee;

    private BigDecimal lateFee;

    private BigDecimal renewalFee;

    private BigDecimal renewalPoundage;

    private Integer borrowDay;

    private String dealFlag;

    private Date createTime;

    private Date updateTime;

    private String remark;

    private Integer extendId;

    private Integer limitId;

    private Integer status;

    private String productName;
}