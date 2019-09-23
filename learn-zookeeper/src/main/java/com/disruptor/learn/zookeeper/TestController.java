package com.disruptor.learn.zookeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlong
 * @date 2019-09-18 21:04
 */
@RestController
public class TestController {

    @Autowired
    private Configs configs;


    @GetMapping("/test")
    public String test() {
        return configs.getTest();
    }
}
