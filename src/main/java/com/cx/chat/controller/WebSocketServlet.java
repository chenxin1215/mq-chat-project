package com.cx.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/13
 */
@ServerEndpoint(value = "/websocket") @Component public class WebSocketServlet {

    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketServlet.class);

    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, WebSocketServlet> clients = new ConcurrentHashMap<>();
    private Session session;
    private String username;
    private static final String NICKNAME_PRE = "user_";
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @OnOpen public void onOpen(Session session) {
        this.username = getNickname();
        this.session = session;
        addOnlineCount();
        clients.put(username, this);
        LOGGER.info("{} 已连接...", username);
    }

    @OnClose public void onClose() {
        clients.remove(username);
        subOnlineCount();
    }

    @OnMessage public void onMessage(String message, Session session) {
        LOGGER.info("收到客户端的消息：{}", message);
        sendMessageTo("已收到！", session);
    }

    @OnError public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessageTo(String message, Session session) {
        LOGGER.info("sendMessageTo：{}", message);
        for (WebSocketServlet item : clients.values()) {
            if (item.session.equals(session))
                item.session.getAsyncRemote().sendText(message);
        }
    }

    public void sendMessageAll(String message) {
        LOGGER.info("sendMessageAll：{}", message);
        for (WebSocketServlet item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static String getNickname() {
        return NICKNAME_PRE + atomicInteger.incrementAndGet();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServlet.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServlet.onlineCount--;
    }

    public static synchronized ConcurrentHashMap<String, WebSocketServlet> getClients() {
        return clients;
    }
}