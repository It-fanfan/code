package com.fish.utils.http;

import com.fish.utils.EmptyX509TrustManager;
import com.fish.utils.XwhTool;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.*;

public class XwhHttp {
    public static void main(String[] arg) {
//        try {
//            String send = "https://pay.vivo.com.cn/vivopay/order/request?cpOrderNumber=ypg_1571813030645&orderAmount=1.00&orderDesc=10000%20贝壳&orderTime=20191023144350&orderTitle=10000%20贝壳&packageName=com.blazefire.fish.vivominigame&version=1.2.0&78312275292f89151624230a57f61cdb&signature=66136e0c5fe8a39f6c63655ce1636229&signMethod=MD5";
//            String send ="{\"orderTitle\":\"a\",\"respCode\":\"200\",\"signMethod\":\"MD5\",\"appId\":\"100002714\",\"orderAmount\":\"1.00\",\"orderDesc\":\"b\",\"vivoOrderNumber\":\"157173991638032403031\",\"respMsg\":\"success\",\"vivoSignature\":\"aa20a8529024471ac1d67486e3274079\",\"signature\":\"a251e539060d71f2f6b5b2f1dcabee20\"}\n"
//            String send ="https://pay.vivo.com.cn/vivopay/order/request?cpOrderNumber=1571059181066&orderAmount=1.00&orderDesc=b&orderTime=20191014211941&orderTitle=a&packageName=com.blazefire.fish.vivominigame&version=1.2.0&78312275292f89151624230a57f61cdb&signature=bd4004a0cba7233bacac2f5e8f0eaeb0&signMethod=MD5";
//            String json = "{\"orderTime\":\"20191023145632\",\"orderAmount\":\"1.00\",\"signature\":\"60f62856d99a4bcb75a2f8348a175edf\",\"packageName\":\"com.blazefire.fish.vivominigame\",\"cpOrderNumber\":\"mik_1571813792028\",\"orderTitle\":\"10000 贝壳\",\"version\":\"1.2.0\",\"orderDesc\":\"10000 贝壳\",\"signMethod\":\"MD5\"}";
//
//            JSONObject jsonObject = JSONObject.parseObject(json);
//            String context = doPostForm("https://pay.vivo.com.cn/vivopay/order/request", jsonObject.getInnerMap());
//            System.out.println(context);
//        } catch (Exception r) {
//            r.printStackTrace();
//        }

    }

    /**
     * 进行一次GET类型的 HTTP 连接的方法
     *
     * @param urlParam 链接参数
     */
    public static String sendGet(String urlParam) throws Exception {

        byte[] resultByte = httpConnect(urlParam, "GET", null);
        if (resultByte != null) {
            return new String(resultByte, StandardCharsets.UTF_8);
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
    public static String sendHttps(String xml, String interface_url, String p12, String password) throws Exception {
        try {
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
            do {
                byte[] temp = new byte[1024];
                int bufferLength = ins.read(temp);
                if (bufferLength < 0) {
                    break;
                }
                bos.write(temp, 0, bufferLength);
            } while (true);

            ins.close();
            httpsConn.disconnect();

            byte[] getXml = bos.toByteArray();
            return new String(getXml, "utf-8");
        } catch (Exception e) {
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
    private static KeyManager[] createKeyMager(String p12, String password) {
        try {
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
    public static String sendPostByGson(String url, Object param, String charset) throws Exception {
        byte[] sendByte = XwhTool.getGsonValue(param).getBytes(charset);
        byte[] resultByte = Objects.requireNonNull(makeHttpConnect(url, "close", "application/octet-stream", "POST", charset, sendByte, 60000, 30000)).outBytes;
        if (resultByte != null) {
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
    public static String sendPostByGson(String url, Object param) throws Exception {
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
    public static String sendPostByGson(String url, String param, String charset) throws Exception {
        byte[] sendByte = param.getBytes(charset);
        byte[] resultByte = Objects.requireNonNull(makeHttpConnect(url, null, "application/json", "POST", charset, sendByte, 60000, 30000)).outBytes;
        if (resultByte != null) {
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
    public static ResponseCode makeHttpConnect(String urlParam, String connection, String contentType, String method, String charset, byte[] buffer, int connectTimeout, int readTimeout) {
        try {
            URL url = new URL(urlParam);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            if (connectTimeout > 0) {
                httpConn.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                httpConn.setReadTimeout(readTimeout);
            }

            httpConn.setRequestMethod(method);
            if (contentType != null) {
                httpConn.setRequestProperty("Content-Type", contentType);
            }
            if (connection != null) {
                httpConn.setRequestProperty("Connection", connection);
            }

            httpConn.setRequestProperty("Charset", charset);
            sendData(buffer, httpConn);
            ResponseCode responseCode = new ResponseCode();
            responseCode.code = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode.code) {
                byte[] readBytes = new byte[4 * 1024];
                int readed = 0;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((readed = inStream.read(readBytes)) != -1) {
                    byteStream.write(readBytes, 0, readed);
                }
                responseCode.outBytes = byteStream.toByteArray();
                byteStream.close();
                inStream.close();
            }
            return responseCode;
        } catch (IOException e) {
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
    private static void sendData(byte[] buffer, HttpURLConnection httpConn) throws IOException {
        if (buffer != null) {
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
    private static byte[] httpConnect(String urlParam, String method, byte[] buffer) throws IOException {
        URL url = new URL(urlParam);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setConnectTimeout(60000);
        httpConn.setReadTimeout(60000);
        httpConn.setRequestMethod(method);
        httpConn.setRequestProperty("Content-Type", "text/html");
        httpConn.setRequestProperty("Charset", "utf-8");
        int responseCode = httpConn.getResponseCode();
        sendData(buffer, httpConn);
        if (HttpURLConnection.HTTP_OK == responseCode) {
            byte[] readBytes = new byte[4 * 1024];
            int readed;
            InputStream inStream = httpConn.getInputStream();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            while (-1 != (readed = inStream.read(readBytes))) {
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
    public static String getIP(HttpServletRequest request) {
        String ipaddr = request.getHeader("x-forwarded-for");
        if (ipaddr == null || ipaddr.equals("unknown")) {
            ipaddr = request.getHeader("Proxy-Client-IP");
        }
        if (ipaddr == null || ipaddr.equals("unknown")) {
            ipaddr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipaddr == null || ipaddr.equals("unknown")) {
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
    public static String sendPost(String host, String uri, String send) {
        String post = null;
        try {
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
            if (send != null) {
                byte[] buffer = send.getBytes("utf-8");
                httpConn.setRequestProperty("Content-length", String.valueOf(buffer.length));
                OutputStream out = new DataOutputStream(httpConn.getOutputStream());
                out.write(buffer);
                out.flush();
                out.close();
            }
            ResponseCode responseCode = new ResponseCode();
            responseCode.code = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode.code) {
                byte[] readBytes = new byte[4 * 1024];
                int read;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((read = inStream.read(readBytes)) != -1) {
                    byteStream.write(readBytes, 0, read);
                }
                byte[] result = byteStream.toByteArray();
                post = new String(result, "utf-8");
                byteStream.close();
                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * 返回结果
     *
     * @author Sky
     */
    public static class ResponseCode {
        // 接收结果状态码
        public int code;
        // 接收数据
        public byte[] outBytes;
    }


    /**
     * @param httpUrl 请求的url
     * @param param   form表单的参数（key,value形式）
     * @return
     */
    public static String doPostForm(String httpUrl, Map param) {
//        System.out.println(HttpURLConnection.class.getName());
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
            String str = createLinkString(param);
            byte[] sendBytes = str.getBytes(StandardCharsets.UTF_8);
            //System.out.println("字符集数字大小:" + sendBytes.length + ",测试数据集:" + Charset.defaultCharset());
            os.write(sendBytes);
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sbf = new StringBuilder();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            Objects.requireNonNull(connection).disconnect();
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, Object> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key).toString();
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }

        return prestr.toString();
    }
}
