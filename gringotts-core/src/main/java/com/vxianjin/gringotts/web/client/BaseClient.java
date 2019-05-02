package com.vxianjin.gringotts.web.client;

import com.vxianjin.gringotts.web.dao.ICfcaHttpInfoDao;
import com.vxianjin.gringotts.web.pojo.CfcaHttpInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class BaseClient {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClient.class);

    @Autowired
    private ICfcaHttpInfoDao cfcaHttpDao;

    /**
     * 保存http消息
     *
     * @param jsonValue
     * @param result
     * @param reqType
     * @param userId
     */
    public void saveHttpMessage(String jsonValue, String result, Integer reqType, String userId) {
        try {
            CfcaHttpInfo httpInfo = new CfcaHttpInfo();
            httpInfo.setHttpRequest(jsonValue);
            httpInfo.setHttpResponse(result);
            httpInfo.setReqType(reqType);
            httpInfo.setUserId(userId);
            cfcaHttpDao.insert(httpInfo);
        } catch (Exception e) {
            getLOGGER().error("the user:" + userId + " request reqType " + reqType + " happened error!", e);
        }
    }

    public Logger getLOGGER() {
        return LOGGER;
    }
}
