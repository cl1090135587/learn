package com.disruptor.learn.zookeeper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author chenlong
 * @date 2019-09-18 21:03
 */
@Component
@Data
public class Configs {

    @Value("${test.test}")
    private String test;
}
