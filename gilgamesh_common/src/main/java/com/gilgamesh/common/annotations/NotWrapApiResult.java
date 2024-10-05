package com.gilgamesh.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 统一接口响应格式开关注解, 当接口上有该注解时, 不对返回内容做包装
 * @createDate 2024/10/2 22:06
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NotWrapApiResult {
}
