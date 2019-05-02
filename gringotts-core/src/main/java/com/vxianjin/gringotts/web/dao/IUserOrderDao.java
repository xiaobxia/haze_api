package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.UserOrder;
import org.springframework.stereotype.Repository;


/**
 * 用户中心借款列表
 * <p>
 * 2016年12月9日 16:21:04
 *
 * @param <T>
 */
@Repository
public interface IUserOrderDao {

    /**
     * 插入
     *
     * @param zbNews
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
     * 获取用户银行卡信息
     *
     * @param id主键
     * @return
     */
    UserOrder findBankById(Integer id);


}
