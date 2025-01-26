package com.gilgamesh.config.security;

import com.gilgamesh.common.entity.property.InterceptorProperty;
import com.gilgamesh.common.entity.property.WebSecurityPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * @author he.wei
 * @version 1.0.0
 * @description web 安全配置
 * @createDate 2025/1/24 16:08
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {
    /**
     * web 安全策略配置
     */
    private final WebSecurityPolicy webSecurityPolicy;

    /**
     * web 拦截器配置
     */
    private final InterceptorProperty interceptorProperty;

    @Autowired
    public WebSecurityConfiguration(WebSecurityPolicy webSecurityPolicy,
                                    InterceptorProperty interceptorProperty) {
        this.webSecurityPolicy = webSecurityPolicy;
        this.interceptorProperty = interceptorProperty;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 基于JWT,不需要 csrf
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> {
                    headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable)
                            // 网页安全策略,即白名单制度,明确告诉客户端,哪些外部资源可以加载和执行,不符合 CSP 的外部资源就会被阻止加载
                            .contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives(webSecurityPolicy.getContentSecurityPolicy()))
                            // Referrer策略,对于同源的请求,会发送完整的URL作为引用地址;在同等安全级别的情况下,发送文件的源作为引用地址(HTTPS->HTTPS);在降级的情况下不发送此首部 (HTTPS->HTTP)
                            .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);

                })
                // 会话管理,基于JWT,不需要 session
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 跨域问题处理
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests -> {
                    // 资源访问白名单
                    authorizeRequests.requestMatchers(interceptorProperty.getWhiteList().toArray(String[]::new)).permitAll();

                });

        return http.build();
    }
}
