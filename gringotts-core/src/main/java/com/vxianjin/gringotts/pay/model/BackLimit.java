package com.vxianjin.gringotts.pay.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BackLimit implements Serializable {

    private static final long serialVersionUID = -8505402554760690199L;

    private Integer id;

    private String limit_name;

    private Integer limit_count;

    private Integer limit_product_id;

    private Integer limit_status;

    private String limit_remark;

}