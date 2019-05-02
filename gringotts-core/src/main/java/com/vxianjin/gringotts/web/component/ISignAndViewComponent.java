package com.vxianjin.gringotts.web.component;


import com.vxianjin.gringotts.common.JsonResult;

import java.io.IOException;

/**
 * 云证 合同签署和查看
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

public interface ISignAndViewComponent {

    /**
     * 合同签署
     *
     * @param userId
     * @param contractId
     * @return
     */
    JsonResult signAndViewContract(String userId, String contractId) throws IOException;

}
