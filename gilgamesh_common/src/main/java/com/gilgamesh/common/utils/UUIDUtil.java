package com.gilgamesh.common.utils;

import java.util.UUID;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description UUID工具类
 * @createDate 2025/1/23 14:13
 * @since 1.0.0
 */
public class UUIDUtil {
    private UUIDUtil() {
    }

    /**
     * 生成UUId
     *
     * @return String
     */
    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成连字符的UUId
     *
     * @return String
     */
    public static String genUUIDWithoutHyphen() {
        return UUID.randomUUID().toString()
                .replace(ConstantUtil.SPECIAL_CHARACTER_HYPHEN, ConstantUtil.SPECIAL_CHARACTER_EMPTY);
    }
}
