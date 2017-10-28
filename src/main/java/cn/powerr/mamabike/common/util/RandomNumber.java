package cn.powerr.mamabike.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class RandomNumber {
    public static String verCode() {
        Random random = new Random();
        return StringUtils.substring(String.valueOf(random.nextInt()), 2, 6);
    }

}
