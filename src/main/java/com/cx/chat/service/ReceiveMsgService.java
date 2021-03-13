package com.cx.chat.service;

import com.cx.chat.config.RabbitMqConfig;
import com.cx.chat.controller.WebSocketServlet;
import com.cx.chat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <消费MQ消息>
 *
 * @Author: chenxin
 * @Date: 2021/3/5
 */
@Service public class ReceiveMsgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveMsgService.class);

    private static final String CHAT_MSG_CACHE = "chat:msg";

    private static final int TIME_OUT = 3600 * 12 * 7;

    @Autowired private RabbitTemplate rabbitTemplate;

    @Autowired private WebSocketServlet webSocketServlet;

    @Resource(name = "redisTemplate") private RedisTemplate<String, List<ChatMessage>> chatMessageRedisTemplate;

    @RabbitListener(queues = RabbitMqConfig.CHAT_QUEUE) public void consumer(ChatMessage msg) {
        LOGGER.info("消息监听器 触发");
        // 添加消息缓存
        ValueOperations<String, List<ChatMessage>> ops = chatMessageRedisTemplate.opsForValue();
        List<ChatMessage> chatMessages = ops.get(CHAT_MSG_CACHE);
        if (CollectionUtils.isEmpty(chatMessages)) {
            chatMessages = new ArrayList<>();
        }
        chatMessages.add(msg);

        ops.set(CHAT_MSG_CACHE, chatMessages);
        chatMessageRedisTemplate.expire(CHAT_MSG_CACHE, TIME_OUT, TimeUnit.SECONDS);

        // 通知前端接收消息
        webSocketServlet.sendMessageAll("getAllList");
    }

    public List<ChatMessage> getMsgList() {
        ValueOperations<String, List<ChatMessage>> ops = chatMessageRedisTemplate.opsForValue();
        return ops.get(CHAT_MSG_CACHE);
    }

}
