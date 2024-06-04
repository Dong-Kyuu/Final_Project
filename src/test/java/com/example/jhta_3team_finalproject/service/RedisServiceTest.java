package com.example.jhta_3team_finalproject.service;

import com.example.jhta_3team_finalproject.cache.RedisUtils;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.mybatis.mapper.chat.ChatMapper;
import com.example.jhta_3team_finalproject.service.chat.RedisService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    //@Test
    void 레디스_테스트() {

        /**
         * given - 1주일 전 데이터, 5번 채팅방 세팅
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);   // - 현재부터 1주일 정각 전 내용만 가져오게 설정
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        Date oneWeekAgoDate = c.getTime();
        String oneWeekAgoDateStr = simpleDateFormat.format(oneWeekAgoDate);
        logger.info("{}", oneWeekAgoDate);
        logger.info("1주일 전은 {} 입니다.", oneWeekAgoDateStr);
        List<ChatMessage> chatMessageList = redisService.getChatMessage(5, oneWeekAgoDate);
        logger.info("getChatMessage");

        /**
         * when - RDB -> Redis 삽입
         */

        chatMessageList.forEach(chatMessage -> {
            //logger.info("chatMessage: {}", chatMessage);
            String roomKey = String.valueOf(chatMessage.getChatRoomNum());
            ChatMessage value = chatMessage;
            String dateKey = simpleDateFormat.format(chatMessage.getSendTime());
            String key = roomKey + ":" + dateKey; // 방번호:날짜
            long expiredTime = (chatMessage.getSendTime().getTime() - oneWeekAgoDate.getTime()) / 1000; // 초로 변경

            if(dateKey.equals("2024-05-27")){
                logger.info("{}", expiredTime);
            }

            redisUtils.setInitList(key, value);
            redisUtils.setExpired(key, expiredTime);
        });

        /**
         * then - 1주일 전까지를 키로 검색하여 가져오기
         * String key = "5:2024-05-31"; // 방번호:날짜
         */
        String key = "5:2024-05-27";
        List<ChatMessage> chatList = redisUtils.getList(key);
        chatList.forEach(chatMessage -> {
            logger.info("{}", chatMessage);
        });
    }



    @Test
    void 배치_테스트() {
        /**
         * given
         */

        /**
         * when
         */

        /**
         * then
         */

    }
}