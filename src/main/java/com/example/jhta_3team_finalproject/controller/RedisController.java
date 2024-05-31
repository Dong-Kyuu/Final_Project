package com.example.jhta_3team_finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/redisTest")
    public ResponseEntity<?> addRedisKey() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("test-key-2", "test-value-2");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
