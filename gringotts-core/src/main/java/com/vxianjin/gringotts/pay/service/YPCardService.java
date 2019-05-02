package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPBindCardConfirmReq;

/**
 * @author : chenkai
 * @date : 2018/7/18 18:27
 */
public interface YPCardService {
    /**
     * 绑卡确认
     * @param bindCardConfirmReq req
     * @return result
     */
    ResultModel<String> userBankConfirm(YPBindCardConfirmReq bindCardConfirmReq);
}
