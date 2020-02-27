package com.fish.manger.v5;

import com.fish.dao.primary.model.ProductJson;
import com.fish.manger.v5.generator.LayuiHtml;
import com.fish.service.ProductService;
import com.fish.utils.ReadTxtFileTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.fish.manger.v5.mapper")
public class MangerApplicationTests
{
    @Autowired
    LayuiHtml layUi;
    @Autowired
    ProductService productService;

    @Test
    public void contextLoads()
    {
        layUi.generator();
    }
    public String timeStampDate(String time) {
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
