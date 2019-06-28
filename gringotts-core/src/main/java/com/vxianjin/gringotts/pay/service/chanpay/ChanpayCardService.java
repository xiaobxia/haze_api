package com.vxianjin.gringotts.pay.service.chanpay;

import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPBindCardConfirmReq;

/**
 * @author : fully
 * @date : 2019/6/27 12:27
 */
public interface ChanpayCardService {
    /**
     * 绑卡确认
     * @param bindCardConfirmReq req
     * @return result
     */
    ResultModel<String> userBankConfirm(YPBindCardConfirmReq bindCardConfirmReq);
}
