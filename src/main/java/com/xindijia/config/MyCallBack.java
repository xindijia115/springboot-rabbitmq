package com.xindijia.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xia
 * @since 2024/1/10/12:21
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /**
     * 交换机确认回调方法(发布确认回调接口)
     * 成功时：
     * 第一个参数表示消息的id以及相关信息
     * 第二个参数表示消息接收成功
     * 第三个参数 成功时为null
     * 失败时：
     * 第一个参数表示消息的id以及相关的信息
     * 第二个参数表示消息接收失败 为false
     * 第三个参数为 消息接收失败的原因
     */


    //需要将我们的MyCallBack注入到方法里面
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //该注解是在所有注入的注解完成之后再执行,一定要注意注入的顺序
    @PostConstruct
    public void init() {//将当前对象注入到方法里面
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String reason) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经成功接收到消息，id为{}", id);
        } else {
            log.info("交换机还没有接收到消息，id为{}, 原因为：{}", id, reason);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        Message message = returnedMessage.getMessage();
        String exchange = returnedMessage.getExchange();
        String replyText = returnedMessage.getReplyText();//错误原因
        String routingKey = returnedMessage.getRoutingKey();
        log.error("消息{},被交换机{}退回,退回原因:{},路由{}", new String(message.getBody()), exchange, replyText, routingKey);
    }

    /**
     * 消息回退接口
     * 消息传递过程中不可达目的地时将消息回退给生产者
     */
}
