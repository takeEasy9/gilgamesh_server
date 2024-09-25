package com.gilgamesh.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    public RedisImplService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 检测缓存中是否具有该 key
     *
     * @param key Object
     * @return boolean True-key存在 False-key不存在
     */
    @Override
    public boolean hasKey(Object key) {
        if (null == key) {
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
        if (null == key) {
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
        if (null == key || null == value) {
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
        if (null == keys || keys.length == 0) {
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
        if (null == key) {
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
        if (null == key || null == value) {
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
            if (null == key) {
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
        if (null == key) {
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
}
