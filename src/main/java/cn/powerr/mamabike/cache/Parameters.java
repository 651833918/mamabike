package cn.powerr.mamabike.cache;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Parameters {
    /*redis config start*/
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.max-idle}")
    private int redisMaxIdle;
    @Value("${redis.max-total}")
    private int redisMaxTotal;
    @Value("${redis.max-wait-millis}")
    private int redisMaxWaitMillis;
    /*redis config end*/
}
