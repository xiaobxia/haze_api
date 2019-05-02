package com.vxianjin.gringotts.risk.dao;

import com.vxianjin.gringotts.risk.pojo.RiskRuleCal;
import org.springframework.stereotype.Repository;

@Repository
public interface IRiskRuleCalDao {
    int insert(RiskRuleCal riskRuleCal);
}
