package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductFormat
{
    private Integer ddpriority;
    private Boolean ddstate;

    private String codeSelect;
    private String gameCodeSelect;
    private String startTime;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date ddStart;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date ddEnd;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date times;

    private String validTime;

    private Integer gameCode;

    private String gameName;

    private Integer totalField;

    private Integer totalCash;

    private Integer totalGold;
    private String sessionDetail;

    private String gameDateSurvey;

    private Integer id;

    private String ddcode;

    private String ddname;

    private String ddname128u;

    private Integer ddhour0;

    private String ddaward0512a;

    private Integer ddhour1;

    private String ddaward1512a;

    private Integer ddhour2;

    private String ddaward2512a;

    private Integer ddhour3;

    private String ddaward3512a;

    private Integer ddhour4;

    private String ddaward4512a;

    private Integer ddhour5;

    private String ddaward5512a;

    private Integer ddhour6;

    private String ddaward6512a;

    private Integer ddhour7;

    private String ddaward7512a;

    private Integer ddhour8;

    private String ddaward8512a;

    private Integer ddhour9;

    private String ddaward9512a;


    private Date creatTime;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date effectTime;

    private Boolean formatStatus;
    private Boolean gameType;

    private String one1;
    private String one2;
    private String one3;
    private String one4;
    private String one5;
    private String one6;
    private String two1;
    private String two2;
    private String two3;
    private String two4;
    private String two5;
    private String two6;
    private String three1;
    private String three2;
    private String three3;
    private String three4;
    private String three5;
    private String three6;
    private String four1;
    private String four2;
    private String four3;
    private String four4;
    private String four5;
    private String four6;
    private String five1;
    private String five2;
    private String five3;
    private String five4;
    private String five5;
    private String five6;
    private String six1;
    private String six2;
    private String six3;
    private String six4;
    private String six5;
    private String six6;
    private String seven1;
    private String seven2;
    private String seven3;
    private String seven4;
    private String seven5;
    private String seven6;
    private String eight1;
    private String eight2;
    private String eight3;
    private String eight4;
    private String eight5;
    private String eight6;

    public Integer getDdpriority()
    {
        return ddpriority;
    }

    public void setDdpriority(Integer ddpriority)
    {
        this.ddpriority = ddpriority;
    }

    public Boolean getDdstate()
    {
        return ddstate;
    }

    public void setDdstate(Boolean ddstate)
    {
        this.ddstate = ddstate;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getCodeSelect()
    {
        return codeSelect;
    }

    public void setCodeSelect(String codeSelect)
    {
        this.codeSelect = codeSelect;
    }

    public String getGameCodeSelect()
    {
        return gameCodeSelect;
    }

    public void setGameCodeSelect(String gameCodeSelect)
    {
        this.gameCodeSelect = gameCodeSelect;
    }

    public String getValidTime()
    {
        return validTime;
    }

    public void setValidTime(String validTime)
    {
        this.validTime = validTime;
    }

    public Date getDdStart()
    {
        return ddStart;
    }

    public void setDdStart(Date ddStart)
    {
        this.ddStart = ddStart;
    }

    public Date getDdEnd()
    {
        return ddEnd;
    }

    public void setDdEnd(Date ddEnd)
    {
        this.ddEnd = ddEnd;
    }

    public Date getTimes()
    {
        return times;
    }

    public void setTimes(Date times)
    {
        this.times = times;
    }

    public String getSessionDetail()
    {
        return sessionDetail;
    }

    public void setSessionDetail(String sessionDetail)
    {
        this.sessionDetail = sessionDetail;
    }

    public Integer getTotalField()
    {
        return totalField;
    }

    public void setTotalField(Integer totalField)
    {
        this.totalField = totalField;
    }

    public Integer getTotalCash()
    {
        return totalCash;
    }

    public void setTotalCash(Integer totalCash)
    {
        this.totalCash = totalCash;
    }

    public Integer getTotalGold()
    {
        return totalGold;
    }

    public void setTotalGold(Integer totalGold)
    {
        this.totalGold = totalGold;
    }

    public Integer getGameCode()
    {
        return gameCode;
    }

    public void setGameCode(Integer gameCode)
    {
        this.gameCode = gameCode;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDdcode()
    {
        return ddcode;
    }

    public void setDdcode(String ddcode)
    {
        this.ddcode = ddcode == null ? null : ddcode.trim();
    }

    public String getDdname()
    {
        return ddname;
    }

    public void setDdname(String ddname)
    {
        this.ddname = ddname == null ? null : ddname.trim();
    }

    public String getDdname128u()
    {
        return ddname128u;
    }

    public void setDdname128u(String ddname128u)
    {
        this.ddname128u = ddname128u == null ? null : ddname128u.trim();
    }

    public Integer getDdhour0()
    {
        return ddhour0;
    }

    public void setDdhour0(Integer ddhour0)
    {
        this.ddhour0 = ddhour0;
    }

    public String getDdaward0512a()
    {
        return ddaward0512a;
    }

    public void setDdaward0512a(String ddaward0512a)
    {
        this.ddaward0512a = ddaward0512a == null ? null : ddaward0512a.trim();
    }

    public Integer getDdhour1()
    {
        return ddhour1;
    }

    public void setDdhour1(Integer ddhour1)
    {
        this.ddhour1 = ddhour1;
    }

    public String getDdaward1512a()
    {
        return ddaward1512a;
    }

    public void setDdaward1512a(String ddaward1512a)
    {
        this.ddaward1512a = ddaward1512a == null ? null : ddaward1512a.trim();
    }

    public Integer getDdhour2()
    {
        return ddhour2;
    }

    public void setDdhour2(Integer ddhour2)
    {
        this.ddhour2 = ddhour2;
    }

    public String getDdaward2512a()
    {
        return ddaward2512a;
    }

    public void setDdaward2512a(String ddaward2512a)
    {
        this.ddaward2512a = ddaward2512a == null ? null : ddaward2512a.trim();
    }

    public Integer getDdhour3()
    {
        return ddhour3;
    }

    public void setDdhour3(Integer ddhour3)
    {
        this.ddhour3 = ddhour3;
    }

    public String getDdaward3512a()
    {
        return ddaward3512a;
    }

    public void setDdaward3512a(String ddaward3512a)
    {
        this.ddaward3512a = ddaward3512a == null ? null : ddaward3512a.trim();
    }

    public Integer getDdhour4()
    {
        return ddhour4;
    }

    public void setDdhour4(Integer ddhour4)
    {
        this.ddhour4 = ddhour4;
    }

    public String getDdaward4512a()
    {
        return ddaward4512a;
    }

    public void setDdaward4512a(String ddaward4512a)
    {
        this.ddaward4512a = ddaward4512a == null ? null : ddaward4512a.trim();
    }

    public Integer getDdhour5()
    {
        return ddhour5;
    }

    public void setDdhour5(Integer ddhour5)
    {
        this.ddhour5 = ddhour5;
    }

    public String getDdaward5512a()
    {
        return ddaward5512a;
    }

    public void setDdaward5512a(String ddaward5512a)
    {
        this.ddaward5512a = ddaward5512a == null ? null : ddaward5512a.trim();
    }

    public Integer getDdhour6()
    {
        return ddhour6;
    }

    public void setDdhour6(Integer ddhour6)
    {
        this.ddhour6 = ddhour6;
    }

    public String getDdaward6512a()
    {
        return ddaward6512a;
    }

    public void setDdaward6512a(String ddaward6512a)
    {
        this.ddaward6512a = ddaward6512a == null ? null : ddaward6512a.trim();
    }

    public Integer getDdhour7()
    {
        return ddhour7;
    }

    public void setDdhour7(Integer ddhour7)
    {
        this.ddhour7 = ddhour7;
    }

    public String getDdaward7512a()
    {
        return ddaward7512a;
    }

    public void setDdaward7512a(String ddaward7512a)
    {
        this.ddaward7512a = ddaward7512a == null ? null : ddaward7512a.trim();
    }

    public Integer getDdhour8()
    {
        return ddhour8;
    }

    public void setDdhour8(Integer ddhour8)
    {
        this.ddhour8 = ddhour8;
    }

    public String getDdaward8512a()
    {
        return ddaward8512a;
    }

    public void setDdaward8512a(String ddaward8512a)
    {
        this.ddaward8512a = ddaward8512a == null ? null : ddaward8512a.trim();
    }

    public Integer getDdhour9()
    {
        return ddhour9;
    }

    public void setDdhour9(Integer ddhour9)
    {
        this.ddhour9 = ddhour9;
    }

    public String getDdaward9512a()
    {
        return ddaward9512a;
    }

    public void setDdaward9512a(String ddaward9512a)
    {
        this.ddaward9512a = ddaward9512a == null ? null : ddaward9512a.trim();
    }

    public Date getCreatTime()
    {
        return creatTime;
    }

    public void setCreatTime(Date creatTime)
    {
        this.creatTime = creatTime;
    }

    public Date getEffectTime()
    {
        return effectTime;
    }

    public void setEffectTime(Date effectTime)
    {
        this.effectTime = effectTime;
    }

    public Boolean getFormatStatus()
    {
        return formatStatus;
    }

    public void setFormatStatus(Boolean formatStatus)
    {
        this.formatStatus = formatStatus;
    }

    public Boolean getGameType()
    {
        return gameType;
    }

    public void setGameType(Boolean gameType)
    {
        this.gameType = gameType;
    }

    public String getOne1()
    {
        return one1;
    }

    public void setOne1(String one1)
    {
        this.one1 = one1;
    }

    public String getOne2()
    {
        return one2;
    }

    public void setOne2(String one2)
    {
        this.one2 = one2;
    }

    public String getOne3()
    {
        return one3;
    }

    public void setOne3(String one3)
    {
        this.one3 = one3;
    }

    public String getOne4()
    {
        return one4;
    }

    public void setOne4(String one4)
    {
        this.one4 = one4;
    }

    public String getOne5()
    {
        return one5;
    }

    public void setOne5(String one5)
    {
        this.one5 = one5;
    }

    public String getOne6()
    {
        return one6;
    }

    public void setOne6(String one6)
    {
        this.one6 = one6;
    }

    public String getTwo1()
    {
        return two1;
    }

    public void setTwo1(String two1)
    {
        this.two1 = two1;
    }

    public String getTwo2()
    {
        return two2;
    }

    public void setTwo2(String two2)
    {
        this.two2 = two2;
    }

    public String getTwo3()
    {
        return two3;
    }

    public void setTwo3(String two3)
    {
        this.two3 = two3;
    }

    public String getTwo4()
    {
        return two4;
    }

    public void setTwo4(String two4)
    {
        this.two4 = two4;
    }

    public String getTwo5()
    {
        return two5;
    }

    public void setTwo5(String two5)
    {
        this.two5 = two5;
    }

    public String getTwo6()
    {
        return two6;
    }

    public void setTwo6(String two6)
    {
        this.two6 = two6;
    }

    public String getThree1()
    {
        return three1;
    }

    public void setThree1(String three1)
    {
        this.three1 = three1;
    }

    public String getThree2()
    {
        return three2;
    }

    public void setThree2(String three2)
    {
        this.three2 = three2;
    }

    public String getThree3()
    {
        return three3;
    }

    public void setThree3(String three3)
    {
        this.three3 = three3;
    }

    public String getThree4()
    {
        return three4;
    }

    public void setThree4(String three4)
    {
        this.three4 = three4;
    }

    public String getThree5()
    {
        return three5;
    }

    public void setThree5(String three5)
    {
        this.three5 = three5;
    }

    public String getThree6()
    {
        return three6;
    }

    public void setThree6(String three6)
    {
        this.three6 = three6;
    }

    public String getFour1()
    {
        return four1;
    }

    public void setFour1(String four1)
    {
        this.four1 = four1;
    }

    public String getFour2()
    {
        return four2;
    }

    public void setFour2(String four2)
    {
        this.four2 = four2;
    }

    public String getFour3()
    {
        return four3;
    }

    public void setFour3(String four3)
    {
        this.four3 = four3;
    }

    public String getFour4()
    {
        return four4;
    }

    public void setFour4(String four4)
    {
        this.four4 = four4;
    }

    public String getFour5()
    {
        return four5;
    }

    public void setFour5(String four5)
    {
        this.four5 = four5;
    }

    public String getFour6()
    {
        return four6;
    }

    public void setFour6(String four6)
    {
        this.four6 = four6;
    }

    public String getFive1()
    {
        return five1;
    }

    public void setFive1(String five1)
    {
        this.five1 = five1;
    }

    public String getFive2()
    {
        return five2;
    }

    public void setFive2(String five2)
    {
        this.five2 = five2;
    }

    public String getFive3()
    {
        return five3;
    }

    public void setFive3(String five3)
    {
        this.five3 = five3;
    }

    public String getFive4()
    {
        return five4;
    }

    public void setFive4(String five4)
    {
        this.five4 = five4;
    }

    public String getFive5()
    {
        return five5;
    }

    public void setFive5(String five5)
    {
        this.five5 = five5;
    }

    public String getFive6()
    {
        return five6;
    }

    public void setFive6(String five6)
    {
        this.five6 = five6;
    }

    public String getSix1()
    {
        return six1;
    }

    public void setSix1(String six1)
    {
        this.six1 = six1;
    }

    public String getSix2()
    {
        return six2;
    }

    public void setSix2(String six2)
    {
        this.six2 = six2;
    }

    public String getSix3()
    {
        return six3;
    }

    public void setSix3(String six3)
    {
        this.six3 = six3;
    }

    public String getSix4()
    {
        return six4;
    }

    public void setSix4(String six4)
    {
        this.six4 = six4;
    }

    public String getSix5()
    {
        return six5;
    }

    public void setSix5(String six5)
    {
        this.six5 = six5;
    }

    public String getSix6()
    {
        return six6;
    }

    public void setSix6(String six6)
    {
        this.six6 = six6;
    }

    public String getSeven1()
    {
        return seven1;
    }

    public void setSeven1(String seven1)
    {
        this.seven1 = seven1;
    }

    public String getSeven2()
    {
        return seven2;
    }

    public void setSeven2(String seven2)
    {
        this.seven2 = seven2;
    }

    public String getSeven3()
    {
        return seven3;
    }

    public void setSeven3(String seven3)
    {
        this.seven3 = seven3;
    }

    public String getSeven4()
    {
        return seven4;
    }

    public void setSeven4(String seven4)
    {
        this.seven4 = seven4;
    }

    public String getSeven5()
    {
        return seven5;
    }

    public void setSeven5(String seven5)
    {
        this.seven5 = seven5;
    }

    public String getSeven6()
    {
        return seven6;
    }

    public void setSeven6(String seven6)
    {
        this.seven6 = seven6;
    }

    public String getEight1()
    {
        return eight1;
    }

    public void setEight1(String eight1)
    {
        this.eight1 = eight1;
    }

    public String getEight2()
    {
        return eight2;
    }

    public void setEight2(String eight2)
    {
        this.eight2 = eight2;
    }

    public String getEight3()
    {
        return eight3;
    }

    public void setEight3(String eight3)
    {
        this.eight3 = eight3;
    }

    public String getEight4()
    {
        return eight4;
    }

    public void setEight4(String eight4)
    {
        this.eight4 = eight4;
    }

    public String getEight5()
    {
        return eight5;
    }

    public void setEight5(String eight5)
    {
        this.eight5 = eight5;
    }

    public String getEight6()
    {
        return eight6;
    }

    public void setEight6(String eight6)
    {
        this.eight6 = eight6;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddname=").append(ddname);
        sb.append(", ddname128u=").append(ddname128u);
        sb.append(", ddhour0=").append(ddhour0);
        sb.append(", ddaward0512a=").append(ddaward0512a);
        sb.append(", ddhour1=").append(ddhour1);
        sb.append(", ddaward1512a=").append(ddaward1512a);
        sb.append(", ddhour2=").append(ddhour2);
        sb.append(", ddaward2512a=").append(ddaward2512a);
        sb.append(", ddhour3=").append(ddhour3);
        sb.append(", ddaward3512a=").append(ddaward3512a);
        sb.append(", ddhour4=").append(ddhour4);
        sb.append(", ddaward4512a=").append(ddaward4512a);
        sb.append(", ddhour5=").append(ddhour5);
        sb.append(", ddaward5512a=").append(ddaward5512a);
        sb.append(", ddhour6=").append(ddhour6);
        sb.append(", ddaward6512a=").append(ddaward6512a);
        sb.append(", ddhour7=").append(ddhour7);
        sb.append(", ddaward7512a=").append(ddaward7512a);
        sb.append(", ddhour8=").append(ddhour8);
        sb.append(", ddaward8512a=").append(ddaward8512a);
        sb.append(", ddhour9=").append(ddhour9);
        sb.append(", ddaward9512a=").append(ddaward9512a);
        sb.append(", creatTime=").append(creatTime);
        sb.append(", effectTime=").append(effectTime);
        sb.append(", formatStatus=").append(formatStatus);
        sb.append(", gameType=").append(gameType);
        sb.append("]");
        return sb.toString();
    }

    public String getGameDateSurvey()
    {
        return gameDateSurvey;
    }

    public void setGameDateSurvey(String gameDateSurvey)
    {
        this.gameDateSurvey = gameDateSurvey;
    }
}