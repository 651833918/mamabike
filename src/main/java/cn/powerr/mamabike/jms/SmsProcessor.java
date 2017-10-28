package cn.powerr.mamabike.jms;

import cn.powerr.mamabike.sms.SmsSender;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component(value = "smsProcessor")
public class SmsProcessor {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    @Qualifier(value = "verCodeService")
    private SmsSender smsSender;

    public void sendSmsToQueue(Destination destination, final String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @JmsListener(destination = "sms.queue")
    public void doSendSmsMessages(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        smsSender.sendSms(jsonObject.getString("mobile"), jsonObject.getString("tplId"), jsonObject.getString("vercode"));
    }
}
