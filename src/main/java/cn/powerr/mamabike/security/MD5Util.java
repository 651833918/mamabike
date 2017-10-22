package cn.powerr.mamabike.security;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String getMD5(String source){
        return DigestUtils.md5Hex(source);
    }
}
