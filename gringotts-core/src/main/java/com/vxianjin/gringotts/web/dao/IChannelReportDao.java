package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.ChannelInfo;
import com.vxianjin.gringotts.web.pojo.ChannelReport;

import java.util.List;
import java.util.Map;

public interface IChannelReportDao {

    /**
     * 根据条件查询
     *
     * @param map 参数名 ： <br>
     *            参数名 ：
     * @return list
     */
    List<ChannelReport> findAll(Map<String, Object> params);

    List<ChannelReport> findPrAll(Map<String, Object> params);

    List<ChannelReport> findChannelId();


    /**
     * 插入对象
     *
     * @param backUser
     */
    void insert(ChannelReport channelReport);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 更新对象
     *
     * @param backUser
     */
    void updateById(ChannelInfo channelInfo);


    int findRegisterCount(Map<String, Object> param);

    int findRealNameCount(Map<String, Object> param);

    int findJXLCount(Map<String, Object> param);

    int findZMCount(Map<String, Object> param);

    int findContactCount(Map<String, Object> param);

    int findCompanyCount(Map<String, Object> param);

    int findBankCount(Map<String, Object> param);

    int findAlipayCount(Map<String, Object> param);

    int findApproveErrorCount(Map<String, Object> param);

    int findBorrowApplyCount(Map<String, Object> param);

    int findBorrowSucCount(Map<String, Object> param);

    int findIntoMoney(Map<String, Object> param);


}
