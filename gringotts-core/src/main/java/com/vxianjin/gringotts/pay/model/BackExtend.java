package com.vxianjin.gringotts.pay.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 续期配置实体
 * @author fully 2019-05-24
 */
@Data
public class BackExtend implements Serializable {

    private static final long serialVersionUID = 1640307955172050000L;

    private Integer id;

    private String extendName;

    private Integer extendCount;

    private Integer extendMoney;

    private Integer extendStatus;

    private String remark;

    private String extendDay;

}