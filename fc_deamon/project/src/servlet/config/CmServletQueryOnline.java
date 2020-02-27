package servlet.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import pipe.CmPipeServiceDemon;
import pipe.CmPipeSocket;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println(queryOnline().toJSONString());
        out.flush();
        out.close();
    }
}
