package com.wj88.websocket.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Message
 *
 * @author huayu
 * @version 1.0
 * @description Message
 * @date 2019/9/20 17:58
 */
@Getter
@Setter
public class Message {

    //用户ID
    private int id;
    //发送内容
    private String content;
    //发送到用户
    private int pid;
}
