package com.fish.utils;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


@MapperScan("com.fish.manger.v5.mapper")
public class test
{

    @Autowired
    RedisData redisData;

    public static void main(String[] args) throws Exception
    {


        //https://sgame.qinyougames.com/persieService/flush/logic
        //http://192.168.1.55:8080/persieService/flush/logic
        //persieDeamon   persieService    public
//        LocalTime localTime = LocalTime.now();
//        LocalDate localDate = LocalDate.now();
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//        ZoneId zone = ZoneId.systemDefault();
//        Instant instant = localDateTime.atZone(zone).toInstant();
//        Date date = Date.from(instant);
//        System.out.println(date);   //sgame.qinyougames.com

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","wx_config");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","deamon");//deamon , online   persieDeamon
//        String res= HttpUtil.post("https://logic.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","games");
//        String res= HttpUtil.post("http://192.168.1.55:8080/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notice_system");
//        String res= HttpUtil.post("https://logic.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name","notification");
//        String res= HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :"+res);


//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name", "games");
//        String res = HttpUtil.post("https://logic.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :" + res);

//        JSONObject paramMap = new JSONObject();
//        paramMap.put("name", "rounds");
//        String res = HttpUtil.post("https://sgame.qinyougames.com/persieService/flush/logic", paramMap.toJSONString());
//        System.out.println("我是返回值 :" + res);

      /*  Date now = new Date();
        System.out.println(now);
// java.util.Date -> java.time.LocalDate
        LocalDate localDate=now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
// java.time.LocalDate -> java.sql.Date
        Date newDate=java.sql.Date.valueOf(localDate);
        System.out.println(newDate);
        localDate = localDate.minusDays(1);
        Date tomorrowDate=java.sql.Date.valueOf(localDate);
        System.out.println(tomorrowDate);*/

        ;


//        DateFormat dateFmt = new SimpleDateFormat("yyyyMMddHH");//格式化一下时间
//        Date dNow = new Date(); //当前时间
//        Date dBefore = new Date();

//        Calendar calendar = Calendar.getInstance(); //得到日历
//        calendar.setTime(dNow);//把当前时间赋给日历
//        String currenttime = dateFmt.format(calendar.getTime());
//        System.out.println("currenttime :"+Integer.parseInt(currenttime));

//        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
//        dBefore = calendar.getTime(); //得到前一天的时间
//        String defaultStartDate = dateFmt.format(dBefore); //格式化前一天
//        System.out.println("defaultStartDate ："+defaultStartDate);
//        defaultStartDate = defaultStartDate.substring(0,10)+" 00:00:00";
//        System.out.println("defaultStartDate ："+defaultStartDate);
//        String defaultEndDate = defaultStartDate.substring(0,10)+" 23:59:59";
//        System.out.println("defaultEndDate ："+defaultEndDate);
//        RedisData redisData = new RedisData();
//        redisData.searchAllUser();

//        LocalDate now = LocalDate.now();
//        LocalDate localDate = now.minusDays(1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String localDatea = formatter.format(localDate);
//        System.out.println(localDatea );

//        InetAddress address = InetAddress.getLocalHost();
//        System.out.println(address.getHostName());//主机名
//        System.out.println(address.getCanonicalHostName());//主机别名
//        System.out.println(address.getHostAddress());//获取IP地址
//        System.out.println("===============");

//        Date date = new Date();
//        long time = date.getTime();
//        System.out.println(time);


//        String now = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
//        System.out.println(now);
//        String now =  "2019/12/03 - 2019/12/04";
//        String[] split = now.split("-");
//        System.out.println(split[0].replace("/","-"));
//        System.out.println(split[1].trim().replace("/","-"));

//        String url ="http://192.168.1.55:8080/persieRes/wx75f1c4d8cd887fd6/skip/readme.json";
//        String s = ReadJsonUtil.loadJson(url);
//        String minify = minify(s);
//
//        JSONObject jsonObject = JSONObject.parseObject(minify);
//        Object list = jsonObject.get("list");
//        Object banner = jsonObject.get("banner");
//
//        List<ExportResult> exportResults = new ArrayList<>();
//        String obtainResultUrl1 = "http://192.168.1.55:8980/persieDeamon/match/match-c68-g0-i4-0.json";
//        String result1 = HttpUtil.get(obtainResultUrl1);
//        //System.out.println(result1);
//        JSONArray object1 = JSONArray.parseArray(result1);
//
//        String obtainResultUrl2 = "http://192.168.1.55:8980/persieDeamon/match/match-c160-g0-i3-0.json";
//        String result2 = HttpUtil.get(obtainResultUrl2);
//        JSONArray object2 = JSONArray.parseArray(result2);
//
//        object1.addAll(object2);
//        // System.out.println(object1.toJSONString());
//        for (Object object : object1)
//        {
//            ExportResult exportResult = new ExportResult();
//            JSONObject jsonObject = JSONObject.parseObject(object.toString());
//
//            Object uid = jsonObject.get("uid");
//            Object index = jsonObject.get("index");
//            Object name = jsonObject.get("name");
//            Object value = jsonObject.get("value");
//            Object type = jsonObject.get("type");
//            Object mark = jsonObject.get("mark");
//
//            exportResult.setRoundName("常规赛");
//            exportResult.setRoundLength("15分钟");
//            exportResult.setIndex(Integer.parseInt(index.toString()));
//            exportResult.setName(name.toString());
//            exportResult.setUid(uid.toString());
//            exportResult.setValue(Integer.parseInt(value.toString()));
//            exportResult.setType(type.toString());
//            exportResult.setMark(Integer.parseInt(mark.toString()));
//            exportResults.add(exportResult);
//        }
//        LocalDate now = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String localDatea = formatter.format(now);
//        Workbook sheets = ExcelUtils.writeExcel(exportResults, localDatea);

//        String s = ReadJsonUtil.ReadFile("C:/Users/Host-0/Desktop/readme.json");
//        System.out.println(s);
//
//        List<DataCollect> list = (List<DataCollect>) RedisUtils.getList("dataList");
//        for (DataCollect dataCollect : list) {
//            String s = dataCollect.toString();
//            System.out.println(s);
//        }

       // File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/p12/userTemplate.xlsx");
//       String aa= ResourceUtils.CLASSPATH_URL_PREFIX + "static/p12/1502768571.p12";
//        System.out.println(aa);
       String path = test.class.getResource("/").getPath();
        System.out.println(path);


    }


    public static String minify(String jsonString)
    {
        boolean in_string = false;
        boolean in_multiline_comment = false;
        boolean in_singleline_comment = false;
        char string_opener = 'x'; // unused value, just something that makes compiler happy

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < jsonString.length(); i++)
        {
            // get next (c) and next-next character (cc)

            char c = jsonString.charAt(i);
            String cc = jsonString.substring(i, Math.min(i + 2, jsonString.length()));

            // big switch is by what mode we're in (in_string etc.)
            if (in_string)
            {
                if (c == string_opener)
                {
                    in_string = false;
                    out.append(c);
                } else if (c == '\\')
                { // no special treatment needed for \\u, it just works like this too
                    out.append(cc);
                    ++i;
                } else
                    out.append(c);
            } else if (in_singleline_comment)
            {
                if (c == '\r' || c == '\n')
                    in_singleline_comment = false;
            } else if (in_multiline_comment)
            {
                if (cc.equals("*/"))
                {
                    in_multiline_comment = false;
                    ++i;
                }
            } else
            {
                // we're outside of the special modes, so look for mode openers (comment start, string start)
                if (cc.equals("/*"))
                {
                    in_multiline_comment = true;
                    ++i;
                } else if (cc.equals("//"))
                {
                    in_singleline_comment = true;
                    ++i;
                } else if (c == '"' || c == '\'')
                {
                    in_string = true;
                    string_opener = c;
                    out.append(c);
                } else if (!Character.isWhitespace(c))
                    out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key)
    {
        if (key.length() < 16)
        {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = key.charAt(index);
        int c1 = key.charAt(index + 1);
        int c2 = key.charAt(index + 2);
        int c3 = key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0)
        {
            value = -value;
        }

        return String.valueOf(value);
    }



}
