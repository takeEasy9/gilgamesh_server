package com.gilgamesh.config.security;

import com.gilgamesh.biz.service.security.JwtService;
import com.gilgamesh.common.entity.property.WebFilterProperty;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.utils.ConstantUtil;
import com.gilgamesh.common.utils.ResponseUtil;
import com.gilgamesh.common.utils.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description
 * @createDate 2025/11/18 20:14
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    /**
     * 统一路径匹配工具
     */
    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    /**
     * web 拦截器配置
     */
    private final WebFilterProperty webFilterProperty;

    /**
     * JWT服务类
     */
    private final JwtService jwtService;

    /**
     * 用户详情服务类
     */
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationTokenFilter(WebFilterProperty webFilterProperty, JwtService jwtService, @Qualifier("userDetailsService") UserDetailsService userDetailsService) {
        this.webFilterProperty = webFilterProperty;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = URL_PATH_HELPER.getPathWithinApplication(request);
        logger.info("当前请求资源：<{}>", requestUri);
        // 1. 白名单路径直接放行（优化：用 UrlPathHelper 统一路径匹配，避免 servletPath 遗漏上下文）
        if (webFilterProperty.getWhiteList().stream().anyMatch(whitePath ->
                URL_PATH_HELPER.getPathWithinApplication(request).matches(normalizeWhitePath(whitePath)))) {
            logger.debug("白名单场景，无需认证：<{}>", requestUri);
            filterChain.doFilter(request, response);
            return;
        }
        // 2. 提取并验证 Token 格式（优化：提前校验格式，减少无效调用）
        String accessToken = jwtService.getAccessTokenFromHttpRequest(request);
        // 3. 校验 access token 状态(优化：状态判断后直接闭环，避免冗余逻辑)
        SystemEnums.JwtTokenStatus tokenStatus = jwtService.checkAccessToken(accessToken);
        switch (tokenStatus) {
            case JWT_TOKEN_STATUS_INVALID:
                logger.error("Token 无效：<{}>，请求：<{}>", accessToken, requestUri);
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_ACCESS_TOKEN_EXPIRED);
                return;
            case JWT_TOKEN_STATUS_EXPIRED:
                logger.warn("Token 已过期，尝试自动续期：<{}>", accessToken);
                // 续期失败直接返回 401(优化：处理续期失败场景)
                boolean renewResult = jwtService.renewJwtToken(request, response, accessToken);
                if (!renewResult) {
                    ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_ACCESS_TOKEN_EXPIRED_RENEW_FAILED);
                    return;
                }
                break;
            case JWT_TOKEN_STATUS_VALID:
                // 有效 Token 直接放行后续逻辑
                break;
            default:
                logger.error("未知 Token 状态：<{}>，请求：<{}>", tokenStatus, requestUri);
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_ACCESS_TOKEN_INVALID);
                return;
        }
        // 4. 提取用户信息并校验（优化：增加用户状态校验、权限一致性校验）
        UserDetails jwtUserDetail = jwtService.getUserDetailFromAccessToken(accessToken);
        if (jwtUserDetail == null || StringUtil.isEmpty(jwtUserDetail.getUsername())) {
            logger.error("Token 负载无有效用户信息：<{}>", accessToken);
            ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_ACCESS_TOKEN_INVALID);
            return;
        }
        // 5. 安全上下文处理（优化：避免重复认证、校验用户有效性）
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails dbUserDetails = userDetailsService.loadUserByUsername(jwtUserDetail.getUsername());
            // 校验：Token 用户名与数据库一致 + 数据库用户状态有效（需确保 UserDetails 实现 isEnabled()）
            if (!jwtUserDetail.getUsername().equals(dbUserDetails.getUsername()) || !dbUserDetails.isEnabled()) {
                logger.error("用户信息不匹配或已禁用：<{}>", jwtUserDetail.getUsername());
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_USER_INVALID);
                return;
            }
            // 构建认证令牌（优化：用数据库用户的权限，避免 Token 篡改权限）
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    dbUserDetails, null, dbUserDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("用户认证成功：<{}>，请求：<{}>", dbUserDetails.getUsername(), requestUri);
        }
        // 过滤器放行
        filterChain.doFilter(request, response);
    }


    /**
     * 规范化白名单路径（支持通配符，如 /api/* → ^/api/.*$）
     */
    private String normalizeWhitePath(String whitePath) {
        if (StringUtil.isEmpty(whitePath)) {
            return ConstantUtil.STRING_EMPTY;
        }
        // 替换 * 为正则通配符 .*，并添加开始和结束符，避免部分匹配
        return "^" + whitePath.replace("*", ".*") + "$";
    }
}
