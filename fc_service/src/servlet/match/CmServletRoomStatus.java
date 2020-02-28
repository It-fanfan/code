package servlet.match;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import servlet.CmServletMain;
import tool.Log4j;
import tool.db.RedisUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/query/room")
public class CmServletRoomStatus extends CmServletMain {

    private String getUid() {
        return "uid";
    }

    private String getRoomSeq() {
        return "roomSeq";
    }

    private String getMatchKey() {
        return "matchKey";
    }

    private String getGameCode() {
        return "gameCode";
    }

    private String getAppId() {
        return "appId";
    }

    private String getStatus() {
        return "status";
    }

    private String getFinishResult() {
        return "finishResult";
    }

    private String getVpUserFinish() {
        return "vpUserFinish";
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        String uid = requestPackage.getString(getUid());
        String roomSeq = requestPackage.getString(getRoomSeq());
        String matchKey = requestPackage.getString(getMatchKey());
        int gameCode = requestPackage.getInteger(getGameCode());
        String status = requestPackage.getString(getStatus());
        //获取房间缓存数据
        String roomStr = RedisUtils.get(getRoomSeq() + "-" + roomSeq);
        JSONObject result = new JSONObject();
        if (roomStr == null) {
            result.put("result", "fail");
            result.put("msg", "invalid room");
            return result;
        }
        try {
            JSONObject roomCache = JSONObject.parseObject(roomStr);
            if (roomCache == null) {
                result.put("result", "fail");
                result.put("msg", "invalid room str");
                return result;
            }
            //游戏编号,赛场编号 不匹配
            if (gameCode != roomCache.getInteger(getGameCode()) || !matchKey.equals(roomCache.getString(getMatchKey()))) {
                result.put("result", "fail");
                result.put("msg", "mismatching room");
                return result;
            }
            result.put("status", roomCache.getString("status"));
            switch (status) {
                case "state": {
                    JSONObject isSingle = roomCache.getJSONObject("isSingle");
                    if (isSingle != null) {
                        result.put("isSingle", isSingle.getBoolean(uid));
                    }
                    result.put("gameMode", roomCache.get("gameMode"));
                    result.put("result", "success");
                    return result;
                }
                case "finish": {
                    if (getRoomFinish(uid, result, roomCache)) {
                        return result;
                    }
                }
                break;
                default:
                    break;
            }
            result.put("result", "success");
        } catch (Exception e) {
            SERVLET_LOG.error(Log4j.getExceptionInfo(e));
            result.put("result", "fail");
            result.put("msg", "invalid room");
            return result;
        }
        return result;
    }

    /**
     * 获取房间状态为完成
     *
     * @param uid       用户编号
     * @param result    下发结果
     * @param roomCache 房间状态
     * @return 是否状态
     */
    private boolean getRoomFinish(String uid, JSONObject result, JSONObject roomCache) {
        JSONArray vpRecords = roomCache.getJSONArray("vpRecords");
        if (vpRecords == null) {
            result.put("result", "fail");
            result.put("msg", "This room doesn't belong to you!");
            return true;
        }
        boolean isTrue = false;
        for (int i = 0; i < vpRecords.size(); i++) {
            String temp = vpRecords.getString(i);
            if (uid.equals(temp)) {
                isTrue = true;
                break;
            }
        }
        if (!isTrue) {
            result.put("result", "fail");
            result.put("msg", "This room doesn't belong to you!");
            return true;
        }
        result.put("result", "success");
        //总结果信息
        if (roomCache.containsKey(getFinishResult())) {
            result.put(getFinishResult(), roomCache.getString(getFinishResult()));
        }
        if (result.containsKey(getFinishResult())) {
            return true;
        }
        //单结果信息
        if (roomCache.containsKey(getVpUserFinish())) {
            JSONArray vpUserFinish = roomCache.getJSONArray(getVpUserFinish());
            for (int i = 0; i < vpUserFinish.size(); i++) {
                JSONObject data = vpUserFinish.getJSONObject(i);
                String tempUid = data.getString(getUid());
                if (tempUid.equals(uid)) {
                    result.put(getFinishResult(), data.getJSONObject(getFinishResult()));
                    return true;
                }
            }
        }
        return false;
    }
}
