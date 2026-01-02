package com.gilgamesh.config;

import com.gilgamesh.common.handlers.PathVersionHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description web mvc 配置
 * @createDate 2024/10/2 22:34
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public WebMvcRegistrations getWebMvcRegistrations() {
        return new CustomWebMvcRegistrations();
    }

    // 自定义Spring MVC配置, 添加接口版本号处理器映射
    private static class CustomWebMvcRegistrations implements WebMvcRegistrations {
        @Override
        public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
            return new PathVersionHandlerMapping();
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 仅对API路径启用CORS
                .allowedOrigins("*") // 允许所有来源
                .allowedHeaders("*") // 允许所有请求头
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 限制允许的方法
                .exposedHeaders("Authorization")// 限制允许的请求头
                .maxAge(3600L);
    }
}
