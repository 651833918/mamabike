package cn.powerr.mamabike.cache;

import cn.powerr.mamabike.common.exception.MaMaBikeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class JedisPoolWrapper {
    private JedisPool jedisPool = null;

    @Autowired
    private Parameters parameters;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @PostConstruct
    public void init() throws MaMaBikeException {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(parameters.getRedisMaxIdle());
            config.setMaxTotal(parameters.getRedisMaxTotal());
            config.setMaxWaitMillis(parameters.getRedisMaxWaitMillis());

            jedisPool = new JedisPool(config, parameters.getRedisHost(), parameters.getRedisPort(), 2000);
        } catch (Exception e) {
            log.error("fail to initialize jedis pool", e);
            throw new MaMaBikeException("fail to initialize jedis pool");
        }
    }
}
