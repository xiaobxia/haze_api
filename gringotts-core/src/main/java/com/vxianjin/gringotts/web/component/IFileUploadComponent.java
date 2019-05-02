package com.vxianjin.gringotts.web.component;

import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;

/**
 * 云证 文件上传接口类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IFileUploadComponent {

    /**
     * 云证文件上传
     *
     * @param filePath
     * @param filaPage
     * @return
     * @throws Exception
     */
    JsonResult fileUpload(CfcaUserInfo cfcauser, String filePath, String filaPage);

    /**
     * 云证文件上传
     *
     * @param userInfo
     * @param filePath
     * @param fileType
     * @param filePage
     * @return
     * @throws Exception
     */
    JsonResult fileUpload(CfcaUserInfo userInfo, String filePath, String fileType, String filePage) throws Exception;

}
