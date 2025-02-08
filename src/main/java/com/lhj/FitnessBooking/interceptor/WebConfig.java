package com.lhj.FitnessBooking.interceptor;

import com.lhj.FitnessBooking.member.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor(jwtService))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/members/**", "/error");
    }
}
