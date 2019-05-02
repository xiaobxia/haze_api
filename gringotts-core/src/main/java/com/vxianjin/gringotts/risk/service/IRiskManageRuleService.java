package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.risk.pojo.RiskManageRule;

import java.util.HashMap;
import java.util.List;

public interface IRiskManageRuleService {

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
     * 分页查询
     *
     * @param params ruleName规则名称；<br>
     *               status启停状态<br>
     *               ruleType规则类型<br>
     *               rule_desc描述<br>
     * @return
     */
    PageConfig<RiskManageRule> findPage(HashMap<String, Object> params);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    RiskManageRule findById(Integer id);

    /**
     * 查询全部规则
     *
     * @return
     */
    List<RiskManageRule> findAll();

    /**
     * 根据根节点类型查询根节点
     *
     * @return
     */
    List<RiskManageRule> findAllByRootType(Integer rootType);
}
