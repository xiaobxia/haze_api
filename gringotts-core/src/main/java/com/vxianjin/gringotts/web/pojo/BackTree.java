package com.vxianjin.gringotts.web.pojo;

/**
 * 类描述：后台树形结构 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-29 下午02:35:02 <br>
 */
public class BackTree {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public int id;// 节点ID
    public int pid;// 父节点ID
    public String name;// 节点名称
    public String file;//
    public String url;//
    public boolean open;// 是否折叠，默认为false
    public boolean isParent;
    public String target;//
    public String title;//

    // 组ID
    public int gid;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
