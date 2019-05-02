package com.vxianjin.gringotts.pay.task;

import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.pojo.RepaymentDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: chenkai
 * @Date: 2018/7/19 9:49
 * @Description: 支付状态同步定时
 *
 * 通过主动查询易宝支付的接口，同步代付或代扣状态，达到一定的补偿效果
 */
@Component
public class PayStatusSynTask {

    private final static Logger logger = LoggerFactory.getLogger(PayStatusSynTask.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private RepaymentDetailService repaymentDetailService;
    @Autowired
    private RepaymentService repaymentService;

    public void execute(){
        //捞取状态还未过渡的订单，捞取T日状态未过渡的订单
        HashMap<String,Object> params = new HashMap<>();
        params.put("begDate",DateUtil.getDateFormat(new Date(),"yyyy-MM-dd 00:00:00"));
        params.put("endDate",DateUtil.getDateFormat(new Date(),"yyyy-MM-dd 23:59:59"));
        params.put("status",0);
        params.put("orderType","YEEPAY");
        List<RepaymentDetail> repaymentDetails =  repaymentDetailService.queryOrderResultForSYN(params);

        if (repaymentDetails == null || repaymentDetails.size() <= 0){
            logger.info("PayStatusSynTask 当前无需要过渡的还款订单,定时任务执行结束======>");
            return;
        }


        List<FutureTask<Integer>> tasks = new ArrayList<>(repaymentDetails.size());

        //定时处理
        for (RepaymentDetail repaymentDetail:repaymentDetails){
             FutureTask<Integer> task = new FutureTask<>(new ProcessCallable(repaymentDetail,repaymentService));
             executorService.submit(task);
             tasks.add(task);
        }

        //统计定时任务执行情况
        int count  = 0;
        for (FutureTask<Integer> taskResult:tasks) {
            try {
                count  += taskResult.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.info(MessageFormat.format("PayStatusSynTask is over,预计同步订单{0}笔，实际执行成功{1}笔",repaymentDetails.size(),count));
    }
}
class ProcessCallable implements Callable<Integer>{

    private final static Logger logger = LoggerFactory.getLogger(ProcessCallable.class);

    private RepaymentDetail repaymentDetail;
    private RepaymentService repaymentService;

    ProcessCallable(RepaymentDetail repaymentDetail,RepaymentService repaymentService){
        this.repaymentDetail = repaymentDetail;
        this.repaymentService = repaymentService;
    }

    @Override
    public Integer call(){
        try{
            repaymentService.synReapymentDetailStatus(repaymentDetail);
        }catch (PayException e){
            logger.error(MessageFormat.format("还款订单 repaymentDetail id={0},定时同步还款状态失败,原因{1}",repaymentDetail.getId(),e.getMessage()));
            return 0;
        }
        return 1;
    }
}
