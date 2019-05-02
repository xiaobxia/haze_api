package com.vxianjin.gringotts.web.pojo;

import java.util.List;

/**
 * 类描述：系统菜单类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-27 下午06:01:20 <br>
 */
public class BackModule {

    private Integer id;

    private String moduleName;

    private String moduleUrl;
    private String moduleStyle;

    private String moduleDescribe;
    private Integer moduleSequence;
    private Integer moduleView;

    private Integer moduleParentId;

    private List<BackModule> moduleList;

    public List<BackModule> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<BackModule> moduleList) {
        this.moduleList = moduleList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleSequence() {
        return moduleSequence;
    }

    public void setModuleSequence(Integer moduleSequence) {
        this.moduleSequence = moduleSequence;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl == null ? null : moduleUrl.trim();
    }

    public String getModuleDescribe() {
        return moduleDescribe;
    }

    public void setModuleDescribe(String moduleDescribe) {
        this.moduleDescribe = moduleDescribe == null ? null : moduleDescribe
                .trim();
    }

    public String getModuleStyle() {
        return moduleStyle;
    }

    public void setModuleStyle(String moduleStyle) {
        this.moduleStyle = moduleStyle;
    }

    public Integer getModuleView() {
        return moduleView;
    }

    public void setModuleView(Integer moduleView) {
        this.moduleView = moduleView;
    }

    public Integer getModuleParentId() {
        return moduleParentId;
    }

    public void setModuleParentId(Integer moduleParentId) {
        this.moduleParentId = moduleParentId;
    }

    @Override
    public String toString() {
        return "BackModule [id=" + id + ", moduleDescribe=" + moduleDescribe
                + ", moduleList=" + moduleList + ", moduleName=" + moduleName
                + ", moduleParentId=" + moduleParentId + ", moduleSequence="
                + moduleSequence + ", moduleStyle=" + moduleStyle
                + ", moduleUrl=" + moduleUrl + ", moduleView=" + moduleView
                + "]";
    }

}