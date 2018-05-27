package cn.powerr.mamabike.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESUtil {
    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";


    /**
     * AES对称加密
     *
     * @param data
     * @param key  key需要16位
     * @return
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES对称解密 key需要16位
     *
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        String okey = "123456789abcdefg";
        //移动端随机key  AES加密数据
        String data = encrypt("{'mobile':'15310614196','code':'4006','platform':'android'}", okey);
        System.out.println("data: "+data);
        //移动端RSA加密AES的key 和加密的数据一起传到服务器
        byte[] keyrsa = RSAUtil.encryptByPublicKey(okey.getBytes("UTF-8"), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuVRY8B3+Af5euC9WbgNkJKAiBzqOvrYi9mSST78jd4clpn7vkYHDfHzJiqFz9wjNRLzg9MUREF53bw9yhSljZ7F8JPMryfe8RR2Ed6CJq5nCy/2hvTTw4L6ypDemwe9f9yjIg52oPRPwU8lm8Uj3wKhjedDmZrkO1TAmt3sbQtwIDAQAB");
        System.out.println("key:" + Base64Util.encode(keyrsa));

        System.out.println("=====================");
        String base = Base64Util.encode(keyrsa);

       //服务端RSA解密AES的key
       byte[] keybyte= RSAUtil.decryptByPrivateKey(Base64Util.decode(base));
       String keyR=new String(keybyte,"UTF-8");
        System.out.println(keyR);
    }
    /*public static void main(String[] args) {
        String key = "123456789abcdefg";
        String xuebengang = AESUtil.encrypt("xuebengang", key);
        System.out.println(xuebengang);
        String decry = decrypt(xuebengang,key);
        System.out.println(decry);
    }*/
    /*public static void main(String[] args) throws Exception {
        //AES加密数据
        String key = "123456789abcdfgt";
        String dataToEn = "{'mobile':'18980840843','code':'6666','platform':'android'}";
        String enResult = encrypt(dataToEn, key);
        System.out.println(enResult);
        //RSA加密AES加密数据的key
        byte[] enKey = RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuVRY8B3+Af5euC9WbgNkJKAiBzqOvrYi9mSST78jd4clpn7vkYHDfHzJiqFz9wjNRLzg9MUREF53bw9yhSljZ7F8JPMryfe8RR2Ed6CJq5nCy/2hvTTw4L6ypDemwe9f9yjIg52oPRPwU8lm8Uj3wKhjedDmZrkO1TAmt3sbQtwIDAQAB");
        String baseKey = Base64Util.encode(enKey);
        System.out.print(baseKey);

    }*/
}
