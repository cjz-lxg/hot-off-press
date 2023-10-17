package com.russel.wemedia.config;

import com.russel.wemedia.interceptor.WnTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * author by Russel
 * created by 2023/10/17.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WnTokenInterceptor()).addPathPatterns("/**");
    }
}
