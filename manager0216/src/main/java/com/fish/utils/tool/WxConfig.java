package com.fish.utils.tool;

import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class WxConfig
{
    // 微信证书信息
    public final static String P12;
    // 微信证书密码
    public final static String PASSWORD;
    // 商户号
    public final static String MCHID;
    // 设备号 ,选填
    public final static String DEVICE_INFO;
    // 校验用户姓名选项 NO_CHECK：不校验真实姓名
    // ,FORCE_CHECK：强校验真实姓名,OPTION_CHECK：针对已实名认证的用户才校验真实姓名
    public final static CheckNameType CHECK_NAME;
    // 企业付款描述信息
    public final static String DESC;
    // 调用接口的机器IP地址
    public final static String SPBILL_CREATE_IP;
    // 微信KEY值
    public final static String KEY;
    // 支付到零钱接口
    public final static String TRANSFERS_URL;
    //统一下单接口
    public final static String UNIFIEDORDER_URL;
    //订单查询接口
    public final static String ORDERQUERY_URL;
    // file config
    private static final String CONFIG_FILE_NAME = "weixin.properties";

    private static Logger LOG = LoggerFactory.getLogger(WxConfig.class);

    static
    {
        InputStream in = WxConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
        Properties properties = new Properties();
        try
        {
            properties.load(in);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        P12 = properties.getProperty("pkcs12");
        PASSWORD = properties.getProperty("p12password");
        TRANSFERS_URL = properties.getProperty("transfers_url");
        UNIFIEDORDER_URL = properties.getProperty("unifiedorder_url");
        ORDERQUERY_URL = properties.getProperty("orderquery_url");
        MCHID = properties.getProperty("mchid");
        DEVICE_INFO = properties.getProperty("device_info");
        CHECK_NAME = CheckNameType.valueOf(properties.getProperty("check_name"));
        DESC = properties.getProperty("desc");
        SPBILL_CREATE_IP = properties.getProperty("spbill_create_ip");
        KEY = properties.getProperty("key");
        try
        {
            in.close();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    // 校验用户姓名选项
    public enum CheckNameType
    {
        NO_CHECK, // 不校验真实姓名
        FORCE_CHECK, // 强校验真实姓名
        OPTION_CHECK// 针对已实名认证的用户才校验真实姓名
    }
}
