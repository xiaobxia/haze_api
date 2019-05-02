package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPBindCardConfirmReq;
import com.vxianjin.gringotts.pay.service.YPCardService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.web.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: chenkai
 * @Date: 2018/7/25 18:00
 * @Description:
 */
public class BindCardServcieTest extends AbstractTest {

    @Autowired
    private YPCardService ypCardService;

    @Test
    public void bindCardTest(){

        YPBindCardConfirmReq bindCardConfirmReq = new YPBindCardConfirmReq();

        final String orderNo = GenerateNo.nextOrdId();
        bindCardConfirmReq.setRequestNo("11111");
        bindCardConfirmReq.setSmsCode("6120531");
        bindCardConfirmReq.setUserId("203130");
        bindCardConfirmReq.setOrderNo(orderNo);
        bindCardConfirmReq.setPhone("15060646122");
        bindCardConfirmReq.setCardNo("1616050506406060");
        bindCardConfirmReq.setUserName("16161060");

        //【3】用户绑卡
        ResultModel<String> result = ypCardService.userBankConfirm(bindCardConfirmReq);
    }
}
