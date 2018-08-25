package cn.powerr.mamabike.user.entity;

import lombok.Data;

/**
 * @author bengang
 * @date@time 2018/8/25 8:57
 * @description 用于接收加密数据的bean
 */
@Data
public class TransferInfo {
    //AES加密的data.
    private String data;
    //RSA加密的AES密文
    private String key;
}
