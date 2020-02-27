package com.code.service.pay;

import com.code.protocols.sdk.Huawei;
import com.utils.ReadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class HuaweiService
{
    private static final Logger LOG = LoggerFactory.getLogger(HuaweiService.class);

    /**
     * 将商户id，appId,requestId,商品名称，商品说明，支付金额，渠道号，回调地址版本号等必须参与参与签名的字段信息按照key值升序排列后
     * 以key=value并以&的方式连接起来生成待签名的字符串，将生成的代签名字符串使用开发者联盟网站提供的应用支付私钥
     * 进行签名
     *
     * @return 签名
     */
    public static String getSign(Huawei.Pay request)
    {

        Map<String, Object> params = new HashMap<>();
        //Request为客户端传入过来的需要进行参数签名的子串，均为String类型。
        params.put("merchantId", request.merchantId);
        params.put("applicationID", request.applicationID);
        params.put("amount", request.amount);
        params.put("productName", request.productName);
        params.put("productDesc", request.productDesc);
        params.put("requestId", request.requestId);
        params.put("sdkChannel", request.sdkChannel);
        //注意：这里的key值为urlver ,v是小写。
        if (request.urlver != null)
            params.put("urlver", request.urlver);
        //对参数按照key做升序排序，对map的所有value进行处理，转化成string类型
        //拼接成key=value&key=value&....格式的字符串
        StringBuilder content = new StringBuilder();
        // 按照key做排序
        List<String> keys = new ArrayList<>(params.keySet());
        keys.sort(Comparator.naturalOrder());
        String value;
        Object object;
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            object = params.get(key);
            if (object instanceof String)
            {
                value = (String) object;
            } else
            {
                value = String.valueOf(object);
            }
            if (value != null)
            {
                content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
            }
        }
        //带签名的字符串
        String signOri = content.toString();
        return rsaSign(signOri);
    }

    /**
     * 使用开发者联盟网站分配的支付私钥对支付信息进行签名
     * 强烈建议在商户服务端做签名处理，且私钥存储在服务端，防止信息泄露
     * CP通过服务器获取服务器端的签名之后，再进行支付请求
     */
    private static String rsaSign(String content)
    {
        if (null == content)
        {
            return null;
        }

        //开发者联盟网站申请的支付私钥，请妥善保管，最好存储在服务器端
        String privateKey = ReadConfig.get("huawei.privateKey");
        //使用加密算法规则
        String SIGN_ALGORITHMS = "SHA256WithRSA";
        String charset = "UTF-8";
        try
        {
            //Base64请采用服务器端标准的Base64算法库。
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            byte[] signed = signature.sign();
            //Base64请采用服务器端标准的Base64算法库。
            return new BASE64Encoder().encode(signed);
        } catch (NoSuchAlgorithmException e)
        {
            LOG.error("sign NoSuchAlgorithmException");
        } catch (InvalidKeySpecException e)
        {
            LOG.error("sign InvalidKeySpecException");
        } catch (InvalidKeyException e)
        {
            LOG.error("sign InvalidKeyException");
        } catch (SignatureException e)
        {
            LOG.error("sign SignatureException");
        } catch (UnsupportedEncodingException e)
        {
            LOG.error("sign UnsupportedEncodingException");
        } catch (IOException e)
        {
            LOG.error("sign IOException");
        }
        return null;
    }

    /**
     * 校验签名信息
     *
     * @param content 步骤1中获取的JSON字符串
     * @param sign    签名字符串
     * @return 是否校验通过
     */
    public static boolean doCheck(String content, String sign)
    {
        try
        {
            String publicKey = ReadConfig.get("huawei.publicKey");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = new BASE64Decoder().decodeBuffer(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            //开发者联盟网站申请的支付私钥，请妥善保管，最好存储在服务器端
            //使用加密算法规则
            String SIGN_ALGORITHMS = "SHA256WithRSA";
            //Base64请采用服务器端标准的Base64算法库。
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(new BASE64Decoder().decodeBuffer(sign));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
