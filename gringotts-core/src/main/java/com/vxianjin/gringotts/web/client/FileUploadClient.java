package com.vxianjin.gringotts.web.client;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IYunFaTongApiService;
import com.vxianjin.gringotts.util.JpgThumbnail;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.util.Map;

/**
 * 云证 文件上传接口类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
@Deprecated
public class FileUploadClient extends BaseClient {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadClient.class);
    /**
     * 请求类型
     */
    private static final Integer reqType = 1;

    public String upload(String filePath, String fileType, CfcaUserInfo userInfo) throws Exception {

        String fileVal = JpgThumbnail.getImageBinary(filePath, fileType);

        JSONObject json = new JSONObject();
        json.put("file", fileVal);
        json.put("type", fileType);


        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(json.toJSONString());
        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongApiService.class);
        Response<JSONObject> response = service.fileUpload(headerMap, fileVal, fileType).execute();

        json.put("file", filePath);
        String jsonValue = json.toJSONString();

        if (response.isSuccessful()) {
            if (response.body() != null) {
                saveHttpMessage(jsonValue, response.body().toJSONString(), reqType, userInfo.getUser().getId());
                return response.body().toJSONString();
            } else {
                saveHttpMessage(jsonValue, null, reqType, userInfo.getUser().getId());
            }
        } else {
            saveHttpMessage(jsonValue, null, reqType, userInfo.getUser().getId());
        }
        return null;
    }

    @Override
    public Logger getLOGGER() {
        return LOGGER;
    }
}
