package com.gilgamesh.common.handlers;

import com.gilgamesh.common.annotations.ApiVersion;
import com.gilgamesh.common.conditions.ApiVersionCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description api version handler
 * @createDate 2024/10/2 22:26
 * @since 1.0.0
 */
public class PathVersionHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    @Nullable
    protected RequestCondition<?> getCustomTypeCondition(@NonNull Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }

    @Override
    @Nullable
    protected RequestCondition<?> getCustomMethodCondition(@NonNull Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }

    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
    }
}
