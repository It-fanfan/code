package com.fish.utils.tool;

import java.util.*;

/**
 * 微信签名算法
 *
 * @author Host-0222
 */
public class SignatureAlgorithm
{
    // 密钥
    private String key;
    // 需要进行设置签名信息
    private Map<String, String> signMap;
    // 生成订单签名
    private String sign;

    public SignatureAlgorithm(String key, Map<String, String> signMap)
    {
        this.key = key;
        this.signMap = signMap;
    }

    /**
     * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（
     * 即key1=value1&key2=value2…）拼接成字符串stringA。 特别注意以下重要规则： ◆
     * 参数名ASCII码从小到大排序（字典序）； ◆ 如果参数的值为空不参与签名； ◆ 参数名区分大小写； ◆
     * 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。 ◆
     * 微信接口可能增加字段，验证签名时必须支持增加的扩展字段
     */
    private String createSignStringA()
    {
        Collection<String> keyset = signMap.keySet();
        List<String> list = new ArrayList<String>(keyset);
        Collections.sort(list);
        StringBuilder builder = new StringBuilder();
        for (String key : list)
        {
            String value = signMap.get(key);
            if (key != null && !key.trim().isEmpty())
            {
                if (builder.length() > 0)
                {
                    builder.append("&");
                }
                builder.append(key).append("=").append(value.trim());
            }
        }
        return builder.toString();
    }

    /**
     * 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，
     * 再将得到的字符串所有字符转换为大写，得到sign值signValue。
     * key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
     */
    public String createSign()
    {
        String stringA = createSignStringA();
        String stringSignTemp = stringA + "&key=" + key;
        return Objects.requireNonNull(CmTool.getMD5Encode(stringSignTemp)).toUpperCase();
    }

    /**
     * 获取最终XML流信息
     *
     * @return
     */
    public String getSignXml()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<xml>\n");
        for (String key : signMap.keySet())
        {
            builder.append("<").append(key).append(">");
            builder.append(signMap.get(key));
            builder.append("</").append(key).append(">\n");
        }
        sign = createSign();
        builder.append("<sign>").append(sign).append("</sign>\n");
        builder.append("</xml>");
        return builder.toString();
    }

    public String getSign()
    {
        return sign;
    }
}
