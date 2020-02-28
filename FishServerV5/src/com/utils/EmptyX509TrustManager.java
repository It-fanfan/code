package com.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * X509信任方法-默认
 *
 * @author Host-0222
 */
public class EmptyX509TrustManager implements X509TrustManager
{
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
    {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
    {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }
}
