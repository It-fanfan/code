package com.fish.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fish.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class XwhTool
{
    private static final Logger LOG = LoggerFactory.getLogger(XwhTool.class);
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 获取当前时间格式的方法
     *
     * @return 得到的时间值
     */
    public static int getCurrentDateValue()
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);
        return Integer.parseInt(temp);
    }

    /**
     * 获取当前时间格式的方法
     *
     * @return 得到的时间值
     */
    public static int getCurrentHourValue()
    {
        SimpleDateFormat sm = new SimpleDateFormat("HHmm");
        Date date = new Date(System.currentTimeMillis());
        String temp = sm.format(date);
        return Integer.parseInt(temp);
    }

    /**
     * 获取当前时间格式的方法
     *
     * @return 得到的时间值
     */
    public static int getCurrentValue()
    {
        SimpleDateFormat sm = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String temp = sm.format(date);
        return Integer.parseInt(temp);
    }

    /**
     * 获取时间格式
     *
     * @param times 日期
     * @return 数字
     */
    public static int getDateValue(Date times)
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        String temp = sm.format(times);
        return Integer.parseInt(temp);
    }

    /**
     * 获取周期数据
     *
     * @param times 日期
     * @return 周期
     */
    public static int getWeekValue(Date times)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(times);
        return calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取月份数据
     *
     * @param times 日期
     * @return 周期
     */
    public static int getMonthValue(Date times)
    {
        String month = getFormaterTime(times.getTime(), "yyyyMM");
        return Integer.valueOf(month);
    }

    /**
     * 获取时间格式
     */
    public static int getDateValue(Timestamp times)
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        String temp = sm.format(times);
        return Integer.parseInt(temp);
    }

    /**
     * 读取流数据
     */
    public static String readInputStream(InputStream in)
    {
        try
        {
            int length = in.available();
            byte[] bytes = new byte[length];
            in.read(bytes);
            in.close();
            return new String(bytes);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读入一个文本的方法
     *
     * @param path 路径信息
     * @return 得到的文本信息
     */
    public static String readFileString(String path)
    {
        File file = new File(path);
        if (!file.exists())
        {
            return null;
        }
        try
        {
            FileInputStream fis = new FileInputStream(file);
            return readInputStream(fis);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将数据写入文件
     *
     * @param path    文件路径
     * @param content 数据内容
     */
    public static String writeFile(String path, String content)
    {
        try
        {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);

            byte[] bytes = content.getBytes();

            fos.write(bytes);
            fos.close();
            return XwhTool.getMd5ByFile(file);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件md5值
     */
    public static String getMd5ByFile(File file) throws FileNotFoundException
    {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try
        {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            value = byteArrayToHexString(md5.digest());
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                in.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 通过字串获取对应MD5
     */
    public static String getMD5Encode(String sign)
    {
        try
        {
            if (sign == null)
                return null;
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md.digest(sign.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return null;
    }

    /**
     * 进行byte计算
     */
    private static String byteArrayToHexString(byte b[])
    {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b)
            resultSb.append(byteToHexString(aB));

        return resultSb.toString();
    }

    /**
     * 进行混淆byte
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

    public static String sha256_HMAC(String str, String secret) throws Exception
    {
        try
        {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(str.getBytes());
            return byteArrayToHexString(bytes);
        } catch (Exception e)
        {
            throw new Exception("Error HmacSHA256");
        }
    }

    /**
     * 获取格式化时间
     */
    public static String getFormaterTime(long time)
    {
        return getFormaterTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getFormaterTime(long time, String formatter)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatter);
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    /**
     * 获取两者之间的时间差值
     */
    public static long getTimeBetween(LocalDateTime today, LocalDateTime record, ChronoUnit chronoUnit)
    {
        return chronoUnit.between(today, record);
    }

    /**
     * 获取当前与当天差异
     *
     * @param next 比较时间
     * @return 差异天数
     */
    public static long getCurrentSubDay(Timestamp next)
    {
        LocalDate nowDate = LocalDate.now();
        LocalDate nextDate = next.toLocalDateTime().toLocalDate();
        return nowDate.toEpochDay() - nextDate.toEpochDay();
    }

    /**
     * 检测是否为相同天
     *
     * @param curr 当前时间
     * @param diff 对比时间
     * @return bool
     */
    public static boolean isSameTime(Timestamp curr, Timestamp diff, DateFormat format)
    {
        return format.format(curr).equals(format.format(diff));
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

    public static String getJSONByFastJSON(Object obj, String df)
    {
        return JSON.toJSONStringWithDateFormat(obj, df);
    }

    public static String getJSONByFastJSON(Object obj)
    {
        return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm");
    }

    /**
     * 是否相同月
     */
    public static boolean isSameMonth(long time)
    {
        Calendar calendar = Calendar.getInstance();
        int pMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(time);
        int nMonth = calendar.get(Calendar.MONTH);
        return pMonth == nMonth;
    }

    /**
     * 获取上一天时间
     */
    public static long getPreDayTime()
    {
        return getPreDayTime(1);
    }

    /**
     * 获取上一天时间
     */
    public static long getPreDayTime(int preDay)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -preDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取第二天 00:01 时间的毫秒值
     */
    public static long getNextZeroMilliseconds()
    {
        long current = System.currentTimeMillis();
        long dayMilliseconds = getDayMillisenconds();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long nextDay = calendar.getTimeInMillis() + dayMilliseconds;
        return (nextDay - current);
    }

    /**
     * 获取一天的毫秒值
     */
    public static long getDayMillisenconds()
    {
        return 1000 * 3600 * 24;
    }

    /**
     * 将json转换字符串
     */
    public static String convertJsonToStr(String json)
    {
        return json.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * 进行解析对象数据
     *
     * @param json JSON数据
     * @return 对象
     */
    public static Map<Integer, Integer> parseJsonMap(String json)
    {
        Map<Integer, Integer> map = parseJSONByFastJSON(json, new TypeReference<Map<Integer, Integer>>()
        {
        }.getType());
        if (map == null)
            map = new HashMap<>();
        return map;
    }

    /**
     * 进行解析对象数据
     *
     * @param json JSON数据
     * @return 对象
     */
    public static Map<String, String> parseJsonBase(String json)
    {
        if (json == null)
            return new HashMap<>();
        Map<String, String> map = parseJSONByFastJSON(json, new TypeReference<Map<String, String>>()
        {
        }.getType());
        if (map == null)
            map = new HashMap<>();
        return map;
    }

    /**
     * 进行转义list到map
     *
     * @param data list数据
     * @return map
     */
    public static Map<Integer, Integer> convertData(List<Integer> data)
    {
        Map<Integer, Integer> map = new HashMap<>();
        data.forEach(value ->
        {
            Integer count = map.get(value);
            if (count == null)
                count = 0;
            map.put(value, ++count);
        });
        return map;
    }

    /**
     * 获取Class 的所有字段
     *
     * @param clazz 查找类与父类所有field
     */
    public static Field[] getDeclaredFieldsAll(Class clazz)
    {
        List<Field> fields = new ArrayList<>();
        Class<?> superclass = clazz;
        while (superclass != null)
        {
            Field[] declaredFields = superclass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            superclass = superclass.getSuperclass();
        }
        Field[] _temp = new Field[fields.size()];
        _temp = fields.toArray(_temp);
        return _temp;
    }

    /**
     * 进行解析日期
     *
     * @param times 时间
     * @return 结果
     */
    public static Date[] parseDate(String times)
    {
        Date[] result = new Date[2];
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String[] strs = times.split("-");
            for (int i = 0; i < strs.length; i++)
            {
                result[i] = dateFormat.parse(strs[i].trim());
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return result;
    }

    /**
     * 进行解析日期
     *
     * @param times 时间
     * @return 结果
     */
    public static Date parseTime(String times)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.parse(times);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }


    /**
     * 进行解析日期
     *
     * @param times 时间
     * @return 结果
     */
    public static int[] parseMonth(String times)
    {
        int[] result = new int[2];
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
            String[] strs = times.split("-");
            for (int i = 0; i < strs.length; i++)
            {
                result[i] = XwhTool.getMonthValue(dateFormat.parse(strs[i].trim()));
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return result;
    }

    /**
     * 进行解析日期
     *
     * @param times 时间
     * @return 结果
     */
    public static int[] parseWeek(String times)
    {
        int[] result = new int[2];
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String[] strs = times.split("-");
            for (int i = 0; i < strs.length; i++)
            {
                result[i] = XwhTool.getWeekValue(dateFormat.parse(strs[i].trim()));
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return result;
    }
}
