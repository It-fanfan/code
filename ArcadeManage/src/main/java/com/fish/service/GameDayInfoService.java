package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.GameDayInfoMapper;
import com.fish.dao.primary.model.GameDayInfo;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameDayInfoService implements BaseService<GameDayInfo> {


    @Autowired
    GameDayInfoMapper gameDayInfoMapper;

    @Override
    public List<GameDayInfo> selectAll(GetParameter parameter) {

        List<GameDayInfo> gameDayInfos = gameDayInfoMapper.selectAll();
        //查询所有数据，对比赛日概况进行拼接
        List<GameDayInfo> gameDayInfo = splitGameDayInfos(gameDayInfos);
        return gameDayInfo;

    }

    public int insert(GameDayInfo productInfo) {

        GameDayInfo gameDayInfo = slpitReward(productInfo);
        Integer maxId  = gameDayInfoMapper.selectSMaxId();
        gameDayInfo.setDdcode("S"+(maxId+1));


        int insert = gameDayInfoMapper.insert(gameDayInfo);
        return insert;
    }

    public int updateByPrimaryKeySelective(GameDayInfo productInfo) {
        //修改合集内容比赛日场次信息进行拼接
       // GameDayInfo gameDayInfo = slpitAward(productInfo);

        GameDayInfo gameDayInfo = slpitReward(productInfo);
        //int i = 1;
        int i = gameDayInfoMapper.updateByPrimaryKeySelective(gameDayInfo);
        return i;
    }

    private GameDayInfo slpitReward(GameDayInfo productInfo) {
        if (productInfo.getDdhour0() != null) {
            String one1 = productInfo.getOne1();
            String one2 = productInfo.getOne2();
            String one3 = productInfo.getOne3();
            String one4 = productInfo.getOne4();
            String one5 = productInfo.getOne5();
            String one6 = productInfo.getOne6();
            productInfo.setDdaward0512a(one1+"#"+one2+"#"+one3+"#"+one4+"#"+one5+"#"+one6);
        }else {
            productInfo.setDdhour0(0);
            productInfo.setDdaward0512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        if (productInfo.getDdhour1() != null) {
            String two1 = productInfo.getTwo1();
            String two2 = productInfo.getTwo2();
            String two3 = productInfo.getTwo3();
            String two4 = productInfo.getTwo4();
            String two5 = productInfo.getTwo5();
            String two6 = productInfo.getTwo6();
            productInfo.setDdaward1512a(two1+"#"+two2+"#"+two3+"#"+two4+"#"+two5+"#"+two6);
        }else {
            productInfo.setDdhour1(0);
            productInfo.setDdaward1512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        if (productInfo.getDdhour2() != null) {
            String three1 = productInfo.getThree1();
            String three2 = productInfo.getThree2();
            String three3 = productInfo.getThree3();
            String three4 = productInfo.getThree4();
            String three5 = productInfo.getThree5();
            String three6 = productInfo.getThree6();
            productInfo.setDdaward2512a(three1+"#"+three2+"#"+three3+"#"+three4+"#"+three5+"#"+three6);
        }else {
            productInfo.setDdhour2(0);
            productInfo.setDdaward2512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }

        if (productInfo.getDdhour3() != null) {
            String four1 = productInfo.getFour1();
            String four2 = productInfo.getFour2();
            String four3 = productInfo.getFour3();
            String four4 = productInfo.getFour4();
            String four5 = productInfo.getFour5();
            String four6 = productInfo.getFour6();
            productInfo.setDdaward3512a(four1+"#"+four2+"#"+four3+"#"+four4+"#"+four5+"#"+four6);
        }else {
            productInfo.setDdhour3(0);
            productInfo.setDdaward3512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        if (productInfo.getDdhour4() != null) {
            String five1 = productInfo.getFive1();
            String five2 = productInfo.getFive2();
            String five3 = productInfo.getFive3();
            String five4 = productInfo.getFive4();
            String five5 = productInfo.getFive5();
            String five6 = productInfo.getFive6();
            productInfo.setDdaward4512a(five1+"#"+five2+"#"+five3+"#"+five4+"#"+five5+"#"+five6);
        }else {
            productInfo.setDdhour4(0);
            productInfo.setDdaward4512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        if (productInfo.getDdhour5() != null) {
            String six1 = productInfo.getSix1();
            String six2 = productInfo.getSix2();
            String six3 = productInfo.getSix3();
            String six4 = productInfo.getSix4();
            String six5 = productInfo.getSix5();
            String six6 = productInfo.getSix6();
            productInfo.setDdaward5512a(six1+"#"+six2+"#"+six3+"#"+six4+"#"+six5+"#"+six6);
        }else {
            productInfo.setDdhour5(0);
            productInfo.setDdaward5512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        if (productInfo.getDdhour6() != null) {
            String seven1 = productInfo.getSeven1();
            String seven2 = productInfo.getSeven2();
            String seven3 = productInfo.getSeven3();
            String seven4 = productInfo.getSeven4();
            String seven5 = productInfo.getSeven5();
            String seven6 = productInfo.getSeven6();
            productInfo.setDdaward6512a(seven1+"#"+seven2+"#"+seven3+"#"+seven4+"#"+seven5+"#"+seven6);
        }else {
            productInfo.setDdhour6(0);
            productInfo.setDdaward6512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        if (productInfo.getDdhour7() != null) {
            String eight1 = productInfo.getEight1();
            String eight2 = productInfo.getEight2();
            String eight3 = productInfo.getEight3();
            String eight4 = productInfo.getEight4();
            String eight5 = productInfo.getEight5();
            String eight6 = productInfo.getEight6();
            productInfo.setDdaward7512a(eight1+"#"+eight2+"#"+eight3+"#"+eight4+"#"+eight5+"#"+eight6);
        }else {
            productInfo.setDdhour7(0);
            productInfo.setDdaward7512a(0+"#"+0+"#"+0+"#"+0+"#"+0+"#"+0);
        }
        return productInfo;
    }



    private List<GameDayInfo> splitGameDayInfos(List<GameDayInfo> gameDayInfos) {

        for (GameDayInfo gameDayInfo : gameDayInfos) {
            Integer totalField = 0;
            Integer totalCash = 0;
            Integer totalGold = 0;
            String sessionDetail = "";
            Integer currentHour = 0;
            Integer ddHour0 = gameDayInfo.getDdhour0();
            Integer ddHour1 = gameDayInfo.getDdhour1();
            Integer ddHour2 = gameDayInfo.getDdhour2();
            Integer ddHour3 = gameDayInfo.getDdhour3();
            Integer ddHour4 = gameDayInfo.getDdhour4();
            Integer ddHour5 = gameDayInfo.getDdhour5();
            Integer ddHour6 = gameDayInfo.getDdhour6();
            Integer ddHour7 = gameDayInfo.getDdhour7();


            if (ddHour0 != null) {
                if (ddHour0 != 0) {

                    sessionDetail = "0-" + ddHour0;
                    currentHour = ddHour0;
                    totalField = totalField + 1;
                }
                String ddaward0512 = gameDayInfo.getDdaward0512a();
                String[] splits = ddaward0512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setOne1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setOne2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setOne3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setOne4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setOne5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setOne6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }

            }
            if (ddHour1 != null) {
                if (ddHour1 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour1);
                    currentHour = currentHour + ddHour1;
                    totalField = totalField + 1;
                }
                String ddaward1512 = gameDayInfo.getDdaward1512a();
                String[] splits = ddaward1512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setTwo1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setTwo2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setTwo3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setTwo4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setTwo5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setTwo6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }
                System.out.println("totalCash :" + totalCash);
                System.out.println("totalGold :" + totalGold);
            }
            if (ddHour2 != null) {
                if (ddHour2 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour2);
                    currentHour = currentHour + ddHour2;
                    totalField = totalField + 1;
                }
                String ddaward2512 = gameDayInfo.getDdaward2512a();
                String[] splits = ddaward2512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setThree1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setThree2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setThree3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setThree4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setThree5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setThree6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }

            }
            if (ddHour3 != null) {
                if (ddHour3 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour3);
                    currentHour = currentHour + ddHour3;
                    totalField = totalField + 1;
                }
                String ddaward3512 = gameDayInfo.getDdaward3512a();
                String[] splits = ddaward3512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setFour1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setFour2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setFour3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setFour4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setFour5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setFour6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }

            }
            if (ddHour4 != null) {
                if (ddHour4 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour4);
                    currentHour = currentHour + ddHour4;
                    totalField = totalField + 1;
                }
                String ddaward4512 = gameDayInfo.getDdaward4512a();
                String[] splits = ddaward4512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setFive1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setFive2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setFive3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setFive4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setFive5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setFive6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }

            }
            if (ddHour5 != null) {
                if (ddHour5 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour5);
                    currentHour = currentHour + ddHour5;
                    totalField = totalField + 1;
                }
                String ddaward5512 = gameDayInfo.getDdaward5512a();
                String[] splits = ddaward5512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setSix1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setSix2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setSix3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setSix4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setSix5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setSix6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }

            }
            if (ddHour6 != null) {
                if (ddHour6 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour6);
                    currentHour = currentHour + ddHour6;
                    totalField = totalField + 1;
                }
                String ddaward6512 = gameDayInfo.getDdaward6512a();
                String[] splits = ddaward6512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setSeven1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setSeven2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setSeven3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setSeven4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setSeven5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setSeven6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }

            }
            if (ddHour7 != null) {
                if (ddHour7 != 0) {
                    sessionDetail = sessionDetail + "#" + currentHour + "-" + (currentHour + ddHour7);
                    currentHour = currentHour + ddHour7;
                    totalField = totalField + 1;
                }
                String ddaward7512 = gameDayInfo.getDdaward7512a();
                String[] splits = ddaward7512.split("#");
                for (int i = 0, len = splits.length; i < 3; i++) {
                    switch (i) {
                        case 0:
                            gameDayInfo.setEight1(splits[0].toString());
                            break;
                        case 1:
                            gameDayInfo.setEight2(splits[1].toString());
                            break;
                        case 2:
                            gameDayInfo.setEight3(splits[2].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalCash = totalCash + cashInt;
                }
                for (int i = 3, len = splits.length; i < 6; i++) {
                    switch (i) {
                        case 3:
                            gameDayInfo.setEight4(splits[3].toString());
                            break;
                        case 4:
                            gameDayInfo.setEight5(splits[4].toString());
                            break;
                        case 5:
                            gameDayInfo.setEight6(splits[5].toString());
                            break;
                    }
                    int cashInt = Integer.parseInt(splits[i].toString());
                    totalGold = totalGold + cashInt;
                }
                System.out.println("totalCash :" + totalCash);
                System.out.println("totalGold :" + totalGold);
            }
            String gameDateSurvey = "现金奖励" + totalCash + " 元，金币奖励" + totalGold + " 个。";
            gameDayInfo.setSessionDetail(sessionDetail);
            gameDayInfo.setTotalField(totalField);
            gameDayInfo.setGameDateSurvey(gameDateSurvey);
            totalCash = 0;
            totalGold = 0;
            gameDateSurvey = "";
            totalField = 0;
            sessionDetail = "";
            currentHour = 0;
        }
        return gameDayInfos;
    }


    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<GameDayInfo> getClassInfo() {
        return GameDayInfo.class;
    }

    @Override
    public boolean removeIf(GameDayInfo gameDayInfo, JSONObject searchData) {
        return false;
    }


}