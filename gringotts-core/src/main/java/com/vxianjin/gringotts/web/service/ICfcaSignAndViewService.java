package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.CfcaContractInfo;

public interface ICfcaSignAndViewService {

    /**
     * 生成支付令url
     *
     * @param userId
     * @param contractInfo
     * @return
     * @throws Exception
     */
    void generatePayToken(String userId, CfcaContractInfo contractInfo) throws Exception;

    /**
     * 进件云法通用户
     *
     * @param userId
     * @return
     * @throws Exception
     */
    int feachCfcaUserModule(String userId) throws Exception;

    /**
     * 云法通合同状态查询
     *
     * @param contractId
     * @return
     * @throws Exception
     */
    int cfcaQueryStatus(String contractId) throws Exception;
}
