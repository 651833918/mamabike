package cn.powerr.mamabike.user.entity;

import lombok.Data;

@Data
public class LoginInfo {
    //AES加密的data.
    private String data;
    //RSA加密的AES密文
    private String key;
}
