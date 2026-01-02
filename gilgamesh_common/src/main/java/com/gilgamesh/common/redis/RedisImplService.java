package com.gilgamesh.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description redis service
 * @createDate 2024/5/2 2:15
 * @since 1.0.0
 */
@Service
public class RedisImplService implements RedisService {
    private final Logger logger = LoggerFactory.getLogger(RedisImplService.class);

    /**
     * 操作 key value 均为对象的情形
     */
    private final RedisTemplate<Object, Object> redisTemplate;
    /**
     * 操作 key value 均为字符串的场景
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * lua 脚本加载器
     * }
     */
    private final RedisLuaScriptLoader redisLuaScriptLoader;


    @Autowired
    public RedisImplService(RedisTemplate<Object, Object> redisTemplate, StringRedisTemplate stringRedisTemplate, RedisLuaScriptLoader redisLuaScriptLoader) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisLuaScriptLoader = redisLuaScriptLoader;
    }

    /**
     * 检测缓存中是否具有该 key
     *
     * @param key Object
     * @return boolean True-key存在 False-key不存在
     */
    @Override
    public boolean hasKey(Object key) {
        if (key == null) {
            logger.error("待查询缓存的 key 为空,无法判断key在缓存中是否存在");
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 通过 key 获取缓存中存储的值
     *
     * @param key Object
     * @return Object
     */
    @Override
    public Object getValue(Object key) {
        if (key == null) {
            logger.error("待查询缓存的 key 为空,无法完成查询");
            return false;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 通过 key value 新增或更新缓存 (缓存永不过期)
     *
     * @param key   Object
     * @param value Object
     * @return boolean True-新增或更新成功 False-新增或更新失败
     */
    @Override
    public boolean setValue(Object key, Object value) {
        if (key == null || value == null) {
            logger.error("待新增或更新的缓存信息为空,无法完成操作!");
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("缓存新增或更新失败,原因是 <{}>", String.valueOf(e));
            return false;
        }
    }

    /**
     * 通过 keys 删除缓存值
     *
     * @param keys Object,可以传一个值 或多个
     * @return boolean True-删除成功 False-删除失败
     */
    @Override
    public boolean deleteKey(Object... keys) {
        if (keys == null || keys.length == 0) {
            logger.error("没有删除任何缓存的 key 集合, 无法完成缓存删除");
            return false;
        }
        try {
            int deleteCount = 0;
            for (Object k : keys) {
                if (deleteKey(k)) {
                    deleteCount++;
                }
            }
            if (deleteCount == keys.length) {
                return true;
            } else {
                logger.error("缓存删除失败,应该删除 <{}> 个,实际删除了 <{}> 个", keys.length, deleteCount);
                return false;
            }
        } catch (Exception e) {
            logger.error("缓存key集合 <{}> 删除失败,原因是 <{}>", keys, String.valueOf(e));
            return false;
        }
    }

    /**
     * 通过 key 删除缓存值
     *
     * @param key Object
     * @return boolean True-删除成功 False-删除失败
     */
    @Override
    public boolean deleteKey(Object key) {
        if (key == null) {
            logger.error("没有删除任何缓存的 key, 无法完成缓存删除");
            return false;
        }
        if (hasKey(key)) {
            try {
                redisTemplate.delete(key);
                return true;
            } catch (Exception e) {
                logger.error("缓存 <{}> 删除失败,原因是 <{}>", key, String.valueOf(e));
                return false;
            }
        } else {
            logger.error("待删除的缓存 <{}> 不存在", key);
            return false;
        }
    }

    /**
     * 通过 key value 新增或更新缓存
     *
     * @param key   Object,key
     * @param value Object,value
     * @param time  long,缓存时间(秒),如果 time <=0 将设置为缓存永不过期
     * @return boolean True-新增或更新成功 False-新增或更新失败
     */
    @Override
    public boolean setValue(Object key, Object value, long time) {
        if (key == null || value == null) {
            logger.error("待新增或更新的缓存(附加缓存过期时间)信息为空,无法完成操作!");
            return false;
        }
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("缓存(附加缓存过期时间)新增或更新失败,原因是 <{}>", String.valueOf(e));
            return false;
        }
    }

    @Override
    public Set<Object> getAllKey() {
        return redisTemplate.keys("*");
    }

    /**
     * 设置缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒) 当 time <= 0 时,缓存默认为永不过期
     * @return boolean True-设置成功 False-设置失败
     */
    @Override
    public boolean expire(Object key, long time) {
        try {
            if (key == null) {
                logger.error("待设置缓存过期时间的 key 为空,无法设置!");
                return false;
            } else {
                if (time > 0) {
                    redisTemplate.expire(key, time, TimeUnit.SECONDS);
                }
                return true;
            }
        } catch (Exception e) {
            logger.error("key <{}> 设置缓存过期时间失败,原因是 <{}>", key, String.valueOf(e));
            return false;
        }
    }

    /**
     * 获取某key的缓存过期时间
     *
     * @param key 键
     * @return long
     */
    @Override
    public Long getExpire(Object key) {
        if (key == null) {
            logger.error("查询缓存过期时间的 key 为空,无法设置!");
            return -1L;
        }
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("获取 key <{}> 的缓存过期时间失败,原因是 <{}>", key, String.valueOf(e));
            return -1L;
        }
    }

    /**
     * 向Set中添加元素, 已存在则忽略，保证去重
     *
     * @param key   Object
     * @param value Object
     * @return addToSet
     */
    @Override
    public boolean addToSet(Object key, Object value) {
        if (key == null) {
            logger.error("向Set中添加元素 key 为空,无法添加!");
            return false;
        }
        try {
            // SADD：向Set中添加元素(已存在则忽略，保证去重)
            Long result = redisTemplate.opsForSet().add(key, value);
            // result > 0 表示新增成功，result = 0 表示已存在(仍返回成功)
            return result != null;
        } catch (Exception e) {
            logger.error("向Set中添加元素 key <{}> 失败,原因是 <{}>", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从Set中删除指定元素, 不存在则忽略
     *
     * @param key   Object
     * @param value Object
     * @return removeFromSet
     */
    @Override
    public boolean removeFromSet(Object key, Object value) {
        if (key == null) {
            logger.error("从Set中删除元素 key 为空,无法删除!");
            return false;
        }
        try {
            // SREM：从Set中删除指定元素（不存在则忽略）
            Long result = redisTemplate.opsForSet().remove(key, value);
            // result > 0 表示删除成功，result = 0 表示不存在(仍返回成功)
            return result != null;
        } catch (Exception e) {
            logger.error("从 key 为 <{}>  Set中删除素失败,原因是 <{}>", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取Set中的所有元素, 为空则返回空集合
     *
     * @param key Object
     * @return Set<Object>
     */
    @Override
    public Set<Object> getMembersFromSet(Object key) {
        if (key == null) {
            logger.error("获取Set中所有元素 key 为空,无法获取!");
            return Set.of();
        }
        try {
            // SMEMBERS：获取Set中的所有元素（为空则返回空集合）
            Set<Object> set = redisTemplate.opsForSet().members(key);
            // 避免返回null，统一返回空集合
            return set != null ? set : Set.of();
        } catch (Exception e) {
            logger.error("获取Set中所有元素 key <{}> 失败,原因是 <{}>", key, e.getMessage(), e);
            return Set.of();
        }
    }

    @Override
    public boolean hmset(Object key, Map<Object, Object> hash) {
        // 参数校验
        if (key == null) {
            logger.error("批量写入Hash的key为空,无法完成操作!");
            return false;
        }
        if (hash == null || hash.isEmpty()) {
            logger.warn("批量写入Hash的字段映射为空,无需执行写入操作");
            return true; // 空映射视为操作成功（避免无意义的异常）
        }

        try {
            redisTemplate.opsForHash().putAll(key, hash);
            logger.debug("Hash key <{}> 批量写入成功,字段数:{}", key, hash.size());
            return true;
        } catch (Exception e) {
            logger.error("Hash key <{}> 批量写入失败,原因是 <{}>", key, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Map<Object, Object> hgetAll(Object key) {
        // 参数校验
        if (key == null) {
            logger.error("获取Hash所有字段的key为空,无法完成操作!");
            return new HashMap<>();
        }

        try {
            // 避免返回null，统一返回空Map
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            logger.error("获取Hash key <{}> 所有字段失败,原因是 <{}>", key, e.getMessage(), e);
            return new HashMap<>();
        }
    }

    @Override
    public boolean hset(Object key, Object field, Object value) {
        // 参数校验
        if (key == null || field == null) {
            logger.error("写入Hash的key或字段为空,无法完成操作!");
            return false;
        }
        if (value == null) {
            logger.warn("写入Hash的字段 <{}> 值为空,仍执行写入操作", field);
        }

        try {
            redisTemplate.opsForHash().put(key, field, value);
            logger.debug("Hash key <{}> 字段 <{}> 写入成功", key, field);
            return true;
        } catch (Exception e) {
            logger.error("Hash key <{}> 字段 <{}> 写入失败,原因是 <{}>", key, field, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Object hget(Object key, Object field) {
        // 参数校验
        if (key == null || field == null) {
            logger.error("获取Hash字段的key或字段为空,无法完成操作!");
            return null;
        }

        try {
            return redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            logger.error("获取Hash key <{}> 字段 <{}> 失败,原因是 <{}>", key, field, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean hdel(Object key, Object... fields) {
        // 参数校验
        if (key == null) {
            logger.error("删除Hash字段的key为空,无法完成操作!");
            return false;
        }
        if (fields == null || fields.length == 0) {
            logger.warn("删除Hash字段的字段列表为空,无需执行删除操作");
            return true; // 空字段视为操作成功
        }

        try {
            Long deleteCount = redisTemplate.opsForHash().delete(key, fields);
            logger.debug("Hash key <{}> 删除字段数:{}", key, deleteCount);
            return true;
        } catch (Exception e) {
            logger.error("Hash key <{}> 删除字段 <{}> 失败,原因是 <{}>", key, Arrays.toString(fields), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行字符串形式的 Lua 脚本（自动封装为 DefaultRedisScript）
     */
    @Override
    public Long execute(String luaScript, List<String> keys, Object... args) {
        // 封装 Lua 脚本为 DefaultRedisScript 对象
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);

        // 调用重载方法执行
        return execute(redisScript, keys, args);
    }

    /**
     * 执行预编译的 Lua 脚本（性能更优）
     */
    @Override
    public Long execute(RedisScript<Long> redisScript, List<String> keys, Object... args) {
        // 空值校验
        if (redisScript == null) {
            throw new IllegalArgumentException("Lua 脚本对象不能为空");
        }
        if (keys == null) {
            keys = List.of(); // 空 key 列表
        }
        if (args == null) {
            args = new Object[]{}; // 空参数列表
        }
        // 执行 Lua 脚本（核心逻辑）
        return stringRedisTemplate.execute(redisScript, keys, args);
    }

    /**
     * 按SHA1执行Lua脚本, 同步执行Lua脚本（基于EVALSHA，兼容单机/集群）
     */
    @Override
    public Long executeBySha1(String scriptKey, List<String> keys, Object... args) {
        // 1. 参数校验
        if (scriptKey == null || scriptKey.isBlank()) {
            throw new IllegalArgumentException("脚本标识不能为空");
        }
        List<String> finalKeys = Optional.ofNullable(keys).orElse(List.of());
        Object[] finalArgs = Optional.ofNullable(args).orElse(new Object[0]);

        // 2. 获取SHA1
        String sha1 = redisLuaScriptLoader.getScriptSha1(scriptKey);

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(redisLuaScriptLoader.getScriptSha1(scriptKey), Long.class) {
            @Override
            public String getSha1() {
                return sha1;
            }
        };
        try {
            // 3. 执行脚本
            return stringRedisTemplate.execute(redisScript, finalKeys, finalArgs);
        } catch (Exception e) {
            if (e.getCause() instanceof InterruptedException) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("执行Lua脚本被中断", e);
            } else if (e.getCause() instanceof TimeoutException) {
                throw new RuntimeException("执行Lua脚本超时（5秒）", e);
            } else {
                throw new RuntimeException("执行Lua脚本失败", e);
            }
        }
    }

    /**
     * 获取List中的最后一个元素, 为空则返回null
     * 底层使用LRANGE key -1 -1（原子操作，获取最后1个元素）
     *
     * @param key Object
     * @return Object 最后一个元素（null表示无元素/获取失败）
     */
    @Override
    public Object getLastOfList(Object key) {
        if (key == null) {
            logger.error("获取List最后一个元素 key 为空,无法获取!");
            return null;
        }
        try {
            // LRANGE key -1 -1：获取List最后一个元素（返回List，取第一个元素）
            List<Object> lastElementList = redisTemplate.opsForList().range(key, -1, -1);
            if (lastElementList == null || lastElementList.isEmpty()) {
                return null;
            }
            return lastElementList.getFirst();
        } catch (Exception e) {
            logger.error("获取List最后一个元素 key <{}> 失败,原因是 <{}>", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取List中的第一个元素, 为空则返回null
     * 底层使用LRANGE key 0 0（原子操作，获取第一个元素）
     *
     * @param key Object
     * @return Object 第一个元素（null表示无元素/获取失败）
     */
    @Override
    public Object getFirstOfList(Object key) {
        if (key == null) {
            logger.error("获取List第一个元素 key 为空,无法获取!");
            return null;
        }
        try {
            // LRANGE key 0 0：获取List第一个元素（返回List，取第一个元素）
            List<Object> firstElementList = redisTemplate.opsForList().range(key, 0, 0);
            if (firstElementList == null || firstElementList.isEmpty()) {
                return null;
            }
            return firstElementList.getFirst();
        } catch (Exception e) {
            logger.error("获取List第一个元素 key <{}> 失败,原因是 <{}>", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取List中的所有元素, 为空则返回空列表
     * 底层使用LRANGE key 0 -1（获取所有元素）
     *
     * @param key Object
     * @return List<Object> 所有元素（空列表表示无元素/获取失败）
     */
    @Override
    public List<Object> getAllList(Object key) {
        if (key == null) {
            logger.error("获取List所有元素 key 为空,无法获取!");
            return Collections.emptyList();
        }
        try {
            // LRANGE key 0 -1：获取List所有元素（为空则返回空List）
            List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
            // 避免返回null，统一返回空列表
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            logger.error("获取List所有元素 key <{}> 失败,原因是 <{}>", key, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
