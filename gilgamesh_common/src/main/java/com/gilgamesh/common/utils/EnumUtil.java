package com.gilgamesh.common.utils;

import com.gilgamesh.common.enums.EnumValue;
import com.gilgamesh.common.enums.EnumValueLabel;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 枚举工具类
 * @createDate 2024/5/2 15:52
 * @since 1.0.0
 */
public class EnumUtil {
    private EnumUtil() {
    }

    /**
     * 判断枚举值是否存在于指定枚举数组中
     *
     * @param enums 枚举数组
     * @param value 枚举值
     * @param <T>   枚举值类型
     * @return True-枚举值存在 False-枚举值不存在
     */
    public static <T> boolean isExist(EnumValue<T>[] enums, T value) {
        if (CollectionUtil.isEmpty(enums) || null == value) {
            return false;
        }
        for (EnumValue<T> e : enums) {
            if (value.equals(e.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断枚举值是否不存在于指定枚举数组中
     *
     * @param enums 枚举数组
     * @param value 枚举值
     * @param <T>   枚举值类型
     * @return True-枚举值不存在 False-枚举值存在
     */
    public static <T> boolean isNotExist(EnumValue<T>[] enums, T value) {
        return !isExist(enums, value);
    }


    /**
     * 判断枚举值是否存与指定枚举类中
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <E>       枚举类型
     * @param <T>       枚举值类型
     * @return True-枚举值存在 False-枚举值不存在
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<? extends EnumValue<T>>, T> boolean isExist(Class<E> enumClass, T value) {
        if (null == enumClass || null == value) {
            return false;
        }
        for (Enum<? extends EnumValue<T>> e : enumClass.getEnumConstants()) {
            if (((EnumValue<T>) e).getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断枚举值是否不存与指定枚举类中
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <E>       枚举类型
     * @param <T>       枚举值类型
     * @return True-枚举值不存在 False-枚举值存在
     */
    public static <E extends Enum<? extends EnumValue<T>>, T> boolean isNotExist(Class<E> enumClass, T value) {
        return !isExist(enumClass, value);
    }

    /**
     * 判断枚举值是否不存与指定枚举类中
     *
     * @param enumClass Class<? extends Enum<? extends EnumValueLabel<?>>>
     * @param value     String
     * @return True-枚举值不存在 False-枚举值存在
     */
    public static boolean isExist(Class<? extends Enum<? extends EnumValueLabel<?>>> enumClass, String value) {
        // 检查输入是否为null
        if (enumClass == null || value == null) {
            return false;
        }
        // 获取枚举类的所有实例
        Enum<? extends EnumValueLabel<?>>[] enumConstants = enumClass.getEnumConstants();
        // 遍历所有枚举实例
        for (Enum<? extends EnumValueLabel<?>> enumConstant : enumConstants) {
            // 获取当前枚举实例的值
            EnumValueLabel<?> enumValueLabel = (EnumValueLabel<?>) enumConstant;
            String currentValue = (String) enumValueLabel.getValue();

            // 比较当前枚举实例的值与提供的值
            if (value.equals(currentValue)) {
                // 如果值匹配，则返回true
                return true;
            }
        }
        // 如果没有找到匹配的值，则返回false
        return false;
    }

    /**
     * 根据枚举值获取其对应的名字
     *
     * @param enums 枚举列表
     * @param value 枚举值
     * @param <T>   枚举值类型
     * @return 枚举名称
     */
    public static <T> String getNameByValue(EnumValueLabel<T>[] enums, T value) {
        if (value == null) {
            return null;
        }
        for (EnumValueLabel<T> e : enums) {
            if (value.equals(e.getValue())) {
                return e.getLabel();
            }
        }
        return null;
    }

    /**
     * 根据枚举名称获取对应的枚举值
     *
     * @param enums 枚举列表
     * @param name  枚举名称
     * @param <T>   枚举值类型
     * @return 枚举值
     */
    public static <T> T getValueByName(EnumValueLabel<T>[] enums, String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        for (EnumValueLabel<T> e : enums) {
            if (name.equals(e.getLabel())) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * 根据枚举值获取对应的枚举对象
     *
     * @param enums 枚举列表
     * @param value 枚举值
     * @param <E>   枚举对象类型
     * @param <T>   枚举值类型
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<? extends EnumValue<T>>, T> E getEnumByValue(E[] enums, T value) {
        if (CollectionUtil.isEmpty(enums)) {
            return null;
        }
        if (null == value) {
            return null;
        }
        for (E e : enums) {
            if (((EnumValue<T>) e).getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据枚举值获取对应的枚举对象
     *
     * @param enumClass 枚举class
     * @return 枚举对象
     */
    public static <E extends Enum<? extends EnumValue<T>>, T> E getEnumByValue(Class<E> enumClass, T value) {
        return null == enumClass ? null : getEnumByValue(enumClass.getEnumConstants(), value);
    }
}
