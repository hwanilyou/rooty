package com.example.userservice.config;

import com.example.userservice.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthFilter);
        registrationBean.addUrlPatterns("/api/*"); // /api/ 하위 요청에만 필터 적용
        registrationBean.setOrder(1); // 필터 우선순위
        return registrationBean;
    }
}
