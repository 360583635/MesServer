package com.job.dispatchService.common;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 庸俗可耐
 * @create 2024-02-14-10:30
 * @description
 */
@Component
@RocketMQMessageListener(topic = "delayTopic",consumerGroup="ORDERSERVICE")
public class MQMsgListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        String msgId = message.getMsgId();
        String msg = new String(message.getBody());
        System.out.println("消息id："+msgId+"消息内容："+msg);
    }
}
