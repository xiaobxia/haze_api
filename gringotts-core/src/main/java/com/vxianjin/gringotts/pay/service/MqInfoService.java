package com.vxianjin.gringotts.pay.service;

import com.vxianjin.gringotts.pay.model.MqInfo;

import java.util.List;

/**
 * @author jintian
 * @date 15:45
 */
public interface MqInfoService {

    /**
     * 保存mq推送的消息
     *
     * @param mqInfo
     * @return
     */
    public boolean save(MqInfo mqInfo);

    /**
     * 修改mq处理状态
     *
     * @param mqKey  mq的key
     * @param status 是否成功状态
     * @return
     */
    public boolean updateMqStatus(String mqKey, boolean status);


    /**
     * 修改mq发送时间
     *
     * @param mqKey  mq的key
     * @return
     */
    public boolean updateMqSendTime(String mqKey);

    /**
     * 发送mq消息
     *
     * @param topic   mq主题
     * @param tag     mq标题
     * @param context 推送内容
     * @return
     */
    public boolean sendMq(String topic, String tag, String context);

    /**
     * 获取未成功回调的mq消息
     *
     * @return
     */
    public List<MqInfo> queryUnSuccessMqInfo();



}
