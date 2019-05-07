package com.vxianjin.gringotts.web.controller;
import com.vxianjin.gringotts.web.service.IMoneyLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created by lucy on 2019/5/3.
 */
@Controller
@RequestMapping("testController")
public class TestController extends BaseController{
    @Autowired
    private IMoneyLimitService moneyLimitService;

    @RequestMapping("saveUserClientInfo")
    public void testSaveRiskRecord() {
        moneyLimitService.testRiskRecord();
    }
}
