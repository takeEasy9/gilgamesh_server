package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description enum value interface
 * @createDate 2024/5/2 15:34
 * @since 1.0.0
 */
public interface EnumValue<T> {

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    T getValue();
}
