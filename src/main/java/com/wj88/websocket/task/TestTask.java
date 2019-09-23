package com.wj88.websocket.task;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Random;

/**
 * TestTask
 *
 * @author huayu
 * @version 1.0
 * @date 2019/9/12 13:39
 */
@Slf4j
public class TestTask implements Runnable {

    private Session session;

    public TestTask(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        int sum = 0;
        Random random = new Random();
        log.info("<<<--task begin--->>>");
        try {
            while (sum < 100) {
                int flag = random.nextInt(10);
                sum += flag;
                if (sum > 100) {
                    break;
                }
                Thread.sleep(flag * 800);
                log.info("<<<---sum: {}--->>>",sum);
                session.getBasicRemote().sendText(sum + "%");
            }
            session.getBasicRemote().sendText("stop");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("<<<--task end--->>>");
    }
}
