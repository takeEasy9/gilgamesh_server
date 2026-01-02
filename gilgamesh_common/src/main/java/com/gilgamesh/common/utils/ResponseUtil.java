package com.gilgamesh.common.utils;

import com.gilgamesh.common.entity.base.ApiResult;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.CodeMsg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 响应工具类
 * @createDate 2024/5/2 15:52
 * @since 1.0.0
 */
public class ResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    private ResponseUtil() {

    }

    /**
     * 接口正常返回结果封装方法
     *
     * @param codeMsg codeMsg api消息编码与消息枚举值
     * @param data    Object 接口待返回数据
     * @return Mono<Map < String, Object>>
     */
    public static <T> ApiResult<T> success(CodeMsg codeMsg, T data) {
        return new ApiResult<>(codeMsg, data);
    }

    /**
     * 接口异常返回结果封装方法
     *
     * @param codeMsg api消息编码与消息枚举值
     * @return Mono<Map < String, Object>>
     */
    @SuppressWarnings("rawtypes")
    public static ApiResult failed(CodeMsg codeMsg) {
        return failed(codeMsg.getCode(), codeMsg.getMsg());
    }

    /**
     * 接口异常返回结果封装方法
     *
     * @param code 消息编码
     * @param msg  消息
     * @return Mono<Map < String, Object>>
     */
    @SuppressWarnings("rawtypes")
    public static ApiResult failed(String code, String msg) {
        return new ApiResult(code, msg);
    }

    /**
     * 响应结果封装方法
     *
     * @param response   响应对象
     * @param httpStatus 响应状态码
     * @param codeMsg    响应消息枚举值
     */
    public static void setResponse(HttpServletResponse response, int httpStatus, BizCodeMsg codeMsg) {
        setResponse(response, httpStatus, codeMsg.getCode(), codeMsg.getMsg());
    }

    /**
     * 设置 响应, 优化：统一响应格式，避免乱码
     *
     * @param response   响应对象
     * @param httpStatus 响应状态码
     * @param code       响应消息编码
     * @param msg        响应消息
     */
    public static void setResponse(HttpServletResponse response, int httpStatus, String code, String msg) {
        // HttpServletResponse.SC_UNAUTHORIZED
        response.setStatus(httpStatus);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 统一 JSON 响应格式（便于前端解析）
        String responseJson = String.format("{\"code\":\"%s\",\"msg\":\"%s\"}", code, msg);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(responseJson);
            writer.flush();
        } catch (IOException e) {
            log.error("设置响应结果异常", e);
        }
    }

    /**
     * 构建 HttpOnly Cookie（安全配置）
     */
    public static ResponseCookie buildHttpOnlyCookie(String name, String value, long expireMs) {
        return ResponseCookie.from(name, value)
                .httpOnly(true) // 禁止前端 JS 读取（防 XSS）
                .secure(false) // 仅 HTTPS 传输（生产环境必开）
                .sameSite("Strict") // 防 CSRF
                .path("/") // 全接口生效
                .maxAge(expireMs / 1000) // 过期时间（秒）
                .build();
    }

    /**
     * 从 HttpServletRequest 中提取指定名称的 Cookie 值
     *
     * @param request    请求对象
     * @param cookieName 要提取的 Cookie 名称（如 refresh_token）
     * @return Cookie 值（不存在返回 null）
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        // 1. 获取 Cookie 头(格式：key1=value1; key2=value2)
        String cookieHeader = request.getHeader("Cookie");
        if (!StringUtils.hasText(cookieHeader)) {
            return null;
        }

        // 2. 解析 Cookie 键值对(分割符：; ，可能包含空格)
        Map<String, String> cookieMap = new HashMap<>();
        Arrays.stream(cookieHeader.split(";"))
                .map(String::trim) // 去除空格(如 "refresh_token=xxx " → "refresh_token=xxx")
                .filter(cookie -> cookie.contains("=")) // 过滤无效 Cookie 格式
                .forEach(cookie -> {
                    String[] keyValue = cookie.split("=", 2); // 分割为 key 和 value(避免 value 包含 =)
                    String key = keyValue[0].trim();
                    String value = keyValue.length > 1 ? keyValue[1].trim() : ConstantUtil.STRING_EMPTY;
                    cookieMap.put(key, value);
                });

        // 3. 返回指定名称的 Cookie 值
        return cookieMap.get(cookieName);
    }
}
