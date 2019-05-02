package com.vxianjin.gringotts.web.mq;

import com.aliyun.openservices.ons.api.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

@Component
public class SyncUserProducer {

    private static Logger logger = Logger.getLogger(SyncUserProducer.class);

    private String producerId;
    private String accessKeyID;
    private String accessKeySecret;
    private String addr;
    private String sysName;
    private Producer producer;

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public void setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public void start() {
        try {
            Properties properties = new Properties();
            //在控制台创建的 Producer ID
            properties.put(PropertyKeyConst.ProducerId, producerId);
            // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
            properties.put(PropertyKeyConst.AccessKey, accessKeyID);
            // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
            properties.put(PropertyKeyConst.SecretKey, accessKeySecret);
            //设置发送超时时间，单位毫秒
            properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
            // 设置 TCP 接入域名（此处以公共云生产环境为例）
            properties.put(PropertyKeyConst.ONSAddr, addr);
            producer = ONSFactory.createProducer(properties);
            // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
            producer.start();
        } catch (Exception e) {
            logger.error(" ali mq start failed");
        }
    }

    public void sendMqMessage(String topic, String target, String message) {
        Message msg = new Message(topic, target, message.getBytes());

        logger.info("Send mq message . Topic is :" + message + " target is :" + target + "  message is :" + message);
        try {
            SendResult sendResult = producer.send(msg);
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
                logger.info(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId() + ",content is:" + message);
            }
        } catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            logger.error(new Date() + " Send mq message failed. Topic is:" + msg.getTopic() + " content is " + message, e);
        }
    }

    public synchronized void sendMessage(String content) {
        sendMqMessage("jx-money_user_sync", sysName, content);
    }

    public void destory() {
        if (producer != null) {
            producer.shutdown();
        }
    }
}
