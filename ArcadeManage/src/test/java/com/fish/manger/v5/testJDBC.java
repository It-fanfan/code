package com.fish.manger.v5;

import com.fish.dao.primary.mapper.RankingMapper;
import com.fish.dao.primary.model.Ranking;
import com.fish.dao.third.mapper.MinitjWxMapper;
import com.fish.dao.third.model.MinitjWx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan({"com.fish.manger.v5.mapper", "com.fish.dao.primary.mapper", "com.fish.dao.third.mapper"})
public class testJDBC {
    @Autowired
    private RankingMapper rankingMapper;
    @Autowired
    MinitjWxMapper minitjWxMapper;
    @Test
    public void jdbcTest() {
        //List<Ranking> rankings = rankingMapper.selectByDate("2019-12-12");
//        List<Ranking> rankings =new ArrayList<>();
//        Ranking ranking1 = new Ranking();
//        int i = rankingMapper.insertBatch(rankings);
//        System.out.println("i:"+i);

        MinitjWx minitjWx = minitjWxMapper.selectByPrimaryKey("wx092939faea6aa130", "2019-12-30");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//要转换的时间格式

        Date wxDate = minitjWx.getWxDate();
        String format = sdf.format(wxDate);
        System.out.println(format);
        String s = minitjWx.toString();
        System.out.println(s);
    }

}
