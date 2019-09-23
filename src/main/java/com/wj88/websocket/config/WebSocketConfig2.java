package com.wj88.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 首先注入一个ServerEndpointExporterBean,
 * 该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
 *
 * @author huayu
 * @version 1.0
 * @date 2019/9/11 19:35
 */
@Configuration
public class WebSocketConfig2 {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
