package com.gilgamesh.common.utils;

import com.github.f4b6a3.uuid.UuidCreator;

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
    public static String genUuidV7() {
        return UuidCreator.getTimeOrderedEpochPlus1().toString();
    }

    /**
     * 生成连字符的UUId
     *
     * @return String
     */
    public static String genUuidV7WithoutHyphen() {
        return UuidCreator.getTimeOrderedEpochPlus1().toString()
                .replace(ConstantUtil.SPECIAL_CHARACTER_HYPHEN, ConstantUtil.SPECIAL_CHARACTER_EMPTY);
    }

    public static void main(String[] args) {
        System.out.println(genUuidV7WithoutHyphen());
    }
}
