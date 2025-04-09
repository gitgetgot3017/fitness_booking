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

        CaffeineCacheManager cacheManager = new CaffeineCacheManager("course:seatCount");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(3, TimeUnit.SECONDS)
                        .maximumSize(100)
                        .recordStats()
        );
        return cacheManager;
    }
}
