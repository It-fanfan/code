package com.utils;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
public class MyRSAUtils {
    /**
     * ​     \* 加密算法RSA
     * ​
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * ​     \* 获取公钥的key
     * ​
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * ​     \* 获取私钥的key
     * ​
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * ​     \* <p>
     * ​     \* 生成密钥对(公钥和私钥)
     * ​     \* </p>
     * ​     *
     * ​     \* @return
     * ​     \* @throws Exception
     * ​
     */

    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return base64Encode(key.getEncoded());
    }

    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return base64Encode(key.getEncoded());
    }

    /**
     * ​     \* 用base64编码
     * ​     \* @param bytes
     * ​     \* @return
     * ​
     */
    private static String base64Encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> pairs = MyRSAUtils.genKeyPair();
        System.out.println("公钥:" + MyRSAUtils.getPublicKey(pairs));
        System.out.println("私钥:" + MyRSAUtils.getPrivateKey(pairs));

    }
}
