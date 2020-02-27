package com.utils.http;

import com.utils.EmptyX509TrustManager;
import com.utils.XwhTool;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Objects;

public class XwhHttp
{

    /**
     * 进行一次GET类型的 HTTP 连接的方法
     *
     * @param urlParam 链接参数
     */
    public static String sendGet(String urlParam) throws Exception
    {

        byte[] resultByte = httpConnect(urlParam, "GET", null);
        if (resultByte != null)
        {
            return new String(resultByte, "utf-8");
        }
        return null;

    }

    /**
     * 返回结果值
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static String sendHttps(String xml, String interface_url, String p12, String password) throws Exception
    {
        try
        {
            URL url = new URL(interface_url);
            TrustManager[] trust = new TrustManager[]{new EmptyX509TrustManager()};
            KeyManager[] tm = createKeyMager(p12, password);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(tm, trust, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setSSLSocketFactory(ssf);
            httpsConn.setDoOutput(true);
            httpsConn.setDoInput(true);
            httpsConn.setUseCaches(false);
            httpsConn.setRequestMethod("POST");
            httpsConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            OutputStream outputStream = httpsConn.getOutputStream();
            // 注意编码格式
            outputStream.write(xml.getBytes("UTF-8"));
            outputStream.close();
            InputStream ins = httpsConn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            do
            {
                byte[] temp = new byte[1024];
                int bufferLength = ins.read(temp);
                if (bufferLength < 0)
                {
                    break;
                }
                bos.write(temp, 0, bufferLength);
            } while (true);

            ins.close();
            httpsConn.disconnect();

            byte[] getXml = bos.toByteArray();
            return new String(getXml, "utf-8");
        } catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * 设置KeyMager
     *
     * @param p12      p12
     * @param password password
     * @return
     */
    private static KeyManager[] createKeyMager(String p12, String password)
    {
        try
        {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(p12), password.toCharArray());
            KeyManagerFactory trustManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ks, password.toCharArray());
            return trustManagerFactory.getKeyManagers();
        } catch (Exception e) {
        }
        return null;
    }

    public static String sendPostByStr(String url, String data, String charset) throws UnsupportedEncodingException {
        byte[] sendByte = data.getBytes(StandardCharsets.UTF_8);
        ResponseCode res = makeHttpConnect(url, "close", "application/octet-stream", "POST", charset, sendByte, 60000, 30000);
        if (res != null) {
            return new String(res.outBytes, charset);
        }
        return null;
    }

    /**
     * json格式发送POST请求
     *
     * @param url     访问地址
     * @param param   JSON对象
     * @param charset 解析编码格式
     * @return
     */
    public static String sendPostByGson(String url, Object param, String charset) throws Exception
    {
        byte[] sendByte = XwhTool.getGsonValue(param).getBytes(charset);
        byte[] resultByte = Objects.requireNonNull(makeHttpConnect(url, "close", "application/octet-stream", "POST", charset, sendByte, 60000, 30000)).outBytes;
        if (resultByte != null)
        {
            return new String(resultByte, charset);
        }
        return null;
    }

    /**
     * json格式发送POST请求
     *
     * @param url   访问地址
     * @param param JSON对象
     * @return
     */
    public static String sendPostByGson(String url, Object param) throws Exception
    {
        return sendPostByGson(url, param, "UTF-8");
    }

    /**
     * 进行设置GSON数据
     *
     * @param url     地址
     * @param param   参数
     * @param charset 格式编号
     * @return
     * @throws Exception
     */
    public static String sendPostByGson(String url, String param, String charset) throws Exception
    {
        byte[] sendByte = param.getBytes(charset);
        byte[] resultByte = Objects.requireNonNull(makeHttpConnect(url, null, "application/json", "POST", charset, sendByte, 60000, 30000)).outBytes;
        if (resultByte != null)
        {
            return new String(resultByte, charset);
        }
        return null;
    }

    /**
     * 操作一次HTTP链接
     *
     * @param urlParam       访问链接地址
     * @param connection     当次链接状态，HTTP1.1默认为保持（Keep-Alive），设置Connection: Close关闭
     * @param contentType    [type]/[subtype]，后面的文档属于什么MIME类型
     * @param method         当次访问访问类型
     * @param charset        编号格式
     * @param buffer         访问参数
     * @param connectTimeout 链接超时时间
     * @param readTimeout    读取超时时间
     * @return
     */
    public static ResponseCode makeHttpConnect(String urlParam, String connection, String contentType, String method, String charset, byte[] buffer, int connectTimeout, int readTimeout)
    {
        try
        {
            URL url = new URL(urlParam);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            if (connectTimeout > 0)
            {
                httpConn.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0)
            {
                httpConn.setReadTimeout(readTimeout);
            }

            httpConn.setRequestMethod(method);
            if (contentType != null)
            {
                httpConn.setRequestProperty("Content-Type", contentType);
            }
            if (connection != null)
            {
                httpConn.setRequestProperty("Connection", connection);
            }

            httpConn.setRequestProperty("Charset", charset);
            sendData(buffer, httpConn);
            ResponseCode responseCode = new ResponseCode();
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
                responseCode.outBytes = byteStream.toByteArray();
                byteStream.close();
                inStream.close();
            }
            return responseCode;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下发数据
     *
     * @param buffer   下发数据
     * @param httpConn 当前http链接
     * @throws IOException io exception
     */
    private static void sendData(byte[] buffer, HttpURLConnection httpConn) throws IOException
    {
        if (buffer != null)
        {
            httpConn.setRequestProperty("Content-length", String.valueOf(buffer.length));
            OutputStream out = new DataOutputStream(httpConn.getOutputStream());
            out.write(buffer);
            out.flush();
            out.close();
        }
    }

    /**
     * 发起一次网络交互
     *
     * @param urlParam 链接地址
     * @param method   交互方式POST/GET
     * @param buffer   发送数据
     * @return 回调结果数据
     * @throws IOException
     */
    private static byte[] httpConnect(String urlParam, String method, byte[] buffer) throws IOException
    {
        URL url = new URL(urlParam);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setConnectTimeout(60000);
        httpConn.setReadTimeout(60000);
        httpConn.setRequestMethod(method);
        httpConn.setRequestProperty("Charset", "utf-8");
        sendData(buffer, httpConn);
        int responseCode = httpConn.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode)
        {
            byte[] readBytes = new byte[4 * 1024];
            int readed;
            InputStream inStream = httpConn.getInputStream();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            while (-1 != (readed = inStream.read(readBytes)))
            {
                byteStream.write(readBytes, 0, readed);
            }
            byte[] b = byteStream.toByteArray();
            byteStream.close();
            inStream.close();
            return b;
        }
        return null;
    }

    /**
     * 获取ip地址
     */
    public static String getIP(HttpServletRequest request)
    {
        String ipaddr = request.getHeader("x-forwarded-for");
        if (ipaddr == null || ipaddr.equals("unknown"))
        {
            ipaddr = request.getHeader("Proxy-Client-IP");
        }
        if (ipaddr == null || ipaddr.equals("unknown"))
        {
            ipaddr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipaddr == null || ipaddr.equals("unknown"))
        {
            ipaddr = request.getRemoteAddr();
        }
        return ipaddr;
    }

    /**
     * 發送post請求
     *
     * @param host 域名
     * @param uri  uri
     * @param send 數據
     * @return 返回節點
     */
    public static String sendPost(String host, String uri, String send)
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
            httpConn.setRequestProperty("Content-Type", "application/json");
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
            ResponseCode responseCode = new ResponseCode();
            responseCode.code = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode.code)
            {
                byte[] readBytes = new byte[4 * 1024];
                int read;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((read = inStream.read(readBytes)) != -1)
                {
                    byteStream.write(readBytes, 0, read);
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

    /**
     * 返回结果
     *
     * @author Sky
     */
    public static class ResponseCode
    {
        // 接收结果状态码
        public int code;
        // 接收数据
        public byte[] outBytes;
    }
}
