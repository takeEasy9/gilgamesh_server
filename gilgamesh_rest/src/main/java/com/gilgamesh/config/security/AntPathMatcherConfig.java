package com.gilgamesh.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description AntPathMatcherConfig
 * @createDate 2025/11/23 12:21
 * @since 1.0.0
 */
@Configuration
public class AntPathMatcherConfig {

    @Bean
    public AntPathMatcher antPathMatcher() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 路径匹配不区分大小写,默认区分
        pathMatcher.setCaseSensitive(true);
        // 路径分隔符(默认就是 "/"，可省略)
        pathMatcher.setPathSeparator("/");
        return pathMatcher;
    }
}
