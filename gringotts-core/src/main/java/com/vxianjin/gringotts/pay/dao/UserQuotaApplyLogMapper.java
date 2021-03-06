package com.vxianjin.gringotts.pay.dao;

import com.vxianjin.gringotts.pay.model.UserQuotaApplyLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserQuotaApplyLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_quota_apply_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_quota_apply_log
     *
     * @mbggenerated
     */
    int insert(UserQuotaApplyLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_quota_apply_log
     *
     * @mbggenerated
     */
    int insertSelective(UserQuotaApplyLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_quota_apply_log
     *
     * @mbggenerated
     */
    UserQuotaApplyLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_quota_apply_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserQuotaApplyLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_quota_apply_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserQuotaApplyLog record);


    List<UserQuotaApplyLog> queryFail();

    int updateToSended(@Param("id") Long id,@Param("errorNum") int errorNum);


    int updateToSuccess(long id);

    List<UserQuotaApplyLog> queryTodayFail();

    int updateToFail(long id);

    int updateToNoUpdate(long id);
}