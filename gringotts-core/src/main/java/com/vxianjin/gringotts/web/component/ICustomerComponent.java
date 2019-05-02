package com.vxianjin.gringotts.web.component;


import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;

import java.io.IOException;

/**
 * 云证-主体(借款人用户)信息录入接口
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ICustomerComponent {

    /**
     * 用户信息录入并向上游返回执行结果
     *
     * @param userInfo
     * @return
     * @throws IOException
     */
    JsonResult registUserCustomer(CfcaUserInfo userInfo) throws IOException;
}
