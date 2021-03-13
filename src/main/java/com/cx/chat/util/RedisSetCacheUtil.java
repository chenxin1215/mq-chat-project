package com.cx.chat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: chenxin
 * @Date: 2020/11/11
 */
@Component
public class RedisSetCacheUtil {

    /**
     * slf4j 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    /**
     * 功能描述: 添加set元素
     *
     * @Author: chenxin
     * @Param: [key, values]
     * @Date: 2020/11/14
     */
    public boolean pushSet(String key, Collection<Long> values, long time) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            opsForSet.add(key, values.toArray(new Long[] {}));

            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }

        return false;
    }

    /**
     * 功能描述: 从set取出元素
     *
     * @Author: chenxin
     * @Date: 2020/11/14
     */
    public List<Long> popSet(String key, int count) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return opsForSet.pop(key, count);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return new ArrayList<>();
    }

    /**
     * 功能描述: 删除set元素
     *
     * @Author: chenxin
     * @Param: [key, values]
     * @Date: 2020/11/14
     */
    public Long removeSet(String key, Collection<Long> values) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return opsForSet.remove(key, values.toArray());
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }

        return null;
    }

    /**
     * 功能描述: 获取set中所有的的成员
     *
     * @Author: chenxin
     * @Param: [key]
     * @Date: 2020/11/16
     */
    public Set<Long> getMembers(String key) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return opsForSet.members(key);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return new HashSet<>();
    }

    /**
     * 功能描述: 返回set元素数量
     *
     * @Author: chenxin
     * @Param: [key, values]
     * @Date: 2020/11/14
     */
    public Long getSetCount(String key) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();
            return opsForSet.size(key);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return null;
    }

    /**
     * 功能描述: 查询Set成员是否存在
     *
     * @Author: chenxin
     * @Param: [key, v]
     * @Date: 2020/11/16
     */
    public boolean isMember(String key, Long v) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return Optional.ofNullable(opsForSet.isMember(key, v)).orElse(false);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return false;
    }

    /**
     * 功能描述: 获取差集
     *
     * @Author: chenxin
     * @Param: [key, otherKey]
     * @Date: 2020/11/16
     */
    public Set<Long> getDifferenceSet(String key, Collection<String> otherKeys) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return opsForSet.difference(key, otherKeys);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return new HashSet<>();
    }

    /**
     * 功能描述: 获取交集
     *
     * @Author: chenxin
     * @Param: [key, otherKeys]
     * @Date: 2020/11/16
     */
    public Set<Long> getIntersectSet(String key, Collection<String> otherKeys) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return opsForSet.intersect(key, otherKeys);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return new HashSet<>();
    }

    /**
     * 功能描述: 获取并集
     *
     * @Author: chenxin
     * @Param: [key, otherKeys]
     * @Date: 2020/11/16
     */
    public Set<Long> getUnionSet(String key, Collection<String> otherKeys) {
        try {
            SetOperations<String, Long> opsForSet = redisTemplate.opsForSet();

            return opsForSet.union(key, otherKeys);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return new HashSet<>();
    }

}
