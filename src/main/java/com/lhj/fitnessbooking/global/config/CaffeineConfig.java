package com.lhj.fitnessbooking.global.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.List;

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
                .expireAfterWrite(3, TimeUnit.SECONDS) // TODO: TTL 실험 필요
                .maximumSize(100)
                .recordStats();
    }

    @Bean
    public Cache<String, List<Long>> waitCourseListCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES) // TODO: TTL 실험 필요
                .maximumSize(100)
                .recordStats()
                .build();
    }

    @Bean
    public Caffeine<Object, Object> topCourseCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1)
                .recordStats();
    }
}
