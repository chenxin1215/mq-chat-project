package com.cx.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * <MQ配置类>
 *
 * @Author: chenxin
 * @Date: 2021/2/23
 */
@Configuration public class RabbitMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    /**
     * 队列名
     */
    public static final String CHAT_QUEUE = "chat:queue";
    /**
     * 交换机
     */
    public static final String CHAT_EXCHANGE = "chat:exchange";

    /**
     * 绑定路由交换机的key
     */
    public static final String CHAT_ROUTING_KEY = "chat:routing:key";

    @Autowired CachingConnectionFactory cachingConnectionFactory;

    @Bean public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //消息发送到broker时的回调
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = Optional.ofNullable(data).map(CorrelationData::getId).orElse(""); //拿到消息的唯一ID
            if (ack) {
                LOGGER.info("{}:消息发送成功", msgId);
            } else {
                LOGGER.info("{}消息发送失败", msgId);
            }
        });

        //消息在broker中从exchange往queue中发送失败的回调
        rabbitTemplate.setReturnsCallback((returnCallback) -> {
            LOGGER.info("### 消息发送失败 msg: {}", returnCallback.getMessage());
        });

        return rabbitTemplate;
    }

    //声明队列
    @Bean(CHAT_QUEUE) public Queue queue() {
        return new Queue(CHAT_QUEUE);
    }

    //声明交换机
    @Bean(CHAT_EXCHANGE) public Exchange exchange() {
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(CHAT_EXCHANGE).durable(true).build();
    }

    //队列绑定交换机，指定routingKey
    @Bean public Binding bindingExchange(@Qualifier(CHAT_QUEUE) Queue queue,
            @Qualifier(CHAT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CHAT_ROUTING_KEY).noargs();
    }

    //    @Bean(name = "primarySimpleMessageListenerContainer")
    //    public SimpleMessageListenerContainer primaryMessageListenerContainer(
    //            @Qualifier("primaryConnectionFactory") ConnectionFactory connectionFactory) {
    //        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    //        container.setDefaultRequeueRejected(false);
    //        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
    //        container.setMessageListener(tailWindService);
    //        container.start();
    //        return container;
    //    }

}
