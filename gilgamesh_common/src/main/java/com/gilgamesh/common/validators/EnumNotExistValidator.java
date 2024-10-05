package com.gilgamesh.common.validators;

import com.gilgamesh.common.annotations.NotExist;
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
public class EnumNotExistValidator implements ConstraintValidator<NotExist, String> {

    private NotExist notExist;

    /**
     * 初始化方法
     *
     * @param notExist NotExist 注解
     */
    @Override
    public void initialize(NotExist notExist) {
        this.notExist = notExist;
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

        boolean skipIfEmpty = notExist.skipIfEmpty();
        // 如果为空，直接返回true
        if (skipIfEmpty && StringUtil.isEmpty(value)) {
            return true;
        }
        NotExist.EnumValidateType enumValidateType = notExist.enumValidateType();
        // 在全部枚举值中，校验是否存在
        if (enumValidateType == NotExist.EnumValidateType.ALL) {
            Class<? extends Enum<? extends EnumValueLabel<?>>> enumClass = notExist.enumClass();
            return EnumUtil.isExist(enumClass, value);
        } else if (enumValidateType == NotExist.EnumValidateType.PARTIAL) {
            // 在指定的枚举中，校验是否存在
            Set<String> values = Stream.of(notExist.values()).collect(Collectors.toSet());
            return values.contains(value);
        } else {
            // 暂不支持的枚举校验类型
            return false;
        }

    }
}
