package com.xindijia.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 使用插件实现延迟队列
 *
 * @author xia
 * @since 2023/12/30/20:12
 */
@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean("delayedQueue")
    public Queue delayQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //自定义一个延迟交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        HashMap<String, Object> map = new HashMap<>();
        //定义交换机的类型
        map.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, map);
    }

    //绑定
//    @Bean
//    public Binding queueBinding(@Qualifier("delayedQueue") Queue queue, @Qualifier("delayedExchange") CustomExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
//    }

}
