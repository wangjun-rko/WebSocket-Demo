package com.wj88.websocket.controller;

import com.wj88.websocket.model.Greeting;
import com.wj88.websocket.model.HelloMessage;
import com.wj88.websocket.model.Message;
import com.wj88.websocket.util.SocketSessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

/**
 * MyController
 *
 * MessageMapping和@RequestMapping功能类似，用于设置URL映射地址，浏览器向服务器发起请求，需要通过该地址。
 * 需要注意，这里设置路径为/hello，但是客户端需要访问/app/hello，原因前面已经配置了/app前缀。
 *
 * SendTo("/topic/greetings") 设置目的地，这里的目的地是站在服务端的角度对客户端而言。
 * 客户端也需要设置相同的地址，而且必须使用/topic前戳，原因前面已经配置了/topic前戳。
 *
 * @author huayu
 * @version 1.0
 * @description 提供接口
 * @date 2019/9/20 16:56
 */
@Controller
public class MyController {

    @Autowired
    private SocketSessionMap socketSessionMap;

    // 用编程的方式发送消息。直接引用该消息模板
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message)  {
        // 如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息。
        System.out.println("收到：" + message.toString() + "消息");
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/chatOut")
    public void sayHello(String userId) {
        String sessionId = socketSessionMap.getUserSessionId(userId);
        System.out.println("下线：" + userId + "  " + sessionId);
        socketSessionMap.removeSession(userId,sessionId);
    }

    @RequestMapping("/chat/{id}")
    public String chat_page(@PathVariable int id, ModelMap model) {
        model.addAttribute("id", id);
        int count = socketSessionMap.onlineCount();
        model.addAttribute("count", count);
        return "chat";
    }

    @MessageMapping("/chat")
    public void sayHello(Message user) {
        System.out.println(user.getId()+"-->"+user.getPid()+":"+user.getContent());
        String userPid = String.valueOf(user.getPid());
        String userId = String.valueOf(user.getId());
        String sendTo = "/topic/chat/"+userPid;
        String content = user.getId()+":"+user.getContent();
        if (socketSessionMap.getUserSessionId(userPid)!=null){
            // 包装消息并将其发送到给定的目的地
            messagingTemplate.convertAndSend(sendTo, HtmlUtils.htmlEscape(content));
        }else {
            sendTo = "/topic/chat/"+userId;
            content = "对方已下线";
            messagingTemplate.convertAndSend(sendTo, HtmlUtils.htmlEscape(content));
        }
    }

}
