package com.vxianjin.gringotts.pay.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提额配置实体
 * @author fully 2019-05-23
 */
@Data
public class BackLimit implements Serializable {

    private static final long serialVersionUID = -8505402554760690199L;

    private Integer id;

    private String limitName;

    private Integer limitCount;

    private Integer limitProductId;

    private Integer limitStatus;

    private String limitRemark;

}