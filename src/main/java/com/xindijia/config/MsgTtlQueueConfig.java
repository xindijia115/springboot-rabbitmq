package com.xindijia.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 死信队列的优化,消息设置过期时间
 * @author xia
 * @since 2023/12/29/21:40
 */
@Configuration
public class MsgTtlQueueConfig {
    public static final String QUEUE_C = "QC";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    //声明队列C
    @Bean("queueC")
    public Queue queueC() {
        HashMap<String, Object> map = new HashMap<>();
        //声明当前队列绑定的死信队列的交换机名称
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //死信队列的路由key
        map.put("x-dead-letter-routing-key", "YD");
        //不设置过期时间
        return QueueBuilder.durable(QUEUE_C).withArguments(map).build();
    }

    //队列C与正常的交换机进行绑定
    @Bean
    public Binding queueBindingC(@Qualifier("xExchange")DirectExchange xExchange, @Qualifier("queueC") Queue queueC) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
