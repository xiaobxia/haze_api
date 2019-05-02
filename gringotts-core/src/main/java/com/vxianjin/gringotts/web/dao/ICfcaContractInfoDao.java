package com.vxianjin.gringotts.web.dao;


import com.vxianjin.gringotts.web.pojo.CfcaContractInfo;
import org.springframework.stereotype.Repository;

/**
 * 云法通合同dao层
 *
 * @author tgy
 * @version [版本号, 2018年2月6日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface ICfcaContractInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(CfcaContractInfo record);

    int insertSelective(CfcaContractInfo record);

    CfcaContractInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CfcaContractInfo record);

    int updateByPrimaryKey(CfcaContractInfo record);

    CfcaContractInfo selectByContractId(String contractId);
}