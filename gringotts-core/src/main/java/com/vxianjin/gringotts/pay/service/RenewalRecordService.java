package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.web.pojo.RenewalRecord;

import java.util.List;
import java.util.Map;

/**
 * 从core中提取出来
 * Created by wukun on 2018/1/30
 */
public interface RenewalRecordService {
    public RenewalRecord selectByPrimaryKey(Integer id);

    public List<RenewalRecord> findParams(Map<String, Object> params);

    /**
     * 根据主键删除对象
     *
     * @param id
     */
    public boolean deleteByPrimaryKey(Integer id);

    /**
     * 插入 对象
     *
     * @param renewalRecord
     */
    public boolean insert(RenewalRecord renewalRecord);

    /**
     * 插入 对象
     *
     * @param renewalRecord
     */
    public boolean insertSelective(RenewalRecord renewalRecord);

    /**
     * 更新 象
     *
     * @param renewalRecord
     */
    public boolean updateByPrimaryKey(RenewalRecord renewalRecord);

    /**
     * 更新 象
     *
     * @param renewalRecord
     */
    public boolean updateByPrimaryKeySelective(RenewalRecord renewalRecord);


    public RenewalRecord getRenewalRecordByOrderId(String orderId);

    /**
     * 获取当前borrowId关联的产品线的续期次数=a，再获取当前用户该borrowId对应的repayment下的续期列表个数=b，b<a该项成立
     * @param borrowOrderId
     * @return
     */
    boolean borrowOrderRenewalRecordFlag(Integer borrowOrderId);
}
