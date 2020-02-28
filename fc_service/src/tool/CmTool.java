package tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Vinnes
 */
public class CmTool
{

    //
    // 十六进制字符串模板
    //
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static Logger LOG = LoggerFactory.getLogger(CmTool.class);

    /**
     * 获取文件上一次编辑时间
     *
     * @param path 路径信息
     * @return 上一次编辑时间
     */
    public static long getFileDate(String path)
    {
        File file = new File(path);

        return file.lastModified();
    }

    /**
     * 检查文件是否存在的方法
     *
     * @param path 路径信息
     * @return 是否存在
     */
    public static boolean checkFileExist(String path)
    {
        File file = new File(path);

        return file.exists();
    }

    /**
     * 从二进制到字符串的方法
     *
     * @param hexString 二进制信息
     * @return 得到的字符串信息
     */
    public static String getStringFromHex(String hexString)
    {
        try
        {
            byte[] bytes = new byte[hexString.length() / 2];

            for (int i = 0, j = 0; i < hexString.length(); i += 2, j++)
            {
                String value = "0x" + hexString.substring(i, i + 2);

                bytes[j] = Integer.decode(value).byteValue();
            }

            return new String(bytes, "gb2312");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return hexString;
    }

    /**
     * 从字符串到二进制流的方法
     *
     * @param content 源字串信息
     * @return 得到的二进制流的文本描述
     */
    public static String getStringToHex(String content)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            byte[] bytes = content.getBytes("gb2312");

            String temp = null;

            for (int i = 0; i < bytes.length; i++)
            {
                temp = Integer.toHexString(bytes[i] & 0xff);
                if (temp.length() == 1)
                {
                    sb.append("0");
                }
                sb.append(temp);
            }


        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return sb.toString();
    }

    /**
     * 获取系统毫秒级时间
     *
     * @return
     */
    public static long getMillisecondTime()
    {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * 获取选项索引信息
     */
    public static int getOptionIndex(String text)
    {
        if (null == text)
        {
            return 0;
        }
        int index = text.indexOf(" - ");
        if (index > 0)
        {
            text = text.substring(0, index);
            text = CmTool.trimExt(text);
            return CmTool.parseInt(text, 0);
        }

        return 0;
    }

    /**
     * 获取当前月份的方法
     *
     * @return 得到的第几月份
     */
    public static int getCurrentMonth()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前月份共多少天的方法
     *
     * @return 得到的天数
     */
    public static int getCurrentMonthDays()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前第几小时的方法
     *
     * @return 得到的第几小时
     */
    public static int getCurrentDayHour()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前第几分钟的方法
     *
     * @return 得到的第几分钟
     */
    public static int getCurrentHourMinute()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * 获取当前星期的方法
     *
     * @return 得到的星期几
     */
    public static int getCurrentDateWeek()
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int value = cal.get(Calendar.DAY_OF_WEEK);
        if (Calendar.SUNDAY == value)
        {
            return 6;
        }
        if (Calendar.SATURDAY == value)
        {
            return 5;
        }
        if (Calendar.FRIDAY == value)
        {
            return 4;
        }
        if (Calendar.THURSDAY == value)
        {
            return 3;
        }
        if (Calendar.WEDNESDAY == value)
        {
            return 2;
        }
        if (Calendar.TUESDAY == value)
        {
            return 1;
        }
        return 0;
    }

    /**
     * 获取当前时间格式的方法
     *
     * @return 得到的时间值
     */
    public static long getCurrentDateValue()
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);

        return Long.parseLong(temp);
    }

    /**
     * 获取当前精确时间格式的方法
     *
     * @return 得到的时间值
     */
    public static long getCurrentDateValueDetail()
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);

        return Long.parseLong(temp);
    }

    /**
     * 获取当前精确时分秒的方法 HHmmss
     *
     * @return 得到的时间值
     */
    public static int getCurrentDateValueHHmmss()
    {
        SimpleDateFormat sm = new SimpleDateFormat("HHmmss");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);

        return Integer.parseInt(temp);
    }

    /**
     * 获取一份随机单号的方法
     */
    public static String getOnceRandomOrderNo()
    {
        StringBuilder sb = new StringBuilder();
        int currentDateValue = (int) getCurrentDateValue();
        Random random = new Random();

        for (int i = 0; i < 16; i++)
        {
            int value = Math.abs((random.nextInt() + currentDateValue) % 10);
            sb.append(value);
        }

        return sb.toString();
    }

    /**
     * Date转long
     *
     * @param date
     * @return long
     */
    public static long dateToLong(Date date)
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyMMddHHmmss");
        String temp = sm.format(date);

        return Long.parseLong(temp);
    }

    /**
     * 获取当前时间格式的方法
     *
     * @return 得到的时间值
     */
    public static int getCurrentDataValueSummary()
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHH");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);

        return Integer.parseInt(temp);
    }

    /**
     * 获取当前时间格式的方法
     *
     * @return 得到的时间值
     */
    public static int getCurrentDataValueRough()
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);

        return Integer.parseInt(temp);
    }

    /**
     * 获取时间前后某个时间
     *
     * @return
     */
    public static long getDateCompute(long longDate, long diff)
    {
        try
        {
            SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = String.valueOf(longDate);
            Date date = sm.parse(dateStr);
            long time = date.getTime() + diff * 1000;
            date.setTime(time);
            String temp = sm.format(date);

            return Long.parseLong(temp);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 写入一个文本的方法
     *
     * @param path    路径信息
     * @param content 内容信息
     */
    public static void writeFileString(String path, String content)
    {
        try
        {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 读入一个文本的方法
     *
     * @param path 路径信息
     * @return 得到的文本信息
     */
    public static String readFileString(String path)
    {
        try
        {
            File file = new File(path);
            if (!file.exists())
            {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);

            int length = fis.available();
            byte[] bytes = new byte[length];
            fis.read(bytes);
            fis.close();

            return new String(bytes, "GBK");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析客户端提交过来数据
     */
    public static byte[] parseServletInputStream(HttpServletRequest request) throws Exception
    {
        ServletInputStream inStream = request.getInputStream();
        int size = request.getContentLength();
        byte[] bytes = new byte[size];
        int count = 0;
        int offset = 0;

        while (count >= 0)
        {
            count = inStream.read(bytes, offset, 4096);
            offset += count;
        }

        return bytes;
    }

    /**
     * 编码并返回下发的数据
     */
    public static void encodeServletOutputStream(HttpServletResponse responseObject, JSONObject responsePackage) throws Exception
    {
        byte[] bytes = responsePackage.toJSONString().getBytes(StandardCharsets.UTF_8);

        ServletOutputStream out = responseObject.getOutputStream();
        out.write(bytes);
        out.flush();
    }

    /**
     * 将一个字符串去皮的方法
     *
     * @param text 字符串文本信息
     * @return 去皮后的字符串
     */
    public static String trimExt(String text)
    {
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < sb.length(); i++)
        {
            char c = sb.charAt(i);
            if (c == '\n' || c == '\r')
            {
                sb.setCharAt(i, ' ');
                continue;
            }
            break;
        }
        for (int i = sb.length() - 1; i >= 0; i--)
        {
            char c = sb.charAt(i);
            if (c == '\n' || c == '\r')
            {
                sb.setCharAt(i, ' ');
                continue;
            }
            break;
        }
        String temp = sb.toString();
        return temp.trim();
    }

    /**
     * 转换字符串到整型数组参数
     */
    public static int[] parseInts(String content, String tag)
    {
        String[] contents = CmTool.cut(content, tag);
        int[] values = new int[contents.length];

        for (int i = contents.length - 1; i >= 0; i--)
        {
            try
            {
                values[i] = Integer.parseInt(contents[i]);
            } catch (Exception e)
            {
                return null;
            }
        }

        return values;
    }

    /**
     * 解析列表字符串的方法
     *
     * @param content 字符串内容信息
     * @return 得到的解析结果
     */
    public static Vector<String> parseListString(String content)
    {
        Vector<String> list = new Vector<String>();
        if (null == content)
        {
            return list;
        }
        int start = 0, end;
        String tile = null;
        boolean finishLoop = false;

        end = content.indexOf("\n");
        while (start >= 0)
        {
            if (end < 0)
            {
                finishLoop = true;
                end = content.length();
            }
            tile = content.substring(start, end);
            tile = trimExt(tile);
            if (tile.length() > 0)
            {
                list.addElement(tile);
            }

            start = end + 1;
            end = content.indexOf("\n", start);
            if (finishLoop)
            {
                break;
            }
        }

        return list;
    }

    /**
     * 解析配置字符串的方法
     *
     * @param content 字符串内容信息
     * @return 得到的解析结果
     */
    public static HashMap<String, String> parseConfigString(String content)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if (null == content)
        {
            return map;
        }
        int start = 0, end, index;
        String tile = null;
        String key = null;
        String value = null;
        boolean finishLoop = false;

        end = content.indexOf("\n");
        while (start >= 0)
        {
            if (end < 0)
            {
                finishLoop = true;
                end = content.length();
            }
            tile = content.substring(start, end);
            if (!tile.startsWith("//"))
            {
                index = tile.indexOf("=");
                if (index > 0)
                {
                    key = tile.substring(0, index);
                    value = tile.substring(index + 1, tile.length());
                    value = trimExt(value);
                    map.put(key, value);
                }
            }

            start = end + 1;
            end = content.indexOf("\n", start);
            if (finishLoop)
            {
                break;
            }
        }

        return map;
    }

    /**
     * 获取ip地址
     *
     * @param request
     * @return
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
     * 进行一次 HTTP 连接的方法
     *
     * @param urlParam 链接参数
     * @return 返回值信息
     */
    public static String makeHttpConnect(String urlParam)
    {
        String serviceUrl = urlParam;
        try
        {
            URL url = new URL(serviceUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setConnectTimeout(60000);
            httpConn.setReadTimeout(60000);
            httpConn.setRequestMethod("GET");

            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
            {
                byte[] readBytes = new byte[2 * 1024];
                int readed = 0;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((readed = inStream.read(readBytes)) != -1)
                {
                    byteStream.write(readBytes, 0, readed);
                }

                byteStream.close();
                inStream.close();

                return byteStream.toString("UTF-8");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取网络传输的 ascii 字符串字节流
     */
    public static byte[] getNetworkAsciiBytes(String info)
    {
        byte[] bytes = info.getBytes();
        byte[] lengths = CmTransfer.toLH(bytes.length);
        byte[] result = new byte[bytes.length + 4];

        System.arraycopy(lengths, 0, result, 0, 4);
        System.arraycopy(bytes, 0, result, 4, bytes.length);

        return result;
    }

    /**
     * 获取网络传输的 unicode 字符串字节流
     */
    public static byte[] getNetworkUnicodeBytes(String info)
    {
        byte[] bytes = getSimpleUnicodeBytes(info);
        byte[] lengths = CmTransfer.toLH(bytes.length);
        byte[] result = new byte[bytes.length + 4];

        System.arraycopy(lengths, 0, result, 0, 4);
        System.arraycopy(bytes, 0, result, 4, bytes.length);

        return result;
    }

    /**
     * 获取简洁的 unicode 字符串字节流
     */
    public static byte[] getSimpleUnicodeBytes(String info)
    {
        byte[] bytes = decodeUnicodeString(info);
        byte[] result = new byte[bytes.length];

        System.arraycopy(bytes, 2, result, 0, bytes.length - 2);

        return result;
    }

    /**
     * 服务器转码字符
     */

    public static byte[] decodeUnicodeString(String info)
    {
        try
        {
            return info.getBytes("Unicode");
        } catch (UnsupportedEncodingException e)
        {
            return info.getBytes();
        }
    }

    /**
     * 解码客户端提交数据
     *
     * @param bytes
     * @return
     */
    public static String decodeUnicodeBytes(byte[] bytes)
    {
        try
        {
            if (null == bytes)
            {
                return null;
            }
            return new String(bytes, "Unicode");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取异常堆栈信息
     *
     * @param e
     * @return
     */
    public static String getStackTrace(Exception e)
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(buf, true));

        return buf.toString();
    }

    /**
     * 获取一个数据输入流
     */
    public static ByteArrayInputStream getRsDataInputStream(Object data, boolean compress) throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(data);
        oos.close();
        bos.close();

        byte[] bytes = bos.toByteArray();
        if (compress)
        {
            bytes = CmZip.compress(bytes);
        }

        return new ByteArrayInputStream(bytes);
    }

    /**
     * 获取一个数据输出字节
     */
    public static Object getRsDataOutputBytes(ResultSet rs, String name, boolean compress) throws Exception
    {
        InputStream ins = rs.getBinaryStream(name);
        if (null == ins)
        {
            return null;
        }

        byte[] bytes = new byte[ins.available()];
        ins.read(bytes);
        if (compress)
        {
            bytes = CmZip.decompress(bytes);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object object = ois.readObject();
        ois.close();
        ins.close();

        return object;
    }

    /**
     * 转换一个整型的方法
     */
    public static int parseInt(String content, int exceptionValue)
    {
        try
        {
            return Integer.parseInt(content);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return exceptionValue;
    }

    /**
     * 自实现字符串切割方法，速度稍快
     */
    public static String[] cut(String source, String tag)
    {
        ArrayList<String> lists = new ArrayList<String>();
        int start, end;

        start = 0;
        end = source.indexOf(tag);
        if (end < 0)
        {
            lists.add(source);
        } else
        {
            do
            {
                lists.add(source.substring(start, end));
                start = end + 1;
                end = source.indexOf(tag, start);
                if (end < 0)
                {
                    lists.add(source.substring(start, source.length()));
                    break;
                }
            } while (true);
        }

        return lists.toArray(new String[lists.size()]);
    }

    /**
     * 对 string builder 信息进行关键字替换
     *
     * @param sb    字串信息
     * @param key   关键字信息
     * @param value 要替换的内容
     */
    public static void replaceForWord(StringBuilder sb, String key, String value)
    {
        if (null == value)
        {
            return;
        }
        int start = 0;
        int end, length;

        length = key.length();
        start = sb.indexOf(key);
        while (start >= 0)
        {
            end = start + length;

            sb.replace(start, end, value);
            start = sb.indexOf(key, start + length + 1);
        }
    }

    /**
     * 得到时间差
     *
     * @param dateFormat 时间格式
     * @param time1      旧时间
     * @param time2      新时间
     * @return 时间差 秒
     */
    public static int getTimeDeltaSecs(String dateFormat, long time1, long time2)
    {
        try
        {
            SimpleDateFormat sm = new SimpleDateFormat(dateFormat);
            String dateStr = String.valueOf(time1);
            Date date1 = sm.parse(dateStr);
            dateStr = String.valueOf(time2);
            Date date2 = sm.parse(dateStr);

            int diff = (int) ((date2.getTime() - date1.getTime()) / 1000);
            return diff;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 得到天数
     *
     * @param dateFormat
     * @param time1
     * @param time2
     * @return
     */
    public static int daysBetween(String dateFormat, long time1, long time2)
    {
        try
        {
            SimpleDateFormat sm = new SimpleDateFormat(dateFormat);
            String dateStr = String.valueOf(time1);
            Date date1 = sm.parse(dateStr);
            dateStr = String.valueOf(time2);
            Date date2 = sm.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            long mills1 = cal.getTimeInMillis();
            cal.setTime(date2);
            long mills2 = cal.getTimeInMillis();
            long between_days = (mills2 - mills1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取一个序列数据字符串
     */
    public static String gainStringByInts(int[] values)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < values.length; i++)
        {
            if (0 != i)
            {
                sb.append("#");
            }

            sb.append(values[i]);
        }

        return sb.toString();
    }

    /**
     * 随机一个字符串
     */
    public static String createNonceStr()
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 16; i++)
        {
            int index = new Random().nextInt(hexDigits.length);
            buffer.append(hexDigits[index]);
        }
        return buffer.toString();
    }

    /**
     * 进行混淆byte
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 进行byte计算
     *
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte b[])
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    /**
     * 通过字串获取对应MD5
     *
     * @param sign
     * @return
     */
    public static String getMD5Encode(String sign)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md.digest(sign.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return null;
    }

    /**
     * 设置KeyMager
     *
     * @param p12
     * @param password
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
        } catch (Exception e)
        {
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

            outputStream.write(xml.getBytes(StandardCharsets.UTF_8));
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
     * 获取JSON字串
     *
     * @param obj 对象
     * @return json
     */
    public static String getJSONByFastJSON(Object obj)
    {
        return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm");
    }

    /**
     * 解析JSON通过fastjson
     */
    public static <T> T parseJSONByFastJSON(String c, Class<T> clazz)
    {
        return JSON.parseObject(c, clazz);
    }

    public static <T> T parseJSONByFastJSON(String c, Type clazz)
    {
        return JSON.parseObject(c, clazz);
    }

    /**
     * 将json转换字符串
     */
    public static String convertJsonToStr(String json)
    {
        return json.replace("\\", "\\\\").replace("\"", "\\\"");
    }


    public static String SHA1(String decript)
    {
        try
        {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            byte[] byteArray = decript.getBytes("UTF-8");
            byte[] md5Bytes = sha.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++)
            {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
    }

    public static String SHA(String decript)
    {
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return sha(digest, decript);
    }

    private static String sha(MessageDigest digest, String decript)
    {
        digest.update(decript.getBytes());
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        // 字节数组转换为 十六进制 数
        for (byte aMessageDigest : messageDigest)
        {
            String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
            if (shaHex.length() < 2)
            {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

    /**
     * 进行时间规整
     *
     * @param time      时间
     * @param formatStr 时间
     * @return 时间
     */
    public static Timestamp parseTime(String time, String formatStr)
    {
        DateFormat format = new SimpleDateFormat(formatStr);
        try
        {
            long mins = format.parse(time).getTime();
            return new Timestamp(mins);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 进行除法运算
     *
     * @param val1  值1
     * @param val2  值2
     * @param scale 保留位数
     * @return 结果
     */
    public static BigDecimal div(long val1, long val2, int scale)
    {
        BigDecimal div = new BigDecimal(val1);
        if (val2 <= 0)
            return BigDecimal.valueOf(0);
        div = div.divide(BigDecimal.valueOf(val2), scale, RoundingMode.HALF_UP);
        return div;
    }
}
