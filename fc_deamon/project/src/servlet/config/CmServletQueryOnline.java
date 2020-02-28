package servlet.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import pipe.CmPipeServiceDemon;
import pipe.CmPipeSocket;
import servlet.CmServletMain;
<<<<<<< HEAD
import tool.db.RedisUtils;
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

<<<<<<< HEAD
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
=======
@WebServlet(urlPatterns = "/query/online")
public class CmServletQueryOnline extends CmServletMain
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        return queryOnline();
    }

    private JSONObject queryOnline()
    {
        JSONObject result = new JSONObject();
        result.put("result", "success");
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

>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
<<<<<<< HEAD
        out.println(queryOnline(request.getParameter("type"), request.getParameter("roomSeq")).toJSONString());
=======
        out.println(queryOnline().toJSONString());
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        out.flush();
        out.close();
    }
}
