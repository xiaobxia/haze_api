package com.vxianjin.gringotts.pay.component.impl;

import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.pay.component.UserInfoComponent;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : chenkai
 * @date : 2018/7/18 17:52
 */
@Component
public class UserInfoComponentImpl implements UserInfoComponent {

    private final static Logger logger = LoggerFactory.getLogger(UserInfoComponentImpl.class);

    @Resource
    private IUserService userService;

    @Override
    public ResultModel<User> userInfoConfirm(String userId) {

        ResultModel resultModel = new ResultModel(false);

        User user = userService.searchByUserid(Integer.parseInt(userId));
        if (null == user) {
            resultModel.setCode(ResponseStatus.LOGIN.getName());
            resultModel.setMessage(ResponseStatus.LOGIN.getValue());
            return resultModel;
        }

        if ((StringUtils.isBlank(user.getRealname()))
                || (!"1".equals(user.getRealnameStatus()))) {
            resultModel.setCode(ResponseStatus.FAILD.getName());
            resultModel.setMessage("请填写个人信息");
            return resultModel;
        }
        logger.info("YeepayBindCardController userBankConfirm userId=" + userId + " 个人信息验证通过");

        resultModel.setSucc(true);
        resultModel.setData(user);
        return resultModel;
    }
}
