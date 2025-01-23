package com.gilgamesh.common.enums;

import com.gilgamesh.common.utils.CollectionUtil;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description enum value interface
 * @createDate 2024/5/2 15:34
 * @since 1.0.0
 */
public interface EnumValue<T> {

    /**
     * 添加枚举值
     *
     * @param value T 枚举值
     */
    default void addEnumValue(T value) {
        EnumValue<T> self = self();
        EnumValueState.enumValueMap.put(self, value);
        Map<Object, EnumValue<?>> valueLabelEnums = EnumValueState.value2EnumMap.computeIfAbsent(self.getClass(),
                key -> new HashMap<>());
        valueLabelEnums.put(value, self);
    }


    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    @SuppressWarnings("unchecked")
    default T getValue() {
        return (T) EnumValueState.enumValueMap.get(self());
    }

    /**
     * 获取枚举对象
     *
     * @return EnumValue<T>
     */
    default EnumValue<T> self() {
        return this;
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
    static <E extends Enum<? extends EnumValue<T>>, T> E getEnumByValue(E[] enums, T value) {
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
    @Nullable
    @SuppressWarnings("unchecked")
    static <E extends Enum<? extends EnumValue<T>>, T> E getEnumByValue(Class<E> enumClass, T value) {
        if (null == enumClass || null == value) {
            return null;
        }
        Map<Object, EnumValue<?>> valueEnumMap = EnumValueState.value2EnumMap.get(enumClass);
        if (valueEnumMap == null) {
            synchronized (EnumValueState.value2EnumMap) {
                valueEnumMap = EnumValueState.value2EnumMap.get(enumClass);
                if (valueEnumMap == null) {
                    // 保证初始化
                    try {
                        enumClass.getMethod("values").invoke(null);
                        valueEnumMap = EnumValueState.value2EnumMap.get(enumClass);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }

            }
        }
        assert valueEnumMap != null;
        return (E) valueEnumMap.get(value);
    }

    /**
     * 判断枚举值是否存在于指定枚举数组中
     *
     * @param enums 枚举数组
     * @param value 枚举值
     * @param <T>   枚举值类型
     * @return True-枚举值存在 False-枚举值不存在
     */
    static <T> boolean isExist(EnumValue<T>[] enums, T value) {
        if (CollectionUtil.isEmpty(enums) || null == value) {
            return false;
        }
        return Stream.of(enums).map(EnumValue::getValue).collect(Collectors.toSet()).contains(value);
    }

    /**
     * 判断枚举值是否不存在于指定枚举数组中
     *
     * @param enums 枚举数组
     * @param value 枚举值
     * @param <T>   枚举值类型
     * @return True-枚举值不存在 False-枚举值存在
     */
    static <T> boolean isNotExist(EnumValue<T>[] enums, T value) {
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
    static <E extends Enum<? extends EnumValue<T>>, T> boolean isExist(Class<E> enumClass, T value) {
        return getEnumByValue(enumClass, value) != null;
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
    static <E extends Enum<? extends EnumValue<T>>, T> boolean isNotExist(Class<E> enumClass, T value) {
        return !isExist(enumClass, value);
    }
}

class EnumValueState {
    static final ConcurrentHashMap<EnumValue<?>, Object> enumValueMap = new ConcurrentHashMap<>();

    static final ConcurrentHashMap<Class<?>, Map<Object, EnumValue<?>>> value2EnumMap = new ConcurrentHashMap<>();

    private EnumValueState() {
    }

}
