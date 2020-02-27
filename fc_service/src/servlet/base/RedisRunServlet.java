package servlet.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import service.UserService;
import servlet.CmServletMain;
import tool.db.RedisUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@WebServlet(urlPatterns = "/redis/run")
public class RedisRunServlet extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        JSONObject result = new JSONObject();
        String type = requestPackage.getString("redis-type");
        switch (type)
        {
            case "remove":
            {
                //获取清理表格
                JSONArray tables = requestPackage.getJSONArray("tables");
                //获取清理redis key
                JSONArray redisKey = requestPackage.getJSONArray("redis-key");
                //获取执行主键字段
                String key = requestPackage.getString("key");
                //获取主键值
                String value = requestPackage.getString("value");
                for (int i = 0; i < tables.size(); i++)
                {
                    String SQL = "delete from " + tables.getString(i) + " where " + key + "= \"" + value + "\"";
                    CmDbSqlResource.instance().execSQLCMDInfo(SQL);
                }
                //进行移除redis
                String[] keys = new String[redisKey.size()];
                for (int i = 0; i < redisKey.size(); i++)
                {
                    keys[i] = redisKey.getString(i);
                }
                RedisUtils.del(keys);
                result.put("message", "成功清理" + value + "相关数据:redis:" + redisKey.toJSONString() + ",表:" + tables.toJSONString());
            }
            break;
            case "add":
            {
                //新增用户值
                String uid = requestPackage.getString("uid");
                int addValue = requestPackage.getIntValue("addCoin");
                JSONObject extra = new JSONObject();
                extra.put("type", "run");
                //复活次数
                String appId = requestPackage.getString("appId");
                extra.put("appId", appId);
                long current = UserService.addValue(uid, "coin", addValue, extra);
                result.put("coin", current);
                result.put("message", "当前玩家金币数:" + current);
            }
            break;
            case "cmd":
            {
                //新增用户值
                String uid = requestPackage.getString("uid");
                JSONObject cmd = requestPackage.getJSONObject("cmd");
                for (Map.Entry<String, Object> entry : cmd.entrySet())
                {
                    UserService.setCache(uid, entry.getKey(), entry.getValue().toString());
                }
                result.put("message", "执行命令结束!");
            }
            break;
        }

        result.put("result", "success");
        return result;
    }
}
