package servlet.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbAreaConfig;
import db.PeDbGame;
import servlet.CmServletMain;
import tool.CmTool;
import tool.db.RedisUtils;
import tool.ipsearch.IPLocation;
import tool.ipsearch.IPSeeker;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(urlPatterns = "/query/match")
public class CmServletQueryMatch extends CmServletMain
{
    //缓存数据
    private static final String REDIS_KEY = "match-service";

    //进行匹配查询
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        JSONObject result = new JSONObject();
        result.put("result", "fail");
        String uid = requestPackage.getString("uid");
        //对应游戏编号
        int gameCode = requestPackage.getInteger("gameCode");
        //是否存在房间记录
        JSONObject room = requestPackage.getJSONObject("room");
        JSONObject service = getServiceInfo();
        JSONObject config = getServiceConfig();
        if (service != null)
        {
            JSONObject match = null;
            if (room != null)
            {
                //进行检测房间的服务是否存在
                String addr = room.getString("matchAddr");
                for (Map.Entry<String, Object> entry : service.entrySet())
                {
                    JSONObject aresData = (JSONObject) entry.getValue();
                    if (aresData.containsKey(addr))
                    {
                        match = aresData.getJSONObject(addr);
                        break;
                    }
                }
                //正常匹配用户信息
                if (match != null && !config.containsKey(addr))
                {
                    result.put("result", "success");
                    result.put("matchAddr", match.getString("key"));
                    result.put("matchPort", match.getString("port"));
                    return result;
                }
            }
        }
        //通过IP获取地区编号
        String ares = getUserAres(requestObject);
        //获取地区服务器
        PeDbGame game = PeDbGame.getGameFast(gameCode);
        if (game == null)
        {
            result.put("result", "fail");
            result.put("msg", "非法游戏房间");
            return result;
        }
        boolean isPk = game.ddIsPk == 1;
        String type = isPk ? "pk" : "through";
        JSONObject aresData = getServiceAres(ares, service, type);
        JSONObject matchService = getMatchService(aresData, type, config);
        if (matchService != null)
        {
            result.put("result", "success");
            result.put("matchAddr", matchService.getString("key"));
            result.put("matchPort", matchService.getString("port"));
        }
        return result;
    }

    /**
     * 获取对应匹配服
     *
     * @param aresData 地区服务器组
     * @param type     房间配置
     * @param config   关停配置
     * @return 下发服务器节点
     */
    private JSONObject getMatchService(JSONObject aresData, String type, JSONObject config)
    {
        if (aresData == null)
            return null;
        int allWeight = 0;
        Vector<JSONObject> matches = new Vector<>();
        //通过权重进行分配
        for (Map.Entry<String, Object> entry : aresData.entrySet())
        {
            String key = entry.getKey();
            if (config.containsKey(key) && config.getBoolean(key))
                continue;
            JSONObject matchService = (JSONObject) entry.getValue();
            //权重值
            int weight = matchService.getInteger("weight");
            //房间数
            Integer buzy = matchService.getInteger("buzy-" + type);
            Integer allTotal = matchService.getInteger(type);
            int rooms = 0;
            if (allTotal != null)
                rooms = allTotal;
            if (buzy != null)
                rooms -= buzy;
            int value = weight * rooms;
            matchService.put("temp-weight", value);
            allWeight += value;
            matches.add(matchService);
        }
        if (matches.isEmpty())
            return null;
        if (matches.size() == 1)
            return matches.firstElement();
        int random = ThreadLocalRandom.current().nextInt(allWeight);
        int temp = 0;
        for (JSONObject match : matches)
        {
            int value = match.getInteger("temp-weight");
            if (random >= temp && random <= temp + value)
            {
                return match;
            }
            temp += value;
        }
        return matches.firstElement();
    }

    /**
     * 进行获取地区分配
     *
     * @param ares    地区编号
     * @param service 服务配置
     */
    private JSONObject getServiceAres(String ares, JSONObject service, String type)
    {
        if (ares == null || ares.trim().isEmpty())
            return service.getJSONObject("0");
        for (String key : ares.split("#"))
        {
            JSONObject match = service.getJSONObject(key);
            if (match != null)
            {
                for (Map.Entry<String, Object> entry : match.entrySet())
                {
                    JSONObject matchService = (JSONObject) entry.getValue();
                    //权重值
                    int weight = matchService.getInteger("weight");
                    Integer allTotal = matchService.getInteger(type);
                    //存在權重，并且縂房間数分配大于0
                    if (weight > 0 && allTotal != null && allTotal > 0)
                        return match;
                }
            }
        }
        //最终默认地区参数
        return service.getJSONObject("0");
    }

    /**
     * 获取省份匹配区域
     *
     * @param request 请求类型
     * @return 地区
     */
    private String getUserAres(HttpServletRequest request)
    {
        String ip = CmTool.getIP(request);
        IPLocation zone = IPSeeker.getInstance().getIP(ip);
        PeDbAreaConfig areaConfig = PeDbAreaConfig.getConfigsFast(zone.getCountry());
        return areaConfig.ddArea;
    }

    /**
     * 获取服务配置
     */
    private static JSONObject getServiceInfo()
    {
        String str = RedisUtils.hget(REDIS_KEY, "service");
        JSONObject service = new JSONObject();
        if (str != null)
            service = JSONObject.parseObject(str);
        return service;
    }

    /**
     * 获取服务配置
     */
    private JSONObject getServiceConfig()
    {
        String configStr = RedisUtils.hget(REDIS_KEY, "config");
        JSONObject config = new JSONObject();
        if (configStr != null)
            config = JSONObject.parseObject(configStr);
        return config;
    }

    /**
     * 获取在线人数
     *
     * @return 在线用户数
     */
    public static int getOnlineUserCount(String matchKey)
    {
        String json = RedisUtils.hget(REDIS_KEY, "online");
        if (json != null)
        {
            JSONObject data = JSON.parseObject(json);
            if (data != null && data.containsKey(matchKey))
                return data.getInteger(matchKey);
        }
        return 0;
    }
}
