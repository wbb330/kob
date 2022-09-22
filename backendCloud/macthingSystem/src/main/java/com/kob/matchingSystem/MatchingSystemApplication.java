package com.kob.matchingSystem;

import com.kob.matchingSystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :王冰冰
 * @date : ${DATE}
 */
@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start(); // 启动匹配线程
        SpringApplication.run(MatchingSystemApplication.class, args);
    }
}