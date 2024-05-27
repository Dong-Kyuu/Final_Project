package com.example.jhta_3team_finalproject.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(cacheType ->
                        new CaffeineCache(
                                cacheType.getCacheName(), // 주의! .name()은 enum의 이름으로 반환된다.
                                Caffeine.newBuilder()
                                        .recordStats()
                                        .expireAfterWrite(cacheType.getSecsToExpireAfterWrite(), TimeUnit.SECONDS)
                                        .maximumSize(cacheType.getEntryMaxSize())
                                        .build()))
                .toList();

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
