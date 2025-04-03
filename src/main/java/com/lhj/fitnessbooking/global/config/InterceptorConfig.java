package com.lhj.fitnessbooking.global.config;

import com.lhj.fitnessbooking.global.interceptor.LoginCheckInterceptor;
import com.lhj.fitnessbooking.global.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final JwtService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor(jwtService))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/members/**", "/error", "/api/refresh/token", "/api/notifications");
    }
}
