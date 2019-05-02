package com.vxianjin.gringotts.attach.service;

import com.vxianjin.gringotts.attach.pojo.UserInfoTude;
import org.springframework.stereotype.Service;


/**
 * 借款用户坐标信息
 *
 * @author zzc
 */
@Service
public interface IUserInfoTudeService {
    /**
     * 保存借款用户坐标信息
     *
     * @param params
     * @return
     */
    int insertUserInfoTude(UserInfoTude params);
}
