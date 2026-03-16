package com.gilgamesh.common.annotations;

import com.gilgamesh.common.enums.EnumValueLabel;
import com.gilgamesh.common.validators.EnumValidator;
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
@Constraint(validatedBy = {EnumValidator.class})
public @interface ValidateEnum {
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

    enum EnumValidationRule {
        /**
         * 全量匹配：枚举所有取值均为合法值，参数只要在枚举范围内即校验通过
         * 示例：枚举取值 [1,2,3]，参数 1/2/3 均合法
         */
        FULL_MATCH,
        /**
         * 仅特定值合法：仅枚举中预先指定的部分取值为合法值，超出指定范围即校验失败
         * 示例：枚举取值 [1,2,3]，仅指定 [1,3] 为合法值，参数 2 非法
         */
        SPECIFIC_VALUES_ONLY
    }

    EnumValidationRule enumValidationRule() default EnumValidationRule.FULL_MATCH;

    // 枚举类
    Class<? extends Enum<? extends EnumValueLabel<?>>> enumClass() default DefaultEnum.class;

    // 允许的枚举值
    String[] specificValues() default {};

    // 待校验值为空时是否跳过校验
    boolean skipIfEmpty() default false;

    String message() default "{com.gilgamesh.common.annotations.ValidateEnum.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
