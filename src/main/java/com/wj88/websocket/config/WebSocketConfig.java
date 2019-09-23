package com.wj88.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocketConfig
 *
 * EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，
 * 这时候控制器（controller）开始支持@MessageMapping,就像是使用@requestMapping一样。
 *
 * @author huayu
 * @version 1.0
 * @description webSocket配置
 * @date 2019/9/20 16:51
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个Stomp的节点（endpoint）,也就是webSocket的服务端地址，客户端在链接时使用到
        // withSockJs()方法指定使用SockJS协议。SockJs是一个WebSocket的通信js库，Spring对这个js库进行了后台的自动支持，如果使用它不需要进行过多配置
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用一个简单的message broker
        // 配置一个或多个前缀来过滤针对代理的目的地(例如以“/topic”为前缀的目的地)，该前戳限制了网页客户端设置本地地址时的前戳。
        config.enableSimpleBroker("/topic");
        // 设置了一个客户端访问服务端地址的前缀。比如我们设置@MessageMapping("/hello")，那客户端要发送消息到服务器上的地址是 /app/hello。
        config.setApplicationDestinationPrefixes("/app");
    }

}
