package com.vxianjin.gringotts.web.mq;


public class SendMQMessageThread implements Runnable {

    private SyncUserProducer syncUserProducer;
    private String content;

    public SendMQMessageThread(SyncUserProducer syncUserProducer, String content) {
        this.syncUserProducer = syncUserProducer;
        this.content = content;
    }

    @Override
    public void run() {
        syncUserProducer.sendMessage(content);
    }
}
