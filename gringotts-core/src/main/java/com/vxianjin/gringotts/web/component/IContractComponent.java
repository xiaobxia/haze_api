package com.vxianjin.gringotts.web.component;


import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.web.pojo.CfcaContractInfo;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;

import java.io.IOException;

/**
 * 生成合同接口类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IContractComponent {
    /**
     * 生成合同
     *
     * @param contract
     * @param userInfo
     * @return
     * @throws IOException
     */
    JsonResult createContract(CfcaContractInfo contract, CfcaUserInfo userInfo) throws IOException;
}
