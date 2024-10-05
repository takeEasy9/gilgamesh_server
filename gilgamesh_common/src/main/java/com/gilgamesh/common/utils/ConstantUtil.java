package com.gilgamesh.common.utils;

import java.util.regex.Pattern;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 常量工具类
 * @createDate 2024/5/2 15:52
 * @since 1.0.0
 */
public class ConstantUtil {
    private ConstantUtil() {
    }

    /**
     * ********************************  常用时间格式处理  *************************************
     */
    public static final String DATE_TIME_FORMAT_GENERAL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_FULL_NORMAL_WITHOUT_HYPHEN = "yyyyMMdd HH:mm:ss";
    public static final String DATE_FORMAT_GENERAL = "yyyy-MM-dd";
    public static final String TIME_FORMAT_GENERAL = "HH:mm:ss";
    public static final String SPECIAL_CHARACTER_INSTANT_T = "T";
    public static final String SPECIAL_CHARACTER_INSTANT_Z = "Z";

    // 空字符串
    public static final String STRING_EMPTY = "";

    public static final String DEFAULT_VERSION = "1.0";

    public static final String API_PREFIX = "/api/gilgamesh/";

    public static final String API_VERSION_PLACEHOLDER = "{version}";

    public static final Pattern API_VERSION_PREFIX_PATTERN = Pattern.compile(API_PREFIX + "v(\\d+\\.\\d+)");


}
