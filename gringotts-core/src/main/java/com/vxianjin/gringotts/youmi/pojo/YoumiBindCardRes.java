package com.vxianjin.gringotts.youmi.pojo;
/**
 *@author  Created by Wzw
 *@date  on 2019/1/30 0030 17:51
 */
public class YoumiBindCardRes {
    /**
     * 绑卡结果
     */
    private Integer bind_status;
    /**
     * 第三方用户唯一银行卡ID
     */
    private String bind_id;
    /**
     * 状态文字
     */
    private String remark;

    public YoumiBindCardRes() {
    }

    public YoumiBindCardRes(Integer bind_status, String bind_id, String remark) {
        this.bind_status = bind_status;
        this.bind_id = bind_id;
        this.remark = remark;
    }

    public Integer getBind_status() {
        return bind_status;
    }

    public void setBind_status(Integer bind_status) {
        this.bind_status = bind_status;
    }

    public String getBind_id() {
        return bind_id;
    }

    public void setBind_id(String bind_id) {
        this.bind_id = bind_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
