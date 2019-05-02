package com.vxianjin.gringotts.web.pojo;

/**
 * 类描述：角色菜单关联类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 上午10:21:48 <br>
 */
public class BackRoleModule {

    private Integer id;

    private Integer moduleId;

    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "RoleModule [id=" + id + ", moduleId=" + moduleId + ", roleId="
                + roleId + "]";
    }

}