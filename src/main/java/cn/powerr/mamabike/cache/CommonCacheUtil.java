package cn.powerr.mamabike.cache;

import cn.powerr.mamabike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Map;

@Component
@Slf4j
public class CommonCacheUtil {
    @Autowired
    private JedisPoolWrapper jedisPoolWrapper;

    private static final String TOKEN_PREFIX = "token.";

    private static final String USER_PREFIX = "user.";

    /**
     * 缓存 存value永久
     *
     * @param key
     * @param value
     */
    public void cache(String key, String value) {
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    jedis.set(key, value);
                }
            }
        } catch (Exception e) {
            log.error("fail to cache value", e);
        }
    }

    /**
     * 获取缓存的value
     *
     * @param key
     * @return
     */
    public String getCache(String key) {
        String value = null;
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    value = jedis.get(key);
                }
            }
        } catch (Exception e) {
            log.error("fail to get cached value", e);
        }
        return value;
    }

    /**
     * 设置key,value以及过期时间
     *
     * @param key
     * @param value
     * @param expiry
     * @return
     */
    public long cacheNxExpire(String key, String value, int expiry) {
        long result = 0;
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    result = jedis.setnx(key, value);
                    jedis.expire(key, expiry);
                }
            }
        } catch (Exception e) {
            log.error("fail to cacheNx value", e);
        }
        return result;
    }

    /**
     * 删除缓存key
     *
     * @param key
     */
    public void delKey(String key) {
        JedisPool pool = jedisPoolWrapper.getJedisPool();
        if (pool != null) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(0);
                try {
                    jedis.del(key);
                } catch (Exception e) {
                    log.error("fail to remove key from redis", e);
                }
            }
        }
    }

    /**
     * 登录时设置token
     *
     * @param ue
     */
    public void putTokenWhenLogin(UserElement ue) {
        JedisPool pool = jedisPoolWrapper.getJedisPool();
        if (pool != null) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(0);
                Transaction trans = jedis.multi();
                try {
                    trans.del(TOKEN_PREFIX + ue.getToken());
                    trans.hmset(TOKEN_PREFIX + ue.getToken(), ue.toMap());
                    trans.expire(TOKEN_PREFIX + ue.getToken(), 2592000);
                    trans.sadd(USER_PREFIX + ue.getUserId(), ue.getToken());
                    trans.exec();
                } catch (Exception e) {
                    trans.discard();
                    log.error("fail to cache token to redis", e);
                }
            }
        }
    }

    public UserElement getUserByToken(String token) {
        UserElement ue = new UserElement();
        JedisPool pool = jedisPoolWrapper.getJedisPool();
        if (pool != null) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(0);
                try {
                    Map<String, String> map = jedis.hgetAll(TOKEN_PREFIX + token);
                    if (!CollectionUtils.isEmpty(map)) {
                        ue = UserElement.fromMap(map);
                    } else {
                        log.warn("fail to find cached element  for token");
                    }
                } catch (Exception e) {
                    log.error("Fail to get user by token in redis", e);
                    throw e;
                }
            }
        }
        return ue;
    }
}
