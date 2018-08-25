package cn.powerr.mamabike.user.service;

import cn.powerr.mamabike.cache.CommonCacheUtil;
import cn.powerr.mamabike.common.constant.Constants;
import cn.powerr.mamabike.common.exception.MaMaBikeException;
import cn.powerr.mamabike.common.response.CodeMsg;
import cn.powerr.mamabike.common.util.RandomNumber;
import cn.powerr.mamabike.jms.MqConfig;
import cn.powerr.mamabike.jms.SmsProcessor;
import cn.powerr.mamabike.security.AESUtil;
import cn.powerr.mamabike.security.Base64Util;
import cn.powerr.mamabike.security.MD5Util;
import cn.powerr.mamabike.security.RSAUtil;
import cn.powerr.mamabike.user.dao.UserMapper;
import cn.powerr.mamabike.user.entity.User;
import cn.powerr.mamabike.user.entity.UserElement;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"ALL", "AlibabaClassMustHaveAuthor"})
@Slf4j
@Service(value = "userServiceImpl")
/**
 *@author Xue
 *@date 2017/10/15 19:29
 *@description
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonCacheUtil cacheUtil;
    /**
     * Jms消息队列
     */
    @Autowired
    private SmsProcessor smsProcessor;

    private static final String VERIFIYCODE_PREFIX = "verify.code.";
    private static final String SMS_QUEUE = "sms.queue";

    @Override
    public String login(String data, String key) {
        String token = null;
        String decryptData = null;
        try {
            //解密AES的密文
            byte[] aesKey = new byte[0];
            aesKey = RSAUtil.decryptByPrivateKey(Base64Util.decode(key));
            //解密加密后的data
            decryptData = AESUtil.decrypt(data, new String(aesKey, "UTF-8"));
            if (decryptData == null) {
                throw new Exception();
            }
            JSONObject jsonObject = JSON.parseObject(decryptData);
            String mobile = jsonObject.getString("mobile");
            String code = jsonObject.getString("code");
            String platform = jsonObject.getString("platform");
            String channelId = jsonObject.getString("channelId");
            if (mobile == null || code == null || StringUtils.isBlank(platform) || StringUtils.isBlank(channelId)) {
                throw new Exception();
            }
            //取redis的验证码 手机号码做Key,验证码做value 匹配成功说明是本人手机
            String verCode = cacheUtil.getCache(VERIFIYCODE_PREFIX + mobile);
            User user;
            if (code.equals(verCode)) {
                //手机验证码匹配
                user = userMapper.selectByMobile(mobile);
                if (user == null) {
                    user = new User();
                    user.setMobile(mobile);
                    user.setNickname(mobile);
                    userMapper.insertSelective(user);
                }
            } else {
                throw new MaMaBikeException(CodeMsg.VERCODE_VALIDATE_ERROR);
            }
            //生成Token
            token = generateToken(user);
            UserElement ue = new UserElement();
            ue.setMobile(mobile);
            ue.setUserId(user.getId());
            ue.setToken(token);
            ue.setPlatform(platform);
            ue.setPushChannelId(channelId);
            cacheUtil.putTokenWhenLogin(ue);
        } catch (Exception e) {
            throw new MaMaBikeException(CodeMsg.LOGIN_ERROR);
        }
        return token;
    }

    @Override
    public void modifyNickname(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * result 1 当前验证码未过期 60秒内
     * result 2 当前手机号超过验证码次数 10
     * result 3 当前ip超过验证码次数 10
     *
     * @param mobile
     * @param ip
     */
    @Override
    public void sendVercode(String mobile, String ip) {
        String verCode = RandomNumber.verCode();
        int result = cacheUtil.cacheForVerificationCode(VERIFIYCODE_PREFIX + mobile, verCode, "reg", 60, ip);
        if (result == 1) {
            log.info("当前验证码未过期,请稍后重试");
            throw new MaMaBikeException(CodeMsg.VERCODE_IN_EXPIRE);
        } else if (result == 2) {
            log.info("超过当日验证码次数");
            throw new MaMaBikeException(CodeMsg.VERCODE_DAY_SIZE);
        } else if (result == 3) {
            log.info("超过当日验证码次数{}", ip);
            throw new MaMaBikeException(CodeMsg.IP_DAY_SIZE);
        }
        log.info("Sending verify code {} for phone {}", verCode, mobile);
        //校验通过 发送短信
        Map<String, String> smsParam = new HashMap<>();
        smsParam.put("mobile", mobile);
        smsParam.put("tplId", Constants.MDSMS_VERCODE_TPLID);
        smsParam.put("vercode", verCode);
        String message = JSON.toJSONString(smsParam);
        smsProcessor.sendSmsToQueue(MqConfig.VERFICODE_QUEUE, message);
    }

    /**
     * 获取token
     *
     * @param user
     * @return
     */
    private String generateToken(User user) {
        String source = user.getId() + ":" + user.getMobile() + ":" + System.currentTimeMillis();
        return MD5Util.getMD5(source);
    }
}
