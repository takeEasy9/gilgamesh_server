package com.gilgamesh.config;

import com.gilgamesh.common.handlers.PathVersionHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description webmvc 配置
 * @createDate 2024/10/2 22:34
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfiguration {

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
}
