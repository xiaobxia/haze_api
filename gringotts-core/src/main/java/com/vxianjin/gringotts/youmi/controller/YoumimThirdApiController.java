package com.vxianjin.gringotts.youmi.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.vxianjin.gringotts.youmi.pojo.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.net.idn.Punycode;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */

@RestController
@RequestMapping("v3/third-api/")
public class YoumimThirdApiController {


    @PostMapping("loan-status")
    public ResultModel loanStatus(@RequestParam YoumiLoanStatus loanStatus){
        return new ResultModel();
    }


    @PostMapping("loan-pay-status")
    public ResultModel loanPayStatus(@RequestParam YoumiLoanPayStatus loanPayStatus){
        return new ResultModel();
    }

    @PostMapping("loan-repay-plan")
    public ResultModel loannRepayPlan(@RequestParam YoumiLoanRepayPlan loanRepayPlan){
        return new ResultModel();
    }

    @PostMapping("loan-repay-status")
    public ResultModel loanRepayStatus(@RequestParam YoumiLoanRepayStatus loanRepayStatus){
        return new ResultModel();
    }
}
