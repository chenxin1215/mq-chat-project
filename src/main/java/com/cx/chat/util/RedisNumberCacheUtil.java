package com.cx.chat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: chenxin
 * @Date: 2020/11/11
 */
@Component
public class RedisNumberCacheUtil {

    /**
     * slf4j 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * 添加 key:string 缓存
     *
     * @param k key
     * @param v value
     * @param time time
     */
    public boolean cacheValue(String k, Integer v, long time) {
        try {

            ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
            ops.set(k, v);
            if (time > 0) {
                redisTemplate.expire(k, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable e) {
            LOGGER.error("缓存存入失败key:[{}] value:[{}]", k, v);
        }
        return false;
    }

    /**
     * 添加 key:string 缓存
     *
     * @param key key
     * @param value value
     */
    public boolean cacheValue(String key, Integer value) {
        return cacheValue(key, value, -1);
    }

    /**
     * 查询缓存 key 是否存在
     * 
     * @param key key
     * @return true/false
     */
    public Boolean containsKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable e) {
            LOGGER.error("判断缓存存在失败key:[" + key + "],错误信息 [{}]", e);
        }
        return false;
    }

    /**
     * 根据 key 获取缓存value
     *
     * @param key key
     * @return value
     */
    public Integer getValue(String key) {
        try {
            ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
            return ops.get(key);
        } catch (Throwable e) {
            LOGGER.error("根据 key 获取缓存失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return null;
    }

    /**
     * 根据 key 移除 value 缓存
     *
     * @param key key
     * @return true/false
     */
    public boolean removeKey(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable e) {
            LOGGER.error("移除缓存失败 key:[{}] 失败原因 [{}]", key, e);
        }
        return false;
    }

    /**
     * 功能描述: 自增（原子操作）
     *
     * @Author: chenxin
     * @Param: [key]
     * @Date: 2020/11/11
     */
    public boolean increment(String key) {
        try {
            ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
            ops.increment(key, 1);
            return true;
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败 ，当前key:[{}],失败原因:[{}]", key, e);
        }
        return false;
    }

    /**
     * 功能描述: value值操作（原子操作）
     *
     * @Author: chenxin
     * @Param: [key]
     * @Date: 2020/11/11
     */
    public boolean increment(String key, long delta) {
        try {
            ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
            ops.increment(key, delta);
            return true;
        } catch (Throwable e) {
            LOGGER.error("根据 key 获取缓存失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return false;
    }

    /**
     * 功能描述: 设置过期时间 单位：秒
     *
     * @Author: chenxin
     * @Param: [key, timeout]
     * @Date: 2020/11/11
     */
    public boolean expire(String key, long timeout) {
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 功能描述: 获取过期时间 单位：秒
     *
     * @Author: chenxin
     * @Param: [key, timeout]
     * @Date: 2020/11/11
     */
    public Long getExpire(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        }
        return 0L;
    }
}
