package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.risk.pojo.RiskCreditUser;
import com.vxianjin.gringotts.risk.pojo.RiskRuleCal;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IRiskCreditUsrDao {

    RiskCreditUser findByAssetsId(String assetsId);

    List<RiskRuleCal> findRiskRuleCalByAssetsId(String assetsId);

}
