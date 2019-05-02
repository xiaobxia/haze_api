package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class InfoImage implements Serializable {
    private static final long serialVersionUID = -2453979257775033717L;
    private Integer id;
    private String url;
    private String title;
    private String reurl;
    private String sort;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getReurl() {
        return reurl;
    }

    public void setReurl(String reurl) {
        this.reurl = reurl;
    }

    @Override
    public String toString() {
        return "InfoImage [id=" + id + ", reurl=" + reurl + ", sort=" + sort
                + ", status=" + status + ", title=" + title + ", url=" + url
                + "]";
    }


}
