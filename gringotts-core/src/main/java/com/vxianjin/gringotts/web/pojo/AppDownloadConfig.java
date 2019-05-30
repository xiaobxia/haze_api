package com.vxianjin.gringotts.web.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppDownloadConfig implements Serializable {

    private static final long serialVersionUID = 451541319853801149L;

    private Integer id;

    private String name;

    private String iosUrl;

    private String androidUrl;

    private String status;

    private String isDelete;

}