package com.kob.botRunningSystem.controller;

import com.kob.botRunningSystem.service.BotRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author :王冰冰
 * @date : 2022/9/23
 */
@RestController
public class BotRunningController {

    @Autowired
    private BotRunningService botRunningService;


    @PostMapping("/bot/add")
    public String addBot(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.valueOf(Objects.requireNonNull(data.getFirst("user_id")));
        String botCode = data.getFirst("bot_code");
        String input = data.getFirst("input");
        return botRunningService.addBot(userId, botCode, input);
    }
}
