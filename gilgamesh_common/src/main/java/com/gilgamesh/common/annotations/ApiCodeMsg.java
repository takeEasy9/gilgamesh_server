package com.gilgamesh.common.annotations;

import com.gilgamesh.common.enums.BizCodeMsg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.gilgamesh.common.enums.BizCodeMsg.GUI_SUCCESS;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 接口请求成功时返回的消息编码与消息内容
 * @createDate 2024/10/2 22:41
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiCodeMsg {
    BizCodeMsg value() default GUI_SUCCESS;
}
