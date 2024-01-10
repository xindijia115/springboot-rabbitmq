package com.xindijia.controller;

import com.xindijia.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发布确认高级 生产者
 *
 * @author xia
 * @since 2024/1/10/11:40
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY + "123", message, correlationData);
        log.info("发送消息内容:{}", message);
    }
}
