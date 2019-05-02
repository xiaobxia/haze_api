package com.vxianjin.gringotts.web.service;

/**
 * @author jintian
 * @date 17:53
 */
public interface IUserClientInfoService {

    public boolean saveUserClientInfo(int userId,String clientId);

    public String queryClientIdByUserId(int userId);
}
