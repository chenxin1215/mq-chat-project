package com.cx.chat.service;

import com.cx.chat.config.RabbitMqConfig;
import com.cx.chat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <创建MQ消息>
 *
 * @Author: chenxin
 * @Date: 2021/3/5
 */
@Service public class SendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendService.class);

    @Autowired private RabbitTemplate amqpTemplate;

    public void sendMeg(ChatMessage meg) {
        meg.setSendTime(new Date());
        LOGGER.info("service meg = " + meg);

        // 发送消息到交换机（路由交换机）
        amqpTemplate.convertAndSend(RabbitMqConfig.CHAT_EXCHANGE, RabbitMqConfig.CHAT_ROUTING_KEY, meg);
    }
}
