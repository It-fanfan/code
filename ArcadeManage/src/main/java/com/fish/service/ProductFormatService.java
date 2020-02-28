package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ArcadeGamesMapper;
import com.fish.dao.primary.mapper.GameDayInfoMapper;
import com.fish.dao.primary.mapper.GameRoundMapper;
import com.fish.dao.primary.mapper.RoundsMapper;
import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.GameRound;
import com.fish.dao.primary.model.ProductFormat;
import com.fish.dao.primary.model.Rounds;
import com.fish.protocols.GetParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductFormatService implements BaseService<Rounds>
{


    @Autowired
    RoundsMapper roundsMapper;

    @Autowired
    GameDayInfoMapper gameDayInfoMapper;
    @Autowired
    ArcadeGamesMapper arcadeGamesMapper;
    @Autowired
    GameRoundMapper gameRoundMapper;

    @Autowired
    ProductFormat productFormat;
    @Autowired
    GameRound gameRound;

    @Override
    //查询展示所有产品赛制
    public List<Rounds> selectAll(GetParameter parameter)
    {
        List<Rounds> productFormats = new ArrayList<>();
        List<ArcadeGames> arcadeGames = arcadeGamesMapper.selectAll();

        for (ArcadeGames arcadeGame : arcadeGames)
        {
            Integer code = arcadeGame.getDdcode();
            List<GameRound> gameRounds = gameRoundMapper.selectByDdCode(code);
            if (gameRounds.size() > 0)
            {
                for (int i = 0; i < gameRounds.size(); i++)
                {
                    GameRound gameRound = gameRounds.get(i);
                    if (gameRound != null)
                    {
                        String round = gameRound.getDdround();
                        Rounds productFormat = roundsMapper.selectByDdCode(round);
                        Rounds rounds = new Rounds();
                        if (productFormat != null)
                        {
                            rounds.setGameCode(code);
                            rounds.setGameName(arcadeGame.getDdname());
                            rounds.setDdpriority(gameRound.getDdpriority());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            if (gameRound.getDdstart() != null)
                            {
                                String ddStart = sdf.format(gameRound.getDdstart());
                                String ddEnd = sdf.format(gameRound.getDdend());
                                rounds.setValidTime(ddStart + " - " + ddEnd);
                            }
                            rounds.setDdstate(gameRound.getDdstate());
                            rounds.setDdcode(productFormat.getDdcode());
                            rounds.setDdpriority(productFormat.getDdpriority());
                            rounds.setTimes(gameRound.getTimes());
                            rounds.setDdname(productFormat.getDdname());
                            rounds.setId(gameRound.getId());
                            rounds.setDdstate(gameRound.getDdstate());
                            Rounds roundsSplit = splitGameDayInfos(productFormat);
                            rounds.setSessionDetail(roundsSplit.getSessionDetail());
                            rounds.setTotalField(roundsSplit.getTotalField());
                            rounds.setGameDateSurvey(roundsSplit.getGameDateSurvey());

                            productFormats.add(rounds);
                        }

                    }
                }
            }
        }
        return productFormats;
    }

    public List<Rounds> selectAllCode(GetParameter parameter)
    {

        List<Rounds> productFormats = roundsMapper.selectAllS();
        for (Rounds productFormat : productFormats)
        {
            String ddcode = productFormat.getDdcode();
            String ddname = productFormat.getDdname();
            productFormat.setDdcode(ddcode + "-" + ddname);
        }
        return productFormats;
    }

    //新增产品赛制信息
    public int insert(Rounds record)
    {
        String codeSelect = record.getCodeSelect();
        String[] codeSplit = codeSelect.split("-");
        String gameCodeSelect = record.getGameCodeSelect();
        Date times = record.getTimes();
        GameRound gameRound = new GameRound();
        if (StringUtils.isNotBlank(record.getValidTime()))
        {
            String startTime = record.getValidTime();
            startTime = startTime.replaceAll(" ", "");
            String[] split = startTime.split("-");
            if (split.length > 0)
            {
                try
                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date dateStart = simpleDateFormat.parse(split[0]);
                    Date dateEnd = simpleDateFormat.parse(split[1]);

                    gameRound.setDdstart(dateStart);
                    gameRound.setDdend(dateEnd);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (record.getDdpriority() != null)
        {
            gameRound.setDdpriority(record.getDdpriority());
        }
        if (record.getDdstate() == null)
        {
            gameRound.setDdstate(true);
        } else
        {
            gameRound.setDdstate(record.getDdstate());
        }
        if (gameCodeSelect != null && gameCodeSelect.length() > 0)
        {
            gameRound.setDdgame(Integer.parseInt(gameCodeSelect));

        }
        if (codeSelect != null && codeSelect.length() > 0)
        {

            gameRound.setDdround(codeSplit[0]);
        }

        if (times == null)
        {
            gameRound.setTimes(new Timestamp(new Date().getTime()));
        } else
        {
            gameRound.setTimes(times);
        }
        int insert = gameRoundMapper.insert(gameRound);
        //int insert = productFormatMapper.insert(record);
        return insert;
    }

    //更新产品赛制信息
    public int updateByPrimaryKeySelective(Rounds record)
    {
        String codeSelect = record.getCodeSelect();
        String gameCodeSelect = record.getGameCodeSelect();
        String[] codeSplit = codeSelect.split("-");
        Date times = record.getTimes();
        if (StringUtils.isNotBlank(record.getValidTime()))
        {
            String startTime = record.getValidTime();
            startTime = startTime.replaceAll(" ", "");
            String[] split = startTime.split("-");
            if (split.length > 0)
            {
                try
                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date dateStart = simpleDateFormat.parse(split[0]);
                    Date dateEnd = simpleDateFormat.parse(split[1]);
                    gameRound.setDdstart(dateStart);
                    gameRound.setDdend(dateEnd);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (record.getDdpriority() != null)
        {
            gameRound.setDdpriority(record.getDdpriority());
        }
        if (record.getDdstate() == null)
        {
            gameRound.setDdstate(true);
        } else
        {
            gameRound.setDdstate(record.getDdstate());
        }
        if (gameCodeSelect != null && gameCodeSelect.length() > 0)
        {
            gameRound.setDdgame(Integer.parseInt(gameCodeSelect));

        }
        if (codeSelect != null && codeSelect.length() > 0)
        {

            gameRound.setDdround(codeSplit[0]);
        }
        if (times == null)
        {
            gameRound.setTimes(new Timestamp(new Date().getTime()));
        } else
        {
            gameRound.setTimes(times);
        }
        gameRound.setId(record.getId());
        int i = gameRoundMapper.updateByPrimaryKeySelective(gameRound);
        // int i = productFormatMapper.updateByPrimaryKeySelective(record);
        return i;
    }

    private Rounds splitGameDayInfos(Rounds productFormat)
    {

        Integer totalField = 0;
        Integer totalCash = 0;
        Integer totalGold = 0;
        String sessionDetail = "";
        Integer currentHour = 0;
        Integer ddHour0 = productFormat.getDdhour0();
        Integer ddHour1 = productFormat.getDdhour1();
        Integer ddHour2 = productFormat.getDdhour2();
        Integer ddHour3 = productFormat.getDdhour3();
        Integer ddHour4 = productFormat.getDdhour4();
        Integer ddHour5 = productFormat.getDdhour5();
        Integer ddHour6 = productFormat.getDdhour6();
        Integer ddHour7 = productFormat.getDdhour7();


        if (ddHour0 != null)
        {
            if (ddHour0 != 0)
            {

                sessionDetail = "0-" + ddHour0;
                currentHour = ddHour0;
                totalField = totalField + 1;
            }
            String ddaward0512 = productFormat.getDdaward0512a();
            String[] splits = ddaward0512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setOne1(splits[0]);
                        break;
                    case 1:
                        productFormat.setOne2(splits[1]);
                        break;
                    case 2:
                        productFormat.setOne3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setOne4(splits[3]);
                        break;
                    case 4:
                        productFormat.setOne5(splits[4]);
                        break;
                    case 5:
                        productFormat.setOne6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }

        }
        if (ddHour1 != null)
        {
            if (ddHour1 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour1);
                currentHour = currentHour + ddHour1;
                totalField = totalField + 1;
            }
            String ddaward1512 = productFormat.getDdaward1512a();
            String[] splits = ddaward1512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setTwo1(splits[0]);
                        break;
                    case 1:
                        productFormat.setTwo2(splits[1]);
                        break;
                    case 2:
                        productFormat.setTwo3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setTwo4(splits[3]);
                        break;
                    case 4:
                        productFormat.setTwo5(splits[4]);
                        break;
                    case 5:
                        productFormat.setTwo6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }
            System.out.println("totalCash :" + totalCash);
            System.out.println("totalGold :" + totalGold);
        }
        if (ddHour2 != null)
        {
            if (ddHour2 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour2);
                currentHour = currentHour + ddHour2;
                totalField = totalField + 1;
            }
            String ddaward2512 = productFormat.getDdaward2512a();
            String[] splits = ddaward2512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setThree1(splits[0]);
                        break;
                    case 1:
                        productFormat.setThree2(splits[1]);
                        break;
                    case 2:
                        productFormat.setThree3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setThree4(splits[3]);
                        break;
                    case 4:
                        productFormat.setThree5(splits[4]);
                        break;
                    case 5:
                        productFormat.setThree6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }

        }
        if (ddHour3 != null)
        {
            if (ddHour3 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour3);
                currentHour = currentHour + ddHour3;
                totalField = totalField + 1;
            }
            String ddaward3512 = productFormat.getDdaward3512a();
            String[] splits = ddaward3512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setFour1(splits[0]);
                        break;
                    case 1:
                        productFormat.setFour2(splits[1]);
                        break;
                    case 2:
                        productFormat.setFour3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setFour4(splits[3]);
                        break;
                    case 4:
                        productFormat.setFour5(splits[4]);
                        break;
                    case 5:
                        productFormat.setFour6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }

        }
        if (ddHour4 != null)
        {
            if (ddHour4 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour4);
                currentHour = currentHour + ddHour4;
                totalField = totalField + 1;
            }
            String ddaward4512 = productFormat.getDdaward4512a();
            String[] splits = ddaward4512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setFive1(splits[0]);
                        break;
                    case 1:
                        productFormat.setFive2(splits[1]);
                        break;
                    case 2:
                        productFormat.setFive3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setFive4(splits[3]);
                        break;
                    case 4:
                        productFormat.setFive5(splits[4]);
                        break;
                    case 5:
                        productFormat.setFive6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }

        }
        if (ddHour5 != null)
        {
            if (ddHour5 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour5);
                currentHour = currentHour + ddHour5;
                totalField = totalField + 1;
            }
            String ddaward5512 = productFormat.getDdaward5512a();
            String[] splits = ddaward5512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setSix1(splits[0]);
                        break;
                    case 1:
                        productFormat.setSix2(splits[1]);
                        break;
                    case 2:
                        productFormat.setSix3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setSix4(splits[3]);
                        break;
                    case 4:
                        productFormat.setSix5(splits[4]);
                        break;
                    case 5:
                        productFormat.setSix6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }

        }
        if (ddHour6 != null)
        {
            if (ddHour6 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour6);
                currentHour = currentHour + ddHour6;
                totalField = totalField + 1;
            }
            String ddaward6512 = productFormat.getDdaward6512a();
            String[] splits = ddaward6512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setSeven1(splits[0]);
                        break;
                    case 1:
                        productFormat.setSeven2(splits[1]);
                        break;
                    case 2:
                        productFormat.setSeven3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setSeven4(splits[3]);
                        break;
                    case 4:
                        productFormat.setSeven5(splits[4]);
                        break;
                    case 5:
                        productFormat.setSeven6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }

        }
        if (ddHour7 != null)
        {
            if (ddHour7 != 0)
            {
                sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour7);
                currentHour = currentHour + ddHour7;
                totalField = totalField + 1;
            }
            String ddaward7512 = productFormat.getDdaward7512a();
            String[] splits = ddaward7512.split("#");
            for (int i = 0, len = splits.length; i < 3; i++)
            {
                switch (i)
                {
                    case 0:
                        productFormat.setEight1(splits[0]);
                        break;
                    case 1:
                        productFormat.setEight2(splits[1]);
                        break;
                    case 2:
                        productFormat.setEight3(splits[2]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalCash = totalCash + cashInt;
            }
            for (int i = 3, len = splits.length; i < 6; i++)
            {
                switch (i)
                {
                    case 3:
                        productFormat.setEight4(splits[3]);
                        break;
                    case 4:
                        productFormat.setEight5(splits[4]);
                        break;
                    case 5:
                        productFormat.setEight6(splits[5]);
                        break;
                }
                int cashInt = Integer.parseInt(splits[i]);
                totalGold = totalGold + cashInt;
            }
            System.out.println("totalCash :" + totalCash);
            System.out.println("totalGold :" + totalGold);
        }
        String gameDateSurvey = "现金奖励" + totalCash + " 元，金币奖励" + totalGold + " 个。";
        productFormat.setSessionDetail(sessionDetail);
        productFormat.setTotalField(totalField);
        productFormat.setGameDateSurvey(gameDateSurvey);
        totalCash = 0;
        totalGold = 0;
        gameDateSurvey = "";
        totalField = 0;
        sessionDetail = "";
        currentHour = 0;
        return productFormat;
    }


    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("id");
        parameter.setOrder("desc");
    }

    @Override
    public Class<Rounds> getClassInfo()
    {
        return Rounds.class;
    }

    @Override
    public boolean removeIf(Rounds productFormat, JSONObject searchData)
    {
        return false;
    }
}
