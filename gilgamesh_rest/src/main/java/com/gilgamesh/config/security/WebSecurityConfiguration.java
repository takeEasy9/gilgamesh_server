package com.gilgamesh.config.security;

import com.gilgamesh.biz.service.security.ApiAuthorizationManager;
import com.gilgamesh.biz.service.security.JwtAccessDeniedHandler;
import com.gilgamesh.biz.service.security.JwtAuthenticationEntryPoint;
import com.gilgamesh.common.entity.property.InterceptorProperty;
import com.gilgamesh.common.entity.property.WebSecurityPolicy;
import com.gilgamesh.common.enums.SystemEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description web 安全配置
 * @createDate 2025/1/24 16:08
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * web 安全策略配置
     */
    private final WebSecurityPolicy webSecurityPolicy;

    /**
     * web 拦截器配置
     */
    private final InterceptorProperty interceptorProperty;

    /**
     * 用户详情服务
     */
    private final UserDetailsService userDetailsService;

    /**
     * token 过滤器
     */
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 用户未登录异常处理器
     */
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 用户权限不足异常处理器
     */
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 接口权限授权管理器
     */
    private final ApiAuthorizationManager apiAuthorizationManager;

    @Autowired
    public WebSecurityConfiguration(AuthenticationConfiguration authenticationConfiguration, WebSecurityPolicy webSecurityPolicy,
                                    InterceptorProperty interceptorProperty,
                                    @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                                    @Qualifier("jwtAuthenticationEntryPoint") JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                    @Qualifier("jwtAccessDeniedHandler") JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                    ApiAuthorizationManager apiAuthorizationManager) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.webSecurityPolicy = webSecurityPolicy;
        this.interceptorProperty = interceptorProperty;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.apiAuthorizationManager = apiAuthorizationManager;
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
                    authorizeRequests.requestMatchers(interceptorProperty.getWhiteList().toArray(String[]::new)).permitAll()
                            .anyRequest()
                            .access(AuthorizationManagers.allOf(
                                    // 内置的「已认证」授权管理器
                                    AuthenticatedAuthorizationManager.authenticated(),
                                    // 你的自定义权限管理器
                                    apiAuthorizationManager));

                })
                // 异常处理
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authenticationManager(this.authenticationManager(authenticationConfiguration))
                .authenticationProvider(this.authenticationProvider())
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 密码编码器
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 默认加密算法
        String idForEncode = SystemEnums.EncryptAlgorithm.ENCRYPT_ALGORITHM_PBKDF2_V5_8.getValue();
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(SystemEnums.EncryptAlgorithm.ENCRYPT_ALGORITHM_BCRYPT.getValue(), new BCryptPasswordEncoder());
        encoders.put(SystemEnums.EncryptAlgorithm.ENCRYPT_ALGORITHM_PBKDF2_V5_8.getValue(), Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put(SystemEnums.EncryptAlgorithm.ENCRYPT_ALGORITHM_SCRYPT_V5_8.getValue(), SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put(SystemEnums.EncryptAlgorithm.ENCRYPT_ALGORITHM_ARGON2_V5_8.getValue(), Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    /**
     * 配置 DaoAuthenticationProvider
     *
     * @return authenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 绑定用户查询服务
        provider.setUserDetailsService(userDetailsService);
        // 绑定密码编码器
        provider.setPasswordEncoder(this.passwordEncoder());
        return provider;
    }

    /**
     * 用 AuthenticationConfiguration 构建 AuthenticationManager
     *
     * @param config AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
