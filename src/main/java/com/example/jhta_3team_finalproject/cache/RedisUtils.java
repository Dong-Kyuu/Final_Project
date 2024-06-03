package com.example.jhta_3team_finalproject.cache;

import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisUtils {

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public void setExpired(String key, Long expiredTime) {
        redisTemplate.expire(key, expiredTime, TimeUnit.SECONDS);
    }

    public void setInitList(String key, ChatMessage value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public List<ChatMessage> getList(String key) {
        // -1 인 경우 전체 조회
        return  redisTemplate.opsForList().range(key, 0, -1);
    }

    public void setData(String key, ChatMessage value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
    }

    public ChatMessage getData(String key){
        return (ChatMessage) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
