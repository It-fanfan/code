package com.test;

import com.code.protocols.AbstractRequest;
import com.code.protocols.angling.AnglingResultExt;
import com.code.protocols.goods.Shop;
import com.code.protocols.login.Init;
import com.code.protocols.login.UserBasic;
import com.code.protocols.login.other.Logon;
import com.utils.XwhTool;
import com.utils.http.XwhHttp;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ServletTest
{
    static String host = "http://192.168.1.183:8980/FishExitServer";

    public ServletTest()
    {

    }

    private void sendThread()
    {
        long userId = ThreadLocalRandom.current().nextLong(123453);
        String url = "/test/threadSafe";
        Init.RequestImpl req = new Init.RequestImpl();
        req.serverver = String.valueOf(userId);
        String json = sendPost(host, url, req.serverver);
        System.out.println(json);
    }


    public static void main(String[] args)
    {
        try
        {
            ServletTest servletTest = new ServletTest();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);
            for (int i = 0; i < 20; i++)
            {
                scheduler.scheduleWithFixedDelay(new Thread(servletTest::sendThread), 0, 1, TimeUnit.MILLISECONDS);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String sendPost(String host, String uri, String send)
    {
        String post = null;
        try
        {
            URL url = new URL(host + uri);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setConnectTimeout(60000);
            httpConn.setReadTimeout(30000);

            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "text/xml");
            httpConn.setRequestProperty("token", uri);
            httpConn.setRequestProperty("Charset", "utf-8");
            if (send != null)
            {
                byte[] buffer = send.getBytes("utf-8");
                httpConn.setRequestProperty("Content-length", String.valueOf(buffer.length));
                OutputStream out = new DataOutputStream(httpConn.getOutputStream());
                out.write(buffer);
                out.flush();
                out.close();
            }
            XwhHttp.ResponseCode responseCode = new XwhHttp.ResponseCode();
            responseCode.code = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode.code)
            {
                byte[] readBytes = new byte[4 * 1024];
                int readed = 0;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((readed = inStream.read(readBytes)) != -1)
                {
                    byteStream.write(readBytes, 0, readed);
                }
                byte[] result = byteStream.toByteArray();
                post = new String(result, "utf-8");
                byteStream.close();
                inStream.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return post;
    }


}
