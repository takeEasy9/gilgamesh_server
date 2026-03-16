package com.gilgamesh.common.validators;

import com.gilgamesh.common.annotations.ValidateEnum;
import com.gilgamesh.common.enums.EnumValueLabel;
import com.gilgamesh.common.utils.EnumUtil;
import com.gilgamesh.common.utils.StringUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 枚举值校验器
 * @createDate 2024/10/5 12:04
 * @since 1.0.0
 */
public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {

    private ValidateEnum validateEnum;

    /**
     * 初始化方法
     *
     * @param notExist NotExist 注解
     */
    @Override
    public void initialize(ValidateEnum notExist) {
        this.validateEnum = notExist;
    }

    /**
     * 校验方法
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return boolean
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean skipIfEmpty = validateEnum.skipIfEmpty();
        // 如果为空，直接返回true
        if (skipIfEmpty && StringUtil.isEmpty(value)) {
            return true;
        }
        ValidateEnum.EnumValidationRule enumValidateType = validateEnum.enumValidationRule();
        // 在全部枚举值中，校验是否存在
        if (enumValidateType == ValidateEnum.EnumValidationRule.FULL_MATCH) {
            Class<? extends Enum<? extends EnumValueLabel<?>>> enumClass = validateEnum.enumClass();
            return EnumUtil.isExist(enumClass, value);
        } else if (enumValidateType == ValidateEnum.EnumValidationRule.SPECIFIC_VALUES_ONLY) {
            // 在指定的枚举中，校验是否存在
            Set<String> values = Stream.of(validateEnum.specificValues()).collect(Collectors.toSet());
            return values.contains(value);
        } else {
            // 暂不支持的枚举校验类型
            return false;
        }

    }
}
