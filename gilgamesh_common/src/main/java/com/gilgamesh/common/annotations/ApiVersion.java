package com.gilgamesh.common.annotations;

import com.gilgamesh.common.utils.ConstantUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description api 版本管理注解
 * @createDate 2024/10/2 22:13
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiVersion {

    /**
     * api 版本号, 默认接口版本号v1, 用于接口版本管理, 方法级别优先级高于类级别
     *
     * @return String
     */
    String value() default ConstantUtil.DEFAULT_API_VERSION;
}
