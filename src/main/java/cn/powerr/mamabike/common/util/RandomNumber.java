package cn.powerr.mamabike.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
/**
 *@author Xue
 *@date 2018/5/27 16:13
 *@description  随机验证码发送
 */
public class RandomNumber {

    public static String verCode() {
        Random random = new Random();
        return StringUtils.substring(String.valueOf(random.nextInt()), 2, 6);
    }

}
