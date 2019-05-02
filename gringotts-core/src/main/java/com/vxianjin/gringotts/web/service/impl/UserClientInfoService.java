package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IUserClientInfoDao;
import com.vxianjin.gringotts.web.pojo.UserClientInfo;
import com.vxianjin.gringotts.web.service.IUserClientInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author jintian
 * @date 17:55
 */
@Service
public class UserClientInfoService implements IUserClientInfoService {

    private Logger logger = LoggerFactory.getLogger(UserClientInfoService.class);

    @Autowired
    @Qualifier("userClientInfoDao")
    private IUserClientInfoDao dao;

    @Override
    public boolean saveUserClientInfo(int userId, String clientId) {
        logger.info("UserClientInfoService.saveUserClientInfo params:【userId" + userId + "  clientId:" + clientId + "】");
        if (dao.updateByUserId(userId, clientId) == 0) {
            logger.info("UserClientInfoService.saveUserClientInfo params:【userId" + userId + "  clientId:" + clientId + "】 need insert");
            UserClientInfo userClientInfo = new UserClientInfo();
            userClientInfo.setUserId(userId);
            userClientInfo.setclientId(clientId);
            dao.insertSelective(userClientInfo);
        }
        return true;
    }

    @Override
    public String queryClientIdByUserId(int userId) {
        return dao.queryClientIdByUserId(userId);
    }
}
