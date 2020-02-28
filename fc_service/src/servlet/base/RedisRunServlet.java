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

<<<<<<< HEAD
/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/redis/run")
public class RedisRunServlet extends CmServletMain {
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        JSONObject result = new JSONObject();
        String type = requestPackage.getString("redis-type");
        switch (type) {
            case "remove": {
=======
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
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                //获取清理表格
                JSONArray tables = requestPackage.getJSONArray("tables");
                //获取清理redis key
                JSONArray redisKey = requestPackage.getJSONArray("redis-key");
                //获取执行主键字段
                String key = requestPackage.getString("key");
                //获取主键值
                String value = requestPackage.getString("value");
<<<<<<< HEAD
                for (int i = 0; i < tables.size(); i++) {
=======
                for (int i = 0; i < tables.size(); i++)
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    String SQL = "delete from " + tables.getString(i) + " where " + key + "= \"" + value + "\"";
                    CmDbSqlResource.instance().execSQLCMDInfo(SQL);
                }
                //进行移除redis
                String[] keys = new String[redisKey.size()];
<<<<<<< HEAD
                for (int i = 0; i < redisKey.size(); i++) {
=======
                for (int i = 0; i < redisKey.size(); i++)
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    keys[i] = redisKey.getString(i);
                }
                RedisUtils.del(keys);
                result.put("message", "成功清理" + value + "相关数据:redis:" + redisKey.toJSONString() + ",表:" + tables.toJSONString());
            }
            break;
<<<<<<< HEAD
            case "add": {
=======
            case "add":
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
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
<<<<<<< HEAD
            case "cmd": {
                //新增用户值
                String uid = requestPackage.getString("uid");
                JSONObject cmd = requestPackage.getJSONObject("cmd");
                for (Map.Entry<String, Object> entry : cmd.entrySet()) {
=======
            case "cmd":
            {
                //新增用户值
                String uid = requestPackage.getString("uid");
                JSONObject cmd = requestPackage.getJSONObject("cmd");
                for (Map.Entry<String, Object> entry : cmd.entrySet())
                {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                    UserService.setCache(uid, entry.getKey(), entry.getValue().toString());
                }
                result.put("message", "执行命令结束!");
            }
            break;
<<<<<<< HEAD
            default:
                break;
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        }

        result.put("result", "success");
        return result;
    }
}
