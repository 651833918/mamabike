package cn.powerr.mamabike.jms;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bengang
 * @date 2018/8/25
 * @time 9:40
 * @description MQ配置
 */
@Configuration
public class MqConfig {
    public static final String VERFICODE_QUEUE = "sms.queue";

    @Bean
    public Queue queue(){
        return new Queue(VERFICODE_QUEUE,true);
    }
}
