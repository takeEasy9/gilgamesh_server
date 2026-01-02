package com.gilgamesh.biz.service.security;

import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 自动定义未登录异常处理
 * @createDate 2025/11/22 21:22
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        this.logger.error("用户未登录,无权限访问 <{}>", request.getRequestURI());
        ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_NOT_LOGIN);
    }
}
