package com.vxianjin.gringotts.web.pojo;


/**
 * 用户个人中心借款
 *
 * @param
 * @author 2016年12月9日 17:27:29
 */
public class UserOrder extends BorrowOrder {

    //private String css = '<\font color=\"#B0B0B0\" size=\"3\">';

    /**
     * 新建、待审核
     **/
    public static final Integer STATUS_NEW = 0;
    /**
     * 初审通过
     **/
    public static final Integer STATUS_AUDIT_YES = 1;
    /*** 还款中 */
    public static final Integer STATUS_REPLYING = 11;
    /*** 还款完成 */
    public static final Integer STATUS_REPLY_SUCCESS = 12;
    private UserCardInfo userCardInfo;//创建用户银行卡信息


}
