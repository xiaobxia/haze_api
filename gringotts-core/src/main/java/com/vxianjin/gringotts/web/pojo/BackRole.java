package com.vxianjin.gringotts.web.pojo;

import java.util.Date;

/**
 * 类描述： <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 上午10:04:55 <br>
 */
public class BackRole {
    private Integer id;

    private String roleName;

    private String roleSummary;

    private Integer roleSuper;

    private Date roleAddtime;

    private String roleAddip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleSummary() {
        return roleSummary;
    }

    public void setRoleSummary(String roleSummary) {
        this.roleSummary = roleSummary == null ? null : roleSummary.trim();
    }

    public Integer getRoleSuper() {
        return roleSuper;
    }

    public void setRoleSuper(Integer roleSuper) {
        this.roleSuper = roleSuper;
    }

    public Date getRoleAddtime() {
        return roleAddtime;
    }

    public void setRoleAddtime(Date roleAddtime) {
        this.roleAddtime = roleAddtime;
    }

    public String getRoleAddip() {
        return roleAddip;
    }

    public void setRoleAddip(String roleAddip) {
        this.roleAddip = roleAddip == null ? null : roleAddip.trim();
    }

    @Override
    public String toString() {
        return "BackRole [id=" + id + ", roleAddip=" + roleAddip
                + ", roleAddtime=" + roleAddtime + ", roleName=" + roleName
                + ", roleSummary=" + roleSummary + ", roleSuper=" + roleSuper
                + "]";
    }

}