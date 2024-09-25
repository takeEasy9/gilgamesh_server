package com.gilgamesh.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 集合工具类
 * @createDate 2024/5/2 15:53
 * @since 1.0.0
 */
public class CollectionUtil {
    private CollectionUtil() {
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return True-为空 False-不为空
     */
    public static boolean isEmpty(Object[] array) {
        return null == array || array.length == 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection Collection<?>
     * @return True-为空 False-不为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 判断map是否为空
     *
     * @param map map
     * @return True-为空 False-不为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }
}
