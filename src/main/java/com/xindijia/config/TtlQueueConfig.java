package com.xindijia.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 配置文件类代码,队列设置过期时间
 * @author xia
 * @since 2023/12/28/22:52
 */
@Configuration
public class TtlQueueConfig {
    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QD";

    //声明正常的交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }
    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明正常的队列A
    @Bean("queueA")
    public Queue queueA() {
        HashMap<String, Object> map = new HashMap<>();
        //声明当前队列绑定的死信队列的交换机名称
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //死信队列的路由key
        map.put("x-dead-letter-routing-key", "YD");
        //声明队列的过期时间TTL
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(map).build();
    }
    //交换机与队列A进行绑定
    @Bean
    public Binding queueBindingA(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //声明正常的队列B
    @Bean("queueB")
    public Queue queueB() {
        HashMap<String, Object> map = new HashMap<>();
        //声明当前队列绑定的死信队列的交换机名称
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //死信队列的路由key
        map.put("x-dead-letter-routing-key", "YD");
        //声明队列的过期时间TTL
        map.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(map).build();
    }

    //交换机与队列B进行绑定
    @Bean
    public Binding queueBindingB(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //声明死信队列D
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //死信队列与死信交换机进行绑定
    @Bean
    public Binding queueBindingD(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
