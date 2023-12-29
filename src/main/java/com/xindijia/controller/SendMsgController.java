package com.xindijia.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 消息生产者代码
 *
 * @author xia
 * @since 2023/12/28/23:16
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //队列设置过期时间
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40S 的队列: " + message);
    }

    //消息设置过期时间
    @GetMapping("/sendMsg/{message}/{ttl}")
    public String sendMsg(@PathVariable("message") String message, @PathVariable("ttl") String ttl) {
        rabbitTemplate.convertAndSend("X", "XC", message, msg ->
        {
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(), ttl, message);
        return "成功发送消息!";
    }

}
