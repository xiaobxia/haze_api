package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.UserOrder;

import java.util.HashMap;

public interface IUserOrderService {


    /**
     * 插入
     *
     * @param
     * @return
     */
    int insert(UserOrder userOrder);

    /**
     * 删除
     *
     * @return
     */
    int delete(UserOrder userOrder);

    /**
     * 修改
     *
     * @return
     */
    int update(UserOrder userOrder);


    /**
     * 根据主键查询
     *
     * @param id主键
     * @return
     */
    UserOrder findById(Integer id);

    /**
     * 前台查看我的借款列表分页
     *
     * @param params
     * @return
     */
    PageConfig<UserOrder> findPage(HashMap<String, Object> params);

    /**
     * 根据借款信息查询银行卡
     *
     * @param id主键
     * @return
     */
    UserOrder findBankById(Integer id);


}
