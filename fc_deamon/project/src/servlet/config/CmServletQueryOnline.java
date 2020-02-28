package servlet.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import pipe.CmPipeServiceDemon;
import pipe.CmPipeSocket;
import servlet.CmServletMain;
import tool.db.RedisUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/query/online")
public class CmServletQueryOnline extends CmServletMain
{
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        return queryOnline(requestPackage.getString("type"), requestPackage.getString("roomSeq"));
    }

    /**
     * 查询类型
     *
     * @param type 查询参数
     * @return 节点数据
     */
    private JSONObject queryOnline(String type, String roomSeq)
    {
        if (type == null)
        {
            type = "none";
        }
        JSONObject result = new JSONObject();
        result.put("result", "success");

        switch (type)
        {
            case "ares":
            {
                result.put("ares", RedisUtils.hget(CmPipeServiceDemon.REDIS_KEY, "service"));
                return result;
            }
            case "roomSeq":
            {
                result.put("roomSeq", RedisUtils.get(type + "-" + roomSeq));
                return result;
            }
            case "none":
            default:
            {
                JSONArray server = new JSONArray();
                Vector<CmPipeSocket> sockets = CmPipeServiceDemon.getSockets();
                sockets.forEach(socket ->
                {
                    String k = socket.getSocketKey();
                    JSONArray v = socket.getPkRoom();
                    JSONObject object = new JSONObject();
                    object.put("ip", k);
                    object.put("data", v);
                    server.add(object);
                });
                result.put("server", server);
                return result;
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println(queryOnline(request.getParameter("type"), request.getParameter("roomSeq")).toJSONString());
        out.flush();
        out.close();
    }
}
