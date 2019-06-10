package com.vxianjin.gringotts.web.service;

public interface IMoneyLimitService {

    void dealEd(String userId);

    void dealEd(String userId, String mxRawUrl, String mxReportUrl, String DataHtmlUrl);
     void testRiskRecord();
}
