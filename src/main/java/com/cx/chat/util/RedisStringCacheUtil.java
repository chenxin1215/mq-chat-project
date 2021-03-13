package com.cx.chat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
public class RedisStringCacheUtil {

    /**
     * slf4j 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 添加 key:string 缓存
     *
     * @param k key
     * @param v value
     * @param time time
     */
    public boolean cacheValue(String k, String v, long time) {
        try {

            ValueOperations<String, String> ops = redisTemplate.opsForValue();
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
    public boolean cacheValue(String key, String value) {
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
    public String getValue(String key) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            return ops.get(key);
        } catch (Throwable e) {
            LOGGER.error("根据 key 获取缓存失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return null;
    }

    /**
     * 根据 key 获取缓存value
     *
     * @param keys key
     * @return value
     */
    public List<String> getValueList(Collection<String> keys) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            return ops.multiGet(keys);
        } catch (Throwable e) {
            LOGGER.error("根据 key 获取缓存失败，当前key:[{}],失败原因 :[{}]", keys, e);
        }
        return null;
    }

    /**
     * 根据 key 获取总条数 用于分页
     * 
     * @param key key
     * @return 条数
     */
    public Long getListSize(String key) {
        try {
            ListOperations<String, String> opsForList = redisTemplate.opsForList();
            return opsForList.size(key);
        } catch (Throwable e) {
            LOGGER.error("获取list长度失败key[" + key + "], [" + e + "]");
        }
        return 0L;
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
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
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
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
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

    /**
     * 功能描述: 添加set元素
     *
     * @Author: chenxin
     * @Param: [key, values]
     * @Date: 2020/11/14
     */
    public boolean pushSet(String key, Collection<String> values, long time) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

            opsForSet.add(key, values.toArray(new String[] {}));

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
     * @Param: []
     * @Date: 2020/11/14
     */
    public List<String> popSet(String key, int count) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

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
    public Long removeSet(String key, Collection<String> values) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

            return opsForSet.remove(key, values);
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
    public Set<String> getMembers(String key) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

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
        SetOperations<String, String> opsForSet = redisTemplate.opsForSet();
        return opsForSet.size(key);
    }

    /**
     * 功能描述: 查询Set成员是否存在
     *
     * @Author: chenxin
     * @Param: [key, v]
     * @Date: 2020/11/16
     */
    public boolean isMember(String key, String v) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

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
    public Set<String> getDifferenceSet(String key, Collection<String> otherKeys) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

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
    public Set<String> getIntersectSet(String key, Collection<String> otherKeys) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

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
    public Set<String> getUnionSet(String key, Collection<String> otherKeys) {
        try {
            SetOperations<String, String> opsForSet = redisTemplate.opsForSet();

            return opsForSet.union(key, otherKeys);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", key, e);
        }
        return new HashSet<>();
    }

    /**
     * 功能描述: 往集合中新增数据(从尾部)
     *
     * @Author: chenxin
     * @Param: [k, vs]
     * @Date: 2020/11/14
     */
    public Long rightPushList(String k, List<String> vs) {
        try {
            ListOperations<String, String> opsForList = redisTemplate.opsForList();
            return opsForList.rightPushAll(k, vs);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", k, e);
        }
        return null;
    }

    /**
     * 功能描述: 从集合中取出一个元素（从头部）
     *
     * @Author: chenxin
     * @Param: [k, vs]
     * @Date: 2020/11/14
     */
    public String leftPop(String k) {
        try {
            ListOperations<String, String> opsForList = redisTemplate.opsForList();
            return opsForList.leftPop(k);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", k, e);
        }
        return null;
    }

    /**
     * 功能描述: 从集合中删除
     *
     * @Author: chenxin
     * @Param: [k, count 删除的数量, v]
     * @Date: 2020/11/14
     */
    public void removeListValue(String k, int count, String v) {
        try {
            ListOperations<String, String> opsForList = redisTemplate.opsForList();
            opsForList.remove(k, count, v);
        } catch (Throwable e) {
            LOGGER.error("缓存操作失败，当前key:[{}],失败原因 :[{}]", k, e);
        }
    }

    /**
     * 功能描述: 获取集合所有的元素
     *
     * @Author: chenxin
     * @Param: [key]
     * @Date: 2020/11/14
     */
    public List<String> getCachedListAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

}
