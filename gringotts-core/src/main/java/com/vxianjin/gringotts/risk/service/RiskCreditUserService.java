package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.risk.dao.IRiskCreditUserDao;
import com.vxianjin.gringotts.risk.pojo.RiskRuleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 类描述： 此类的所有更新方法，都会先调用insert方法.insert方法内部逻辑是，先根据userId查询数据库中是否有该记录<br>
 * 如果没有该记录则插入一条仅包含userId、用户名、身份证号码、性别、年龄的数据 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-12-12 下午05:14:12 <br>
 */
@Service
public class RiskCreditUserService implements IRiskCreditUserService {
    @Autowired
    private IRiskCreditUserDao riskCreditUserDao;

    @Override
    public List<RiskRuleProperty> findRuleProperty(
            HashMap<String, Object> params) {
        return riskCreditUserDao.findRuleProperty(params);
    }


}
