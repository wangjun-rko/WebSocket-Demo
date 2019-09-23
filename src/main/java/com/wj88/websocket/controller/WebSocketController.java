package com.wj88.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * WebSockerController
 *
 * @author huayu
 * @version 1.0
 * @date 2019/9/11 19:53
 */
@Controller
@Slf4j
public class WebSocketController {

    @GetMapping("index3")
    public ModelAndView guideIndex(ModelAndView mv) {
        mv.setViewName("index3");
        return mv;
    }

    @GetMapping("index2")
    public ModelAndView guideIndex2(ModelAndView mv) {

        mv.setViewName("index2");
        return mv;
    }
}
