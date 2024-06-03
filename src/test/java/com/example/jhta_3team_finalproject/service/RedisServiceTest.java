package com.example.jhta_3team_finalproject.service;

import com.example.jhta_3team_finalproject.cache.RedisUtils;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.mybatis.mapper.ChatMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceTest {

    private final static Logger logger = LoggerFactory.getLogger(RedisServiceTest.class);


    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    RedisTemplate<String, ChatMessage> redisTemplate;
    @Autowired
    RedisUtils redisUtils;

    @Test
    void 레디스_삽입_테스트() {
        //given
        logger.info("getChatMessage");
        List<ChatMessage> list = redisService.getChatMessage();

        //when
        list.forEach(chatMessage -> {
            logger.info("chatMessage: {}", chatMessage);
            redisUtils.setData(String.valueOf(chatMessage.getChatRoomNum()), chatMessage, (long) 10000);
        });

        //then

    }
}