package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

/**
 * 用户认证列表
 *
 * @author user
 */
public class UserCertification implements Serializable {
    private static final long serialVersionUID = -9062052612169307947L;
    private Integer id;
    private String title;
    private Integer sequence;
    private String logoImg;
    private Integer mustBe;
    private String url;
    private String code;
    private String describe;
    private String tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public Integer getMustBe() {
        return mustBe;
    }

    public void setMustBe(Integer mustBe) {
        this.mustBe = mustBe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
