package com.gilgamesh.common.utils;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description String 工具类
 * @createDate 2024/5/2 15:56
 * @since 1.0.0
 */
public class StringUtil {
    private StringUtil() {
    }

    /**
     * 判断字符串是否是空字符串
     * @param str String
     * @return True-是, False-否
     */
    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

    /**
     * 判断字符串是否是非空字符串
     * @param str String
     * @return True-是, False-否
     */
    public static boolean isNotEmpty(String str) {return !isEmpty(str);}
}
