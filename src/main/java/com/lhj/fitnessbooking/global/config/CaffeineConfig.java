package com.lhj.fitnessbooking.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CaffeineConfig {

    @Bean
    CaffeineCacheManager cacheManager() {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache("course:seatCount", seatCountCacheBuilder().build()); // 캐시명1 (키 이름 아님)
        cacheManager.registerCustomCache("course:top3", topCourseCacheBuilder().build()); // 캐시명2 (키 이름 아님)
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> seatCountCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .maximumSize(100)
                .recordStats();
    }

    @Bean
    public Caffeine<Object, Object> topCourseCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100)
                .recordStats();
    }
}
