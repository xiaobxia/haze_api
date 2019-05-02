package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tools.mq.producer.CommonProducer;
import com.vxianjin.gringotts.pay.common.util.RandomUtil;
import com.vxianjin.gringotts.pay.dao.IMqInfoDao;
import com.vxianjin.gringotts.pay.model.MqInfo;
import com.vxianjin.gringotts.pay.service.MqInfoService;
import com.vxianjin.gringotts.web.util.aliyun.RocketMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

/**
 * @author jintian
 * @date 15:45
 */
@Service
public class MqInfoServiceImpl implements MqInfoService {

    private Logger logger = LoggerFactory.getLogger(MqInfoServiceImpl.class);

    @Autowired
    private IMqInfoDao mqInfoDao;

    @Autowired
    private CommonProducer commonProducer;


    @Override
    public boolean save(MqInfo mqInfo) {
        return mqInfoDao.insert(mqInfo) > 0;
    }

    @Override
    public boolean updateMqStatus(String mqKey, boolean status) {
        if (status) {
            return mqInfoDao.updateToSuccess(mqKey) > 0;
        } else {
            return mqInfoDao.updateToError(mqKey) > 0;
        }
    }

    @Override
    public boolean updateMqSendTime(String mqKey) {
        return mqInfoDao.updateMqSendTime(mqKey) > 0;
    }

    @Override
    public boolean sendMq(String topic, String tag, String context) {
        String mqKey = RandomUtil.getUUID();
        JSONObject sendJson = new JSONObject();
        sendJson.put("mqKey", mqKey);
        sendJson.put("context", context);
        MqInfo mqInfo = new MqInfo(mqKey, topic, tag, sendJson.toJSONString());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                try {
                    logger.info("send to cs mq ,topic:" + topic + " tag:" + tag + " context:" + sendJson.toJSONString());
                    RocketMqUtil.sendMqMessage(topic,tag,sendJson.toJSONString());
//                    commonProducer.sendMessage(topic, tag, sendJson.toJSONString());
                    logger.info("send to cs mq end ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return save(mqInfo);
    }

    @Override
    public List<MqInfo> queryUnSuccessMqInfo() {
        return mqInfoDao.queryUnSuccessMqInfo();
    }
}
