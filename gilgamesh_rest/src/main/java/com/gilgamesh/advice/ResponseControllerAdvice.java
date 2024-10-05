package com.gilgamesh.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilgamesh.common.annotations.ApiCodeMsg;
import com.gilgamesh.common.annotations.NotWrapApiResult;
import com.gilgamesh.common.entity.base.ApiResult;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.SystemCode;
import com.gilgamesh.common.exceptions.SystemException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 统一api响应
 * @createDate 2024/10/2 22:36
 * @since 1.0.0
 */
@RestControllerAdvice(basePackages = {"com.gilgamesh.rest"})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是ApiResult那就没有必要进行额外的操作, 返回false
        // 如果类上上加了我们的自定义数据统一响应开关注解也没有必要进行额外的操作, 返回false
        // 如果方法上加了我们的自定义数据统一响应开关注解也没有必要进行额外的操作, 返回false
        return !(returnType.getParameterType().equals(ApiResult.class) || returnType.getContainingClass().getAnnotation(NotWrapApiResult.class) != null || returnType.hasMethodAnnotation(NotWrapApiResult.class));
    }

    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (selectedContentType != MediaType.APPLICATION_JSON) {
            return body;
        }
        ApiCodeMsg apiCodeMsg = returnType.getMethodAnnotation(ApiCodeMsg.class);
        String code = BizCodeMsg.GUI_SUCCESS.getCode();
        String msg = BizCodeMsg.GUI_SUCCESS.getMsg();
        if (apiCodeMsg != null) {
            code = apiCodeMsg.value().getCode();
            msg = apiCodeMsg.value().getMsg();
        }
        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ApiResult里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(new ApiResult<>(code, msg, body));
            } catch (JsonProcessingException e) {
                throw new SystemException(SystemCode.SYSTEM_HTTP_API_JSON_PROCESS_EXCEPTION.getCode(), "统一api响应, 将String类型数据包装在ApiResult失败");
            }
        }
        // 将原本的数据包装在ApiResult里
        return new ApiResult<>(code, msg, body);
    }
}
