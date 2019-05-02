package com.vxianjin.gringotts.web.component;


import com.vxianjin.gringotts.common.JsonResult;

import java.io.IOException;

/**
 * 云证 获取Token
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ITokenComponent {

    /**
     * 获取token用于签署以及查看合同
     *
     * @param contractId
     * @param userId
     * @return
     */
    JsonResult getToken(String userId, String contractId) throws IOException;

}
