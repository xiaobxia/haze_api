package com.vxianjin.gringotts.pay.component;

import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.web.pojo.User;

/**
 * @author : chenkai
 * @date : 2018/7/18 17:50
 */
public interface UserInfoComponent {
    /**
     * 用户信息确认
     * @param userId userId
     * @return result
     */
    ResultModel<User> userInfoConfirm(String userId);
}
