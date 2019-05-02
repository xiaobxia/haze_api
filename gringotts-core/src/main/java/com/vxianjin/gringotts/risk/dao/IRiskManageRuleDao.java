package com.vxianjin.gringotts.risk.dao;

import com.vxianjin.gringotts.risk.pojo.RiskManageRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRiskManageRuleDao {
    /**
     * 插入
     *
     * @param riskManageRule
     * @return
     */
    int insert(RiskManageRule riskManageRule);

    /**
     * 更新
     *
     * @param riskManageRule
     * @return
     */
    int update(RiskManageRule riskManageRule);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    RiskManageRule findById(Integer id);

    List<RiskManageRule> findAll();

    List<RiskManageRule> findAllByRootType(Integer rootType);

    int updateRootType(RiskManageRule riskManageRule);
}
