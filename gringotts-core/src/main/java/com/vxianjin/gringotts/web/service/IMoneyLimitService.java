package com.vxianjin.gringotts.web.service;

public interface IMoneyLimitService {

    void dealEd(String userId);

    void dealEd(String userId, String gxbToken);
     void testRiskRecord();
}
