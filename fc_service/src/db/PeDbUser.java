package db;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import tool.Log4j;

import javax.persistence.Entity;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author feng
 */
@Entity(name = "user_info")
public class PeDbUser extends PeDbObject implements Serializable
{
    private static Logger LOG = LoggerFactory.getLogger(PeDbUser.class);

    private static final long serialVersionUID = 385011785799306823L;

    //
    // 用户 id 信息
    //
    // 注意，dd 为前缀的变量将自动转换为数据库字段
    //
    // 其中当字段为字符串时：
    // 变量名称直接跟数字表示字符串在数据库中所占空间大小，可选择 64、128、256、512、1024、2048 。
    // 数字后必须继续跟进后缀 a（表示纯英文字符串）或 u（表示需要转换编码字符串），例如 64a、128u 。
    //
    // 默认情况下，不加任何后缀，表示的是一个 64a 字符串。
    //
    public String ddUid = "0";
    //注册openId
    public String ddOId = "0";
    //注册appId
    public String ddAppId = "0";
    //
    // 用户昵称信息
    //
    public String ddName = "0";

    //
    // 用户头像地址
    //
    public String ddAvatarUrl = "0";

    //
    // 用户当前头像框
    //
    public int ddAvatarFrame = 0;

    //
    // 用户当前获得的头像框
    //
    public String ddAvatarFrameGain = "0";

    //
    // 用户性别
    //
    public int ddSex = 0;

    //
    // 用户省份
    //
    public String ddProvince = "0";

    //
    // 用户城市
    //
    public String ddCity = "0";

    //
    // 用户国家
    //
    public String ddCountry = "0";

    //
    // 用户语言
    //
    public String ddLanguage = "0";


    //
    // 用户奖金记录
    //
    public String ddAwardList = "0";


    //
    // 用户（永久）是否已经收藏
    //
    public int ddCollected = 0;

    //
    // 用户（永久）是否已经关注公众号
    //
    public int ddInterested = 0;

    //
    // 用户上次领客服奖励的时间
    //
    public String ddServicedTime = "";

    //
    // 用户上次领取分享奖励的时间
    //
    public String ddSharedTime = "";

    //
    // 用户当日观看视频广告次数
    //
    public int ddDayWatchVideo = 0;

    //
    // 用户每日登录奖励及领取情况
    //
    // 后两位数表示第几日：00=第一日、01=第二日、02=第三日 等等
    // 更高位数，例如 190521：表示上一次领取日期，其中 0 表示从未领取
    //
    public int ddDayLoginGift = 0;

    //
    // 用户首次登录时间
    //
    public Timestamp ddRegisterTime = new Timestamp(System.currentTimeMillis());

    //
    // 用户最新登录时间
    //
    public Timestamp ddLoginTime = new Timestamp(System.currentTimeMillis());

    /**
     * 获取用户打包信息
     */
    public static PeDbUser gainUserObject(String uid)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        PeDbUser userGained = null;

        try
        {
            userGained = (PeDbUser) PeDbUser.queryOneObjectWithSplitName(sqlResource, PeDbUser.class, null, "WHERE ddUid='" + uid + "'");
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return userGained;
    }

    /**
     * 创建一个用户信息
     */
    public static PeDbUser createUser(String uid, String openId, String appId, String name, String avatarUrl, int sex, String province, String city, String country, String language)
    {
        PeDbUser user = new PeDbUser();
        Timestamp date = new Timestamp(System.currentTimeMillis());
        user.ddUid = uid;
        user.ddName = name;
        user.ddAvatarUrl = avatarUrl;
        user.ddSex = sex;
        user.ddProvince = province;
        user.ddCity = city;
        user.ddCountry = country;
        user.ddLanguage = language;
        user.ddRegisterTime = date;
        user.ddLoginTime = date;
        user.ddOId = openId;
        user.ddAppId = appId;
        return user;
    }

    /**
     * 获取用户打包消息
     */
    public void gainUserMessage(JSONObject result)
    {
        result.put("uid", ddUid);
        result.put("name", ddName);
        result.put("avatarUrl", ddAvatarUrl);
        result.put("avatarFrame", ddAvatarFrame);
        result.put("avatarFrameGain", ddAvatarFrameGain);
        result.put("recharge", ddAwardList);
        result.put("sex", ddSex);
        result.put("collected", ddCollected);
        result.put("interested", ddCollected);
        result.put("dayWatchVideo", ddDayWatchVideo);
        //获取数值信息
        UserService.putUserValue(result, ddUid);
    }

    /**
     * 更新数据
     */
    public void update(String filter)
    {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        try
        {
            ddLoginTime = new Timestamp(System.currentTimeMillis());
            filter = filter.concat("#ddLoginTime");
            updateObject(sqlResource, filter, "WHERE ddUid='" + ddUid + "'", true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 初始化用户信息
     */
    public static void init()
    {
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbUser()
    {
        super();
    }
}
