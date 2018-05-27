package cn.powerr.mamabike.sms;

import cn.powerr.mamabike.common.constant.Constants;
import cn.powerr.mamabike.common.util.HttpUtil;
import cn.powerr.mamabike.security.MD5Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service(value = "verCodeService")
@Slf4j
public class MiaoDiSmsSender implements SmsSender {

    private static String operation = "/industrySMS/sendSMS";
    @Override
    public void sendSms(String phone, String tplId, String params) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sdf.format(new Date());
            String sig = MD5Util.getMD5(Constants.MDSMS_ACCOUNT_SID +Constants.MDSMS_AUTH_TOKEN +timestamp);
            String url = Constants.MDSMS_REST_URL +operation;
            Map<String,String> param = new HashMap<>();
            param.put("accountSid",Constants.MDSMS_ACCOUNT_SID);
            param.put("to",phone);
            param.put("templateid",tplId);
            param.put("param",params);
            param.put("timestamp",timestamp);
            param.put("sig",sig);
            param.put("respDataType","json");
            String result = HttpUtil.post(url,param);
            JSONObject jsonObject = JSON.parseObject(result);
            if(!"00000".equals(jsonObject.getString("respCode"))){
                log.error("fail to send sms to "+phone+":"+params+":"+result);
            }
        } catch (Exception e) {
            log.error("fail to send sms to "+phone+":"+params);
        }
    }
}
