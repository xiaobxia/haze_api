package com.vxianjin.gringotts.pay.dao;

import com.vxianjin.gringotts.web.pojo.RenewalRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IRenewalRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RenewalRecord record);

    int insertSelective(RenewalRecord record);

    RenewalRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RenewalRecord record);

    int updateStatusAndRemark(@Param("recordId") int recordId, @Param("status") int status,@Param("remark") String remark );

    int updateByPrimaryKeyWithBLOBs(RenewalRecord record);

    int updateByPrimaryKey(RenewalRecord record);

    List<RenewalRecord> findParams(Map<String, Object> map);

    RenewalRecord getRenewalRecordByOrderId(String orderId);
}