package com.vxianjin.gringotts.youmi.controller;

import com.vxianjin.gringotts.youmi.pojo.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *@author Created by Wzw
 *@date on 2019/1/31 0031 10:41
 */


@RestController
@RequestMapping("api/youmi/order/")
public class YoumiOrderController {

    /**
     * 1.6.1 拉取审批结果接口
     * @param order_no
     * @return
     */
    @PostMapping("query-loan-status")
    public ResultModel<YoumiQueryLoanStatusRes> queryLoanStatus(@RequestParam String order_no){
        YoumiQueryLoanStatusRes youmiQueryLoanStatusRes = new YoumiQueryLoanStatusRes();
        ResultModel<YoumiQueryLoanStatusRes> result = new ResultModel<>();
        result.setData(youmiQueryLoanStatusRes);
        return result;
    }

    /**
     * 1.7 用款确认接口（根据商户需求可配）
     * @param applyLoad
     * @return
     */
    @PostMapping("apply-loan")
    public ResultModel applyLoan(@RequestParam YoumiApplyLoad applyLoad){
        return new ResultModel<>();
    }

    /**
     * 1.8.1 拉取订单状态接口
     * @param order_no
     * @return
     */
    @PostMapping("loan-pay-result")
    public ResultModel<YoumiLoanPayResultRes> loadPayResult(@RequestParam String order_no){
        YoumiLoanPayResultRes youmiLoanPayResultRes = new YoumiLoanPayResultRes();
        ResultModel<YoumiLoanPayResultRes> result = new ResultModel<>();
        result.setData(youmiLoanPayResultRes);
        return result;
    }

    /**
     * 1.9.1 拉取还款计划接口
     * @param order_no
     * @return
     */
//    @PostMapping("loan-repay-plan")
//    public ResultModel<YoumiLoanRepayPlanRes> loanRepayPlan(@RequestParam String order_no){
//        YoumiLoanRepayPlanRes youmiLoanRepayPlanRes = new YoumiLoanRepayPlanRes();
//        ResultModel<YoumiLoanRepayPlanRes> result = new ResultModel<>();
//        result.setData(youmiLoanRepayPlanRes);
//        return result;
//    }

    /**
     * 1.10 还款申请接口
     * @param loanRepay
     * @return
     */
    @PostMapping("loan-repay")
    public ResultModel<YoumiLoanRepayRes> loanRepay(@RequestParam YoumiLoanRepay loanRepay){
        YoumiLoanRepayRes youmiLoanRepayRes = new YoumiLoanRepayRes();
        ResultModel<YoumiLoanRepayRes> result = new ResultModel<>();
        result.setData(youmiLoanRepayRes);
        return result;
    }

    /**
     * 2.1 合同获取接口
     * @param agreement
     * @return
     *
     */
    @PostMapping("agreement")
    public ResultModel<List<YoumiAgreementRes>> agreement(@RequestParam YoumiAgreement agreement){
        ArrayList<YoumiAgreementRes> youmiAgreementRes = new ArrayList<>();
        ResultModel<List<YoumiAgreementRes>> result = new ResultModel<>();
        result.setData(youmiAgreementRes);
        return result;
    }

    /**
     * 2.2 借款试算接口
     * @param trial
     * @return
     */
    @PostMapping("trial")
    public ResultModel<YoumiTrialRes> trial(@RequestParam YoumiTrial trial){
        YoumiTrialRes youmiTrialRes = new YoumiTrialRes();
        ResultModel<YoumiTrialRes> result = new ResultModel<>();
        result.setData(youmiTrialRes);
        return result;
    }

    /**
     * 2.3 获取绑卡信息接口（存管流程需要）
     * @param order_no
     * @return
     */
    @PostMapping("bind-card-list")
    public ResultModel<List<YoumiBindCardListRes>> bindCardList(@RequestParam String order_no){
        ArrayList<YoumiBindCardListRes> youmiBindCardListRes = new ArrayList<>();
        ResultModel<List<YoumiBindCardListRes>> result = new ResultModel<>();
        result.setData(youmiBindCardListRes);
        return result;
    }

    /**
     * 2.4 存管开户接口（存管流程需要）
     * @param storageAccount
     * @return
     */
    @PostMapping("storage-account")
    public ResultModel<YoumiStorageRes> storageAccount(@RequestParam YoumiStorage storageAccount){
        YoumiStorageRes youmiStorageAccountRes = new YoumiStorageRes();
        ResultModel<YoumiStorageRes> result = new ResultModel<>();
        result.setData(youmiStorageAccountRes);
        return result;
    }

    @PostMapping("storage-cash")
    public ResultModel<YoumiStorageRes> storageCash(@RequestParam YoumiStorage storageCash){
        YoumiStorageRes youmiStorageAccountRes = new YoumiStorageRes();
        ResultModel<YoumiStorageRes> result = new ResultModel<>();
        result.setData(youmiStorageAccountRes);
        return result;
    }

}
