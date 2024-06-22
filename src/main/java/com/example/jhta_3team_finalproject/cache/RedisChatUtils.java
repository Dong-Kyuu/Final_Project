package com.example.jhta_3team_finalproject.cache;

import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisChatUtils {

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public void setExpired(String key, Long expiredTime) {
        redisTemplate.expire(key, expiredTime, TimeUnit.DAYS);
    }

    public void setAddList(String key, ChatMessage value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public List<ChatMessage> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public boolean isKeyExists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setUpdateList(String key, ChatMessage newValue) {
        redisTemplate.opsForList().rightPop(key);
        redisTemplate.opsForList().rightPush(key, newValue);
    }

    public void setData(String key, ChatMessage value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.DAYS);
    }

    public ChatMessage getData(String key){
        return (ChatMessage) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
