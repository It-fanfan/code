package com.fish.service;

import com.fish.dao.primary.mapper.GameDayInfoMapper;
import com.fish.dao.primary.model.GameDayInfo;
import com.fish.protocols.GetParameter;
import com.fish.utils.HexToStringUtil;
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
        String name = productInfo.getDdname128u();
        System.out.println("name :" + name);

        productInfo.setDdname128u(HexToStringUtil.getStringToHex(name));
        GameDayInfo gameDayInfo = slpitAward(productInfo);

        int insert = gameDayInfoMapper.insert(gameDayInfo);
        return insert;
    }

    public int updateByPrimaryKeySelective(GameDayInfo productInfo) {
        String name = productInfo.getDdname128u();
        System.out.println("name :" + name);
        productInfo.setDdname128u(HexToStringUtil.getStringToHex(name));
        //修改合集内容比赛日场次信息进行拼接
        GameDayInfo gameDayInfo = slpitAward(productInfo);
        System.out.println("gameDayInfo 1:" + gameDayInfo.getDdaward0512a());
        System.out.println("gameDayInfo 2:" + gameDayInfo.getDdaward1512a());
        System.out.println("gameDayInfo 3:" + gameDayInfo.getDdaward2512a());
        System.out.println("gameDayInfo 4:" + gameDayInfo.getDdaward3512a());
        System.out.println("gameDayInfo 5:" + gameDayInfo.getDdaward4512a());
        System.out.println("gameDayInfo 6:" + gameDayInfo.getDdaward5512a());
        System.out.println("gameDayInfo 7:" + gameDayInfo.getDdaward6512a());
        System.out.println("gameDayInfo 8:" + gameDayInfo.getDdaward7512a());
        //int i = 1;
        int i = gameDayInfoMapper.updateByPrimaryKeySelective(productInfo);
        return i;
    }


    private GameDayInfo slpitAward(GameDayInfo productInfo) {

        if (productInfo.getDdhour0() != null) {
            String one1 = productInfo.getOne1();
            String one2 = productInfo.getOne2();
            String one3 = productInfo.getOne3();
            String one4 = productInfo.getOne4();
            String one5 = productInfo.getOne5();
            String one6 = productInfo.getOne6();
            if (one1 == null || one1 == "")
            one1 = "0";
            if (one2 == null || one2 == "")
            one2 = "0";
            if (one3 == null || one3 == "")
            one3 = "0";
            if (one4 == null || one4 == "")
            one4 = "0";
            if (one5 == null || one5 == "")
            one5 = "0";
            if (one6 == null || one6 == "")
            one6 = "0";
            String award0521a = one1 + "#" + one2 + "#" + one3 + "#" + one4 + "#" + one5 + "#" + one6;
            productInfo.setDdaward0512a(award0521a);
        }else {productInfo.setDdhour0(0);
            productInfo.setDdaward0512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        if (productInfo.getDdhour1() != null) {
            String two1 = productInfo.getTwo1();
            String two2 = productInfo.getTwo2();
            String two3 = productInfo.getTwo3();
            String two4 = productInfo.getTwo4();
            String two5 = productInfo.getTwo5();
            String two6 = productInfo.getTwo6();
            if (two1 == null || two1 == "")
            two1 = "0";
            if (two2 == null || two2 == "")
            two2 = "0";
            if (two3 == null || two3 == "")
            two3 = "0";
            if (two4 == null || two4 == "")
            two4 = "0";
            if (two5 == null || two5 == "")
            two5 = "0";
            if (two6 == null || two6 == "")
            two6 = "0";

            String award1521a = two1 + "#" + two2 + "#" + two3 + "#" + two4 + "#" + two5 + "#" + two6;
            productInfo.setDdaward1512a(award1521a);
        }else {productInfo.setDdhour1(0);
            productInfo.setDdaward1512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}

        if (productInfo.getDdhour2() != null) {
            String three1 = productInfo.getThree1();
            String three2 = productInfo.getThree2();
            String three3 = productInfo.getThree3();
            String three4 = productInfo.getThree4();
            String three5 = productInfo.getThree5();
            String three6 = productInfo.getThree6();
            if (three1 == null || three1 == "")
            three1 = "0";
            if (three2 == null || three2 == "")
            three2 = "0";
            if (three3 == null || three3 == "")
            three3 = "0";
            if (three4 == null || three4 == "")
            three4 = "0";
            if (three5 == null || three5 == "")
            three5 = "0";
            if (three6 == null || three6 == "")
            three6 = "0";
            String award2521a = three1 + "#" + three2 + "#" + three3 + "#" + three4 + "#" + three5 + "#" + three6;
            productInfo.setDdaward2512a(award2521a);
        }else {productInfo.setDdhour2(0);
            productInfo.setDdaward2512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        if (productInfo.getDdhour3() != null) {
            String four1 = productInfo.getFour1();
            String four2 = productInfo.getFour2();
            String four3 = productInfo.getFour3();
            String four4 = productInfo.getFour4();
            String four5 = productInfo.getFour5();
            String four6 = productInfo.getFour6();
            if (four1 == null || four1 == "")
            four1 = "0";
            if (four2 == null || four2 == "")
            four2 = "0";
            if (four3 == null || four3 == "")
            four3 = "0";
            if (four4 == null || four4 == "")
            four4 = "0";
            if (four5 == null || four5 == "")
            four5 = "0";
            if (four6 == null || four6 == "")
            four6 = "0";
            String award3521a = four1 + "#" + four2 + "#" + four3 + "#" + four4 + "#" + four5 + "#" + four6;
            productInfo.setDdaward3512a(award3521a);
        }else {productInfo.setDdhour3(0);
            productInfo.setDdaward3512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        if (productInfo.getDdhour4() !=null) {
            String five1 = productInfo.getFive1();
            String five2 = productInfo.getFive2();
            String five3 = productInfo.getFive3();
            String five4 = productInfo.getFive4();
            String five5 = productInfo.getFive5();
            String five6 = productInfo.getFive6();
            if (five1 == null || five1 == "")
            five1 = "0";
            if (five2 == null || five2 == "")
            five2 = "0";
            if (five3 == null || five3 == "")
            five3 = "0";
            if (five4 == null || five4 == "")
            five4 = "0";
            if (five5 == null || five5 == "")
            five5 = "0";
            if (five6 == null || five6 == "")
            five6 = "0";
            String award4521a = five1 + "#" + five2 + "#" + five3 + "#" + five4 + "#" + five5 + "#" + five6;
            productInfo.setDdaward4512a(award4521a);
        }else {productInfo.setDdhour4(0);
            productInfo.setDdaward4512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        if (productInfo.getDdhour5() != null) {
            String six1 = productInfo.getSix1();
            String six2 = productInfo.getSix2();
            String six3 = productInfo.getSix3();
            String six4 = productInfo.getSix4();
            String six5 = productInfo.getSix5();
            String six6 = productInfo.getSix6();
            if (six1 == null || six1 == "")
            six1 = "0";
            if (six2 == null || six2 == "")
            six2 = "0";
            if (six3 == null || six3 == "")
            six3 = "0";
            if (six4 == null || six4 == "")
            six4 = "0";
            if (six5 == null || six5 == "")
            six5 = "0";
            if (six6 == null || six6 == "")
            six6 = "0";
            String award5521a = six1 + "#" + six2 + "#" + six3 + "#" + six4 + "#" + six5 + "#" + six6;
            productInfo.setDdaward5512a(award5521a);
        }else {productInfo.setDdhour5(0);
            productInfo.setDdaward5512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        if (productInfo.getDdhour6() != null) {
            String seven1 = productInfo.getSeven1();
            String seven2 = productInfo.getSeven2();
            String seven3 = productInfo.getSeven3();
            String seven4 = productInfo.getSeven4();
            String seven5 = productInfo.getSeven5();
            String seven6 = productInfo.getSeven6();
            if (seven1 == null || seven1 == "")
            seven1 = "0";
            if (seven2 == null || seven2 == "")
            seven2 = "0";
            if (seven3 == null || seven3 == "")
            seven3 = "0";
            if (seven4 == null || seven4 == "")
            seven4 = "0";
            if (seven5 == null || seven5 == "")
            seven5 = "0";
            if (seven6 == null || seven6 == "")
            seven6 = "0";
            String award6521a = seven1 + "#" + seven2 + "#" + seven3 + "#" + seven4 + "#" + seven5 + "#" + seven6;
            productInfo.setDdaward6512a(award6521a);
        }else {productInfo.setDdhour6(0);
            productInfo.setDdaward6512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        if (productInfo.getDdhour7() != null) {
            String eight1 = productInfo.getEight1();
            String eight2 = productInfo.getEight2();
            String eight3 = productInfo.getEight3();
            String eight4 = productInfo.getEight4();
            String eight5 = productInfo.getEight5();
            String eight6 = productInfo.getEight6();
            if (eight1 == null || eight1 == "")
            eight1 = "0";
            if (eight2 == null || eight2 == "")
            eight2 = "0";
            if (eight3 == null || eight3 == "")
            eight3 = "0";
            if (eight4 == null || eight4 == "")
            eight4 = "0";
            if (eight5 == null || eight5 == "")
            eight5 = "0";
            if (eight6 == null || eight6 == "")
            eight6 = "0";
            String award7521a = eight1 + "#" + eight2 + "#" + eight3 + "#" + eight4 + "#" + eight5 + "#" + eight6;
            productInfo.setDdaward7512a(award7521a);
        }else {productInfo.setDdhour7(0);
            productInfo.setDdaward7512a(0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0 + "#" + 0);}
        return productInfo;
    }


    private List<GameDayInfo> splitGameDayInfos(List<GameDayInfo> gameDayInfos) {

        for (GameDayInfo gameDayInfo : gameDayInfos) {
            Integer totalField = 0;
            Integer totalCash = 0;
            Integer totalGold = 0;
            gameDayInfo.setDdname128u(HexToStringUtil.getStringFromHex(gameDayInfo.getDdname128u()));
            Integer ddHour0 = gameDayInfo.getDdhour0();
            Integer ddHour1 = gameDayInfo.getDdhour1();
            Integer ddHour2 = gameDayInfo.getDdhour2();
            Integer ddHour3 = gameDayInfo.getDdhour3();
            Integer ddHour4 = gameDayInfo.getDdhour4();
            Integer ddHour5 = gameDayInfo.getDdhour5();
            Integer ddHour6 = gameDayInfo.getDdhour6();
            Integer ddHour7 = gameDayInfo.getDdhour7();
            if (ddHour0 != null) {
                if(ddHour0 !=0){
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
                if(ddHour1 != 0){
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
                if(ddHour2 != 0){
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
                if(ddHour3 != 0){
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
                if(ddHour4 != 0){
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
                if(ddHour5 != 0){
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
                if(ddHour6 != 0){
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
                if(ddHour7 != 0){
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
            String gameDateSurvey = "共 " + totalField + " 场，现金奖励" + totalCash + " 元，金币奖励" + totalGold + " 个。";
            gameDayInfo.setGameDateSurvey(gameDateSurvey);
            totalCash =0;
            totalGold =0;
        }
        return gameDayInfos;
    }


    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<GameDayInfo> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(GameDayInfo gameDayInfo, Map<String, String> searchData) {
        return false;
    }


}