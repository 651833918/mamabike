package cn.powerr.mamabike.common.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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

    //获取security无需认证的页面
    @Value("#{'${security.noneSecurityPath}'.split(',')}")
    private List<String> noneSecurityPath;
}
