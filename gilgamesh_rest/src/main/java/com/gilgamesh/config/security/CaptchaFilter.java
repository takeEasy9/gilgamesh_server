package com.gilgamesh.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilgamesh.biz.entity.vo.LoginVO;
import com.gilgamesh.common.entity.property.WebFilterProperty;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.exceptions.BusinessException;
import com.gilgamesh.common.exceptions.VerifyException;
import com.gilgamesh.common.redis.RedisService;
import com.gilgamesh.common.utils.ConstantUtil;
import com.gilgamesh.common.utils.ResponseUtil;
import com.gilgamesh.common.utils.StringUtil;
import com.gilgamesh.common.wrapper.RepeatableReadRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description
 * @createDate 2026/1/3 7:41
 * @since 1.0.0
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(CaptchaFilter.class);

    /**
     * web 拦截器配置
     */
    private final WebFilterProperty webFilterProperty;

    /**
     * Redis缓存服务
     */
    private final RedisService redisService;

    /**
     * Jackson对象映射器
     */
    private final ObjectMapper objectMapper;

    public CaptchaFilter(WebFilterProperty webFilterProperty, RedisService redisService, ObjectMapper objectMapper) {
        this.webFilterProperty = webFilterProperty;
        this.redisService = redisService;
        this.objectMapper = objectMapper;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        // 1. 非登录POST请求直接放行（提前return，减少嵌套）
        if (!webFilterProperty.getLoginApi().equals(requestUri)
                || !ConstantUtil.HTTP_METHOD_POST.equals(requestMethod)) {
            filterChain.doFilter(request, response);
            return;
        }
        RepeatableReadRequestWrapper requestWrapper = new RepeatableReadRequestWrapper(request);
        try {

            // 4. 读取并校验请求体
            String requestBody = getRequestBody(requestWrapper);
            // 5. 解析登录参数（Jackson替代FastJson）
            LoginVO loginVo = parseLoginVo(requestBody);
            // 6. 核心校验逻辑
            validateCaptcha(loginVo);
            // 7. 校验通过，继续过滤链（传递包装后的request，保证后续能读取请求体）
            filterChain.doFilter(requestWrapper, response);
        } catch (VerifyException e) {
            logger.error("验证码校验失败：{}", e.getMessage());
            ResponseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getCode(), e.getMsg());
        } catch (Exception e) {
            // 兜底异常：请求体解析失败、系统异常等
            logger.error("登录验证码校验过滤器异常", e);
            ResponseUtil.setResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, BizCodeMsg.USER_LOGIN_FAILED);
        }

    }

    /**
     * 读取请求体（基于ContentCachingRequestWrapper，Spring原生实现）
     */
    private String getRequestBody(RepeatableReadRequestWrapper requestWrapper) throws UnsupportedEncodingException {
        // 1. 校验Content-Type是否为JSON（避免非JSON请求解析失败）
        String contentType = requestWrapper.getContentType();
        if (contentType == null || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), "登录请求必须为JSON格式");
        }
        // 2. 读取缓存的请求体字节数组（ContentCachingRequestWrapper会自动缓存请求体）
        byte[] contentAsByteArray = requestWrapper.getBody();
        if (contentAsByteArray.length == 0) {
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), "登录请求体不能为空");
        }
        // 3. 校验请求体大小，防止恶意大请求
        if (contentAsByteArray.length > webFilterProperty.getLoginMaxRequestBodySize()) {
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), String.format("登录请求体大小超过限制（最大%s字节）", webFilterProperty.getLoginMaxRequestBodySize()));
        }
        // 4. 转换为字符串（指定UTF-8编码，避免乱码）
        return requestWrapper.getBodyAsString();
    }

    /**
     * 解析登录参数（Jackson替代FastJson，Spring内置更安全）
     */
    private LoginVO parseLoginVo(String requestBody) {
        if (StringUtil.isEmpty(requestBody)) {
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), "登录请求体不能为空");
        }
        try {
            // Jackson解析JSON为LoginVO
            return objectMapper.readValue(requestBody, LoginVO.class);
        } catch (Exception e) {
            logger.error("登录请求体解析失败：请求体={}", requestBody, e);
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), "登录参数格式错误，请检查请求体");
        }
    }

    /**
     * 核心：验证码校验逻辑（业务逻辑不变）
     */
    private void validateCaptcha(LoginVO loginVo) {
        // 1. 基础参数校验（空值+有效性）
        if (loginVo == null || StringUtil.isEmpty(loginVo.getVerifyKey()) || StringUtil.isEmpty(loginVo.getVerifyCode())) {
            logger.error("验证码校验失败：用户未输入任何有效的验证码 或者 未传入有效的验证码验证参数");
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), "请输入验证码");
        }
        // 2. 校验验证码是否过期（Redis中不存在）
        if (!redisService.hasKey(loginVo.getVerifyKey())) {
            logger.warn("验证码校验失败：校验Key已过期 | verifyKey={}", loginVo.getVerifyKey());
            throw new VerifyException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED.getCode(), "验证码已过期，请重新获取");
        }
        // 3. 校验验证码是否正确（忽略大小写比较）
        String verifyCode = (String) redisService.getValue(loginVo.getVerifyKey());
        if (StringUtil.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(loginVo.getVerifyCode())) {
            logger.warn("验证码校验失败：输入验证码与缓存不一致 | verifyKey={}, 输入验证码={}", loginVo.getVerifyKey(), verifyCode);
            throw new BusinessException(BizCodeMsg.VALIDATION_CODE_VERIFY_FAILED);
        }
        // 4. 校验通过：立即删除Redis中的验证码（防止复用）
        boolean deleteResult = redisService.deleteKey(loginVo.getVerifyKey());
        logger.info("验证码校验通过：已删除Redis中的验证码, verifyKey={}, 删除结果={}", loginVo.getVerifyKey(), deleteResult);
    }
}
