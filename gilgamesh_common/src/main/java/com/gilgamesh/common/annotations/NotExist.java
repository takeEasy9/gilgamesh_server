package com.gilgamesh.common.annotations;

import com.gilgamesh.common.enums.EnumValueLabel;
import com.gilgamesh.common.validators.EnumNotExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 枚举值校验注解
 * @createDate 2024/10/5 12:00
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumNotExistValidator.class})
public @interface NotExist {
    enum DefaultEnum implements EnumValueLabel<String> {
        ;

        @Override
        public String getValue() {
            return "";
        }

        @Override
        public String getLabel() {
            return "";
        }
    }

    enum EnumValidateType {
        ALL,
        PARTIAL,
    }

    EnumValidateType enumValidateType() default EnumValidateType.ALL;

    // 枚举类
    Class<? extends Enum<? extends EnumValueLabel<?>>> enumClass() default DefaultEnum.class;

    // 允许的枚举值
    String[] values() default {};

    // 待校验值为空时是否跳过校验
    boolean skipIfEmpty() default false;

    String message() default "{com.hx.ylb.common.annotation.NotExist.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
