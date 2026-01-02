package com.gilgamesh.biz.service.security;

import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 自定义的用户权限不足处理器
 * @createDate 2025/11/22 21:09
 * @since 1.0.0
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    /**
     * 已登录但权限不足时(即认证通过，但无访问当前资源的权限)，统一返回自定义的 403 响应(而非 Spring Security 默认的 403 页面 / 响应)，适配 JWT 令牌认证的前后端分离架构
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param accessDeniedException AccessDeniedException
     * @throws IOException IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        this.logger.error("用户权限不足,无法访问 <{}>", request.getRequestURI());
        ResponseUtil.setResponse(response, HttpStatus.FORBIDDEN.value(), BizCodeMsg.AUTH_NO_PERMISSION);
    }
}
