package com.example.jhta_3team_finalproject.cache;

import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisUtils {

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public void setData(String key, ChatMessage value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public ChatMessage getData(String key){
        return (ChatMessage) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
