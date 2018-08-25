package cn.powerr.mamabike.jms;

import cn.powerr.mamabike.sms.SmsSender;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "smsProcessor")
public class SmsProcessor {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    @Qualifier(value = "verCodeService")
    private SmsSender smsSender;

    /**
     * 发送message到destination
     * @param queue
     * @param message
     */
    public void sendSmsToQueue(String queue, final String message) {
        amqpTemplate.convertAndSend(queue, message);
    }

    @RabbitListener(queues = MqConfig.VERFICODE_QUEUE)
    public void doSendSmsMessages(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        smsSender.sendSms(jsonObject.getString("mobile"), jsonObject.getString("tplId"), jsonObject.getString("vercode"));
    }
}
