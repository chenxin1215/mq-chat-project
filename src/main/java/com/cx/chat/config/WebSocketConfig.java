package com.cx.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <>
 *
 * @Author: chenxin
 * @Date: 2021/3/13
 */
@Configuration public class WebSocketConfig {
    private static final long serialVersionUID = 7600559593733357846L;

    @Bean public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}