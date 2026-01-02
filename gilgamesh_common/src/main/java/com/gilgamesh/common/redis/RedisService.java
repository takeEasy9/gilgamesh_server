package com.gilgamesh.common.redis;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Map;
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

    /**
     * 向Set中添加元素, 已存在则忽略，保证去重
     *
     * @param key   Object
     * @param value Object
     * @return addToSet
     */
    boolean addToSet(final Object key, Object value);

    /**
     * 从Set中删除指定元素, 不存在则忽略
     *
     * @param key   Object
     * @param value Object
     * @return removeFromSet
     */
    boolean removeFromSet(final Object key, Object value);

    /**
     * 获取Set中的所有元素, 为空则返回空集合
     *
     * @param key Object
     * @return Set<Object>
     */
    Set<Object> getMembersFromSet(final Object key);

    /**
     * 批量写入 Hash 结构（原子操作）
     *
     * @param key  Hash的key
     * @param hash 字段-值映射
     * @return 是否成功
     */
    boolean hmset(Object key, Map<Object, Object> hash);

    /**
     * 获取 Hash 中所有字段和值
     *
     * @param key Hash的key
     * @return 字段-值映射（为空返回空Map）
     */
    Map<Object, Object> hgetAll(Object key);

    /**
     * 写入单个 Hash 字段
     *
     * @param key   Hash的key
     * @param field 字段名
     * @param value 字段值
     * @return 是否成功
     */
    boolean hset(Object key, Object field, Object value);

    /**
     * 获取单个 Hash 字段值
     *
     * @param key   Hash的key
     * @param field 字段名
     * @return 字段值（为空返回null）
     */
    Object hget(Object key, Object field);

    /**
     * 删除 Hash 中的指定字段
     *
     * @param key    Hash的key
     * @param fields 字段名（可传多个）
     * @return 是否成功
     */
    boolean hdel(Object key, Object... fields);

    /**
     * 执行 Lua 脚本
     *
     * @param luaScript Lua 脚本内容
     * @param keys      Redis key 列表
     * @param args      脚本参数列表
     * @return 脚本执行结果（Long 类型）
     */
    Long execute(String luaScript, List<String> keys, Object... args);

    /**
     * 重载方法：支持预编译的 RedisScript（推荐，避免重复编译）
     *
     * @param redisScript 预编译的 Lua 脚本对象
     * @param keys        Redis key 列表
     * @param args        脚本参数列表
     * @return 脚本执行结果（Long 类型）
     */
    Long execute(RedisScript<Long> redisScript, List<String> keys, Object... args);

    /**
     * 按SHA1执行Lua脚本, 同步执行Lua脚本（基于EVALSHA，兼容单机/集群）
     *
     * @param scriptKey String
     * @param keys      List<String>
     * @param args      Object[]
     * @return Long
     */
    Long executeBySha1(String scriptKey, List<String> keys, Object... args);

    /**
     * 获取List中的最后一个元素, 为空则返回null
     *
     * @param key Object
     * @return Object 最后一个元素（null表示无元素/获取失败）
     */
    Object getLastOfList(final Object key);

    /**
     * 获取List中的第一个元素, 为空则返回null
     *
     * @param key Object
     * @return Object 第一个元素（null表示无元素/获取失败）
     */
    Object getFirstOfList(final Object key);

    /**
     * 获取List中的所有元素, 为空则返回空列表
     *
     * @param key Object
     * @return List<Object> 所有元素（空列表表示无元素/获取失败）
     */
    List<Object> getAllList(final Object key);
}
