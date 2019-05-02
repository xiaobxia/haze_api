package com.vxianjin.gringotts.risk.dao;

import com.vxianjin.gringotts.risk.pojo.RiskCreditLog;
import org.springframework.stereotype.Repository;

@Repository
public interface IRiskCreditLogDao {
    int insert(RiskCreditLog riskCreditLog);
}
