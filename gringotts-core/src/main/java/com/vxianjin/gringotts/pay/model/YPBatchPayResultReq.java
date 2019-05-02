package com.vxianjin.gringotts.pay.model;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 10:33
 * @Description: 打款批次明细查询接口
 */
public class YPBatchPayResultReq extends BaseYeepayReq {

    //商户编号
    private String groupId;

    //本级下级标识
    private String queryMode;

    /**
     * 产品类型，值为 "空" 走代付、代发出款,值为 "RJT" 走日结通道出款
     */
    private String product;

    //打款批次号
    private String batchNo;

    //订单号
    private String orderId;

    //页码
    private String pageNo;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public String getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }
}
