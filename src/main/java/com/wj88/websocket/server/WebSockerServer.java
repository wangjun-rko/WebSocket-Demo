package com.wj88.websocket.server;

import com.wj88.websocket.task.TestTask;
import com.wj88.websocket.util.SpringContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * WebSockerServer
 *
 * @author huayu
 * @version 1.0
 * @date 2019/9/11 19:41
 */
@ServerEndpoint(value = "/websocket/{id}") // 接受websocket请求路径
@Component
@Slf4j
public class WebSockerServer {

    private static Map<String, Session> webSocketMap = new HashMap<>(16);

    private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(2, 2, 1L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), (r, executor) -> executor.execute(r));

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        webSocketMap.put(id, session);
        log.info("有连接加入，id为：{}", id);
        SendMessage("连接成功", id);

        log.info("<<<<<<----taskExecutor：{}-----", SpringContextProvider.getBean("taskExecutor"));

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("id") String id) {
        webSocketMap.remove(id);
        log.info("有连接关闭，id为：{}", id);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("id") String id) {
        log.info("来自客户端{}的消息：{}", id, message);
        SendMessage(message, id);

    }

    /**
     * 出现错误
     * 
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam("id") String id) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
    }

    /**
     * 指定Session发送消息
     * 
     */
    public static void SendMessage(String message, String id) {
        Session session = webSocketMap.get(id);
        if (session != null) {
            SendMessage(session, message, id);
        } else {
            log.warn("没有找到你指定ID的会话：{}", id);
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * 
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message, String id) {
        try {
            session.getBasicRemote().sendText(String.format("%s (From Server， ID=%s)", message, id));
            THREAD_POOL.execute(new TestTask(session));
        } catch (IOException e) {
            log.error("SendMessage error:{}", e.getMessage());
        }
    }


}
