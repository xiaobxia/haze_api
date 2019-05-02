package com.vxianjin.gringotts.web.pojo;

/**
 * 类描述：用户角色关联类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 上午10:23:33 <br>
 */
public class BackUserRole {

    private Integer id;

    private Integer roleId;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}