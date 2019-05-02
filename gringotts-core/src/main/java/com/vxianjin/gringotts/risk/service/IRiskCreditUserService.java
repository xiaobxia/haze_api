package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.risk.pojo.RiskRuleProperty;

import java.util.HashMap;
import java.util.List;

public interface IRiskCreditUserService {

    /**
     * 获得所有的规则标识与征信对象的属性对应关系
     *
     * @param params
     * @return
     */
    List<RiskRuleProperty> findRuleProperty(
            HashMap<String, Object> params);

}
