package com.vxianjin.gringotts.web.dao;


import com.vxianjin.gringotts.web.pojo.CfcaHttpInfo;
import org.springframework.stereotype.Repository;

/**
 * 云法通http请求信息dao层
 *
 * @author tgy
 * @version [版本号, 2018年2月6日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface ICfcaHttpInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(CfcaHttpInfo record);

    int insertSelective(CfcaHttpInfo record);

    CfcaHttpInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CfcaHttpInfo record);

    int updateByPrimaryKey(CfcaHttpInfo record);
}