package com.vxianjin.gringotts.web.dao;


import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import org.springframework.stereotype.Repository;

/**
 * 云法通用户信息dao层
 *
 * @author tgy
 * @version [版本号, 2018年2月6日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface ICfcaUserInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(CfcaUserInfo record);

    int insertSelective(CfcaUserInfo record);

    CfcaUserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CfcaUserInfo record);

    int updateByPrimaryKey(CfcaUserInfo record);

    CfcaUserInfo selectCfcaByUserId(String userId);
}