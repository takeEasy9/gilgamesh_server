package com.gilgamesh.common.redis;

import java.util.Set;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description redis interface
 * @createDate 2024/5/2 2:11
 * @since 1.0.0
 */
public interface RedisService {
    /**
     * 检测缓存中是否具有该 key
     *
     * @param key Object
     * @return boolean True-key存在 False-key不存在
     */
    boolean hasKey(Object key);

    /**
     * 通过 key 获取缓存中存储的值
     *
     * @param key Object
     * @return Object
     */
    Object getValue(Object key);

    /**
     * 通过 key value 新增或更新缓存
     *
     * @param key   Object
     * @param value Object
     * @return boolean True-新增或更新成功 False-新增或更新失败
     */
    boolean setValue(final Object key, Object value);

    /**
     * 通过 keys 删除缓存值
     *
     * @param key Object,可以传一个值 或多个
     * @return boolean True-删除成功 False-删除失败
     */
    boolean deleteKey(Object... key);

    /**
     * 通过 key 删除缓存值
     *
     * @param key Object
     * @return boolean True-删除成功 False-删除失败
     */
    boolean deleteKey(Object key);

    /**
     * 通过 key value 新增或更新缓存
     *
     * @param key   Object,key
     * @param value Object,value
     * @param time  long,缓存时间(秒),如果 time <=0 将设置为缓存永不过期
     * @return boolean True-新增或更新成功 False-新增或更新失败
     */
    boolean setValue(final Object key, Object value, long time);

    /**
     * 获取Redis中所有key值
     *
     * @return Set<Object>
     */
    Set<Object> getAllKey();

    /* 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return boolean True-设置成功 False-设置失败
     */
    boolean expire(Object key, long time);

    /**
     * 根据 key 获取缓存过期时间
     *
     * @param key 键
     * @return long 时间(秒) 返回 0 代表为缓存永不过期
     */
    Long getExpire(Object key);
}
