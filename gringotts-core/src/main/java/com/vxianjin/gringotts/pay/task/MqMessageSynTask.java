package com.vxianjin.gringotts.pay.task;

import com.tools.mq.producer.CommonProducer;
import com.vxianjin.gringotts.pay.model.MqInfo;
import com.vxianjin.gringotts.pay.service.MqInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: chenkai
 * @Date: 2018/8/22 10:17
 * @Description: MQ消息同步定时
 */
@Component
public class MqMessageSynTask {

    private final static Logger logger = LoggerFactory.getLogger(MqMessageSynTask.class);

    @Autowired
    private MqInfoService mqInfoService;

    @Autowired
    private CommonProducer commonProducer;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void execute(){
        // 数据库捞取未发送的数据
        logger.info("mq 消息同步定时开始============================>");
        List<MqInfo> mqInfos = mqInfoService.queryUnSuccessMqInfo();

        if (mqInfos == null || mqInfos.size() <= 0){
            logger.info("无未发送的mq消息，同步定时结束============================>");
            return;
        }

        List<FutureTask<Integer>> tasks = new ArrayList<>(mqInfos.size());
        //定时处理
        for (MqInfo mq:mqInfos){
            FutureTask<Integer> task = new FutureTask<>(new MqProcessCallback(mq,commonProducer,mqInfoService));
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
        logger.info(MessageFormat.format("MqMessageSynTask is over,预计同步mq消息{0}条，实际执行成功{1}条",tasks.size(),count));
    }
}

class MqProcessCallback implements Callable<Integer>{

    private final static Logger logger = LoggerFactory.getLogger(MqProcessCallback.class);

    private MqInfo mqInfo;

    private CommonProducer commonProducer;

    private MqInfoService mqInfoService;

    MqProcessCallback(MqInfo mqInfo,CommonProducer commonProducer,MqInfoService mqInfoService){
        this.mqInfo = mqInfo;
        this.commonProducer = commonProducer;
        this.mqInfoService = mqInfoService;
    }

    @Override
    public Integer call(){
        try{
            commonProducer.sendMessage(mqInfo.getMqTopic(), mqInfo.getMqTag(), mqInfo.getMqContext());
            mqInfoService.updateMqSendTime(mqInfo.getMqId());
        }catch (Exception e){
            logger.error(MessageFormat.format("定时消息发送失败，mqId:{0}, mqTopic: {1}, mqTag: {2}",mqInfo.getMqId(),mqInfo.getMqTopic(),mqInfo.getMqTag()));
            return 0;
        }
        return 1;
    }
}
