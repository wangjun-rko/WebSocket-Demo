package com.wj88.websocket.listenter;

import com.wj88.websocket.util.SocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * StompConnectEventListener
 *
 * @author huayu
 * @version 1.0
 * @description 监听注册 对STOMP监听类进行扩展
 * @date 2019/9/20 17:54
 */
@Component
public class StompConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private SocketSessionMap socketSessionMap;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        // 读取到客户端的ID，这个值需要网页客户端手动在header头部信息中设置。
        String userId = sha.getFirstNativeHeader("id");
        String sessionId = sha.getSessionId();
        //判断客户端的连接状态
        switch (sha.getCommand()) {
            case CONNECT:
                System.out.println("上线：" + userId + "  " + sessionId);
                // 当服务端监听到客户端连接时，会将用户SessionId注册到Map中。
                socketSessionMap.registerSession(userId, sessionId);
                break;
            case DISCONNECT:
                System.out.println("下线：" + userId + "  " + sessionId);
                socketSessionMap.removeSession(userId,sessionId);
                break;
            case SUBSCRIBE:
                System.out.println("订阅");
                break;
            case SEND:
                System.out.println("发送");
                break;
            case UNSUBSCRIBE:
                System.out.println("取消订阅");
                break;
            default:
                break;
        }
    }
}
