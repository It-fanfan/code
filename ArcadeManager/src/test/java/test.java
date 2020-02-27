import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.fish.manger.v5.mapper")
public class test {

    public static void main(String[] args) {

        //https://sgame.qinyougames.com/persieService/flush/logic
        //http://192.168.1.55:8080/persieService/flush/logic
        //persieDeamon   persieService
//        LocalTime localTime = LocalTime.now();
//        LocalDate localDate = LocalDate.now();
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//        ZoneId zone = ZoneId.systemDefault();
//        Instant instant = localDateTime.atZone(zone).toInstant();
//        Date date = Date.from(instant);
//        System.out.println(date);   //sgame.qinyougames.com

        JSONObject paramMap = new JSONObject();
        paramMap.put("name", "init_config");
        String res = HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
        System.out.println("我是返回值 :" + res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","gameset");
//        String res= HttpUtil.post("https://sgame.qinyougames.com/persieDeamon/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);
//

//     String aa = getStringFromHex("");
//
//        System.out.println(aa);

//        String s = generateTableCodeByKey("oMea15B7hdc978j-bCh9UPKS3bh0");
//        System.out.println(s);
    }
    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key) {
        if (key.length() < 16) {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = (int) key.charAt(index);
        int c1 = (int) key.charAt(index + 1);
        int c2 = (int) key.charAt(index + 2);
        int c3 = (int) key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0) {
            value = -value;
        }

        return String.valueOf(value);
    }

    /**
     * 从二进制到字符串的方法
     *
     * @param hexString 二进制信息
     * @return 得到的字符串信息
     */
    public static String getStringFromHex(String hexString)
    {
        byte[] bytes = new byte[hexString.length() / 2];

        for (int i = 0, j = 0; i < hexString.length(); i += 2, j++)
        {
            String value = "0x" + hexString.substring(i, i + 2);

            bytes[j] = Integer.decode(value).byteValue();
        }

        return new String(bytes);
    }

    /**
     * 从字符串到二进制流的方法
     *
     * @param content 源字串信息
     * @return 得到的二进制流的文本描述
     */
    public static String getStringToHex(String content)
    {
        byte[] bytes = content.getBytes();
        StringBuilder sb = new StringBuilder();
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

        return sb.toString();
    }

    @Test
    public void testConn() throws SQLException {
        String time = "1567197702450";
        String timeStamp2Date = timeStampDate(time);
        Date date = new Date();
        System.out.println(date);

    }
    public static String timeStampDate(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
