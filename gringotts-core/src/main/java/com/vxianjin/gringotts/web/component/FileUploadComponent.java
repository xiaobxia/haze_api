package com.vxianjin.gringotts.web.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.client.FileUploadClient;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 云证 文件上传接口类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component(value = "fileUploadCnt")
public class FileUploadComponent implements IFileUploadComponent {
    /**
     * 日志打印
     */
    private static Logger LOGGER = LoggerFactory.getLogger(FileUploadComponent.class);

    @Autowired
    private FileUploadClient client;

    @Override
    public JsonResult fileUpload(CfcaUserInfo cfcauser, String filePath, String filaPage) {
        return null;
    }


    /**
     * 云证文件上传
     *
     * @param filePath
     * @param fileType
     * @return
     * @throws Exception
     */
    @Override
    public JsonResult fileUpload(CfcaUserInfo userInfo, String filePath, String fileType, String filePage) throws Exception {

        String result = client.upload(filePath, fileType, userInfo);
        LOGGER.info(result);
        JsonResult ret = dealCfcaResult(result);

        //如果结果成功 直接設置fileName
        if (CfcaCommonUtil.CODE_SUCCESS.equals(ret.getCode())) {
            String fileName = ret.getData().toString();
            String idCardFileName = JSON.parseObject(fileName).getString("fileName");

            //设置CFCA云证文件名 正面
            if ("1".equals(filePage)) {
                userInfo.setCfcaIdcardImages(idCardFileName);
            }
            //设置CFCA云证文件名 反面
            if ("2".equals(filePage)) {
                userInfo.setCfcaIdcardImages2(idCardFileName);
            }

        }
        //重设data
        ret.setData(userInfo);

        return ret;
    }

    /**
     * 处理云证系统返回的json字符串
     *
     * @param resultMsg
     * @return
     */
    private JsonResult dealCfcaResult(String resultMsg) {
        JsonResult ret = new JsonResult();
        ret.setCount("1");
        ret.setData(resultMsg);
        //云证出错可能会返回一个网页
        if (!StringUtils.isEmpty(resultMsg)) {
            if (resultMsg.contains("<html")) {
                ret.setMsg("CFCA DEAL ERROR,AND RETURN HTML PAGE!");
                ret.setResult(CfcaCommonUtil.RET_FAILURE);
                ret.setCode(CfcaCommonUtil.CODE_FAILURE);
                ret.setData(resultMsg);
                return ret;
            }
        }

        //当是json串处理成json
        JSONObject jsonCfca = JSON.parseObject(resultMsg);

        //返回結果為空或不存在code
        if (jsonCfca == null || StringUtils.isEmpty(jsonCfca.getString("code"))) {
            LOGGER.error("Upload file failed , please check network!");
            ret.setMsg("Upload file failed , please check network!");
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
            return ret;
        }
        String code = jsonCfca.getString("code");
        String msg = jsonCfca.getString("msg");
        //成功
        if ("0".equals(code)) {
            ret.setMsg("UPLOAD FILE SUCCESS!");
            ret.setResult(CfcaCommonUtil.RET_SUCCESS);
            ret.setCode(CfcaCommonUtil.CODE_SUCCESS);
            String data = jsonCfca.getString("data");
            ret.setData(data);
        } else {
            ret.setMsg(msg);
            ret.setResult(CfcaCommonUtil.RET_FAILURE);
            ret.setCode(CfcaCommonUtil.CODE_FAILURE);
        }
        return ret;
    }
}
