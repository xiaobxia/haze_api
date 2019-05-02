package com.vxianjin.gringotts.youmi.controller;

import com.vxianjin.gringotts.youmi.pojo.ResultModel;
import com.vxianjin.gringotts.youmi.pojo.YoumiCheckUser;
import com.vxianjin.gringotts.youmi.pojo.YoumiCheckUserRes;
import com.vxianjin.gringotts.youmi.pojo.YoumiRegister;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 有米管家
 *
 * @author zed
 * @since 2019-01-30 10:07 AM
 */
@RestController
@RequestMapping("api/youmi/user")
public class YoumiController {
    @PostMapping("check-user")
    public ResultModel<YoumiCheckUserRes> checkUser(@RequestBody YoumiCheckUser checkUser){
        YoumiCheckUserRes res = new YoumiCheckUserRes();
        res.setResult(7);
        res.setMax_amount(1000);
        res.setMin_amount(1000);
        int[] terms = {7};
        res.setTerms(terms);
        res.setTerm_type(1);

        ResultModel<YoumiCheckUserRes> result = new ResultModel<>();
        result.setData(res);

        return result;
    }
    @PostMapping("register")
    public ResultModel register(YoumiRegister register){

        return new ResultModel();
    }

}

