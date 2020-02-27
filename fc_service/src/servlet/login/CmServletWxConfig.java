package servlet.login;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbWxConfig;
import servlet.CmServletMain;
import tool.CmTool;
import tool.Log4j;
import tool.db.RedisUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@WebServlet(urlPatterns = "/getInfoMation")
public class CmServletWxConfig extends CmServletMain
{
    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param sqlResource    数据库资源句柄
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        JSONObject result = new JSONObject();
        String appId = requestPackage.getString("appId");
        String url = requestPackage.getString("url");
        PeDbWxConfig wxConfig = PeDbWxConfig.getConfigFast(appId);
        if (wxConfig == null)
        {
            result.put("result", "fail");
            result.put("msg", "appId is empty!");
            return result;
        }
        result.put("result", "success");
        try
        {
            String token = getAccessToken(wxConfig);
            String ticket = getWeiXinJsapi_Ticket(wxConfig.ddAppId, token);
            String nonceStr = CmTool.createNonceStr();
            long timeStamp = System.currentTimeMillis() / 1000;
            result.put("appId", appId);
            result.put("ticket", ticket);
            result.put("nonceStr", nonceStr);
            result.put("timestamp", timeStamp);
            StringBuilder out = new StringBuilder();
            out.append("jsapi_ticket=").append(ticket).append("&noncestr=").append(nonceStr).append("&timestamp=").append(timeStamp).append("&url=").append(url);
            String signature = CmTool.SHA1(out.toString());

            result.put("out", out.toString());
            result.put("signature", signature);
            return result;
        } catch (Exception ex)
        {
            SERVLET_LOG.error(Log4j.getExceptionInfo(ex));
        }
        return result;
    }

    /**
     * 获取好友token数据
     *
     * @param wxConfig 配置
     * @return 参数
     */
    private String getAccessToken(PeDbWxConfig wxConfig)
    {
        String accessToken = RedisUtils.hget("access-token", wxConfig.ddAppId);
        long now = System.currentTimeMillis();
        if (accessToken != null)
        {
            JSONObject token = JSONObject.parseObject(accessToken);
            int expires_in = token.getInteger("expires_in");
            if (now - token.getLong("now") / 1000 < expires_in)
                return token.getString("access_token");
        }
        String tokenUrl = MessageFormat.format("https://api.weixin.qq.com/cgi-bin/token?grant_type={0}&appid={1}&secret={2}", "client_credential", wxConfig.ddAppId, wxConfig.ddAppSecret);
        accessToken = CmTool.makeHttpConnect(tokenUrl);
        SERVLET_LOG.debug("token=" + accessToken + ",tokenUrl=" + tokenUrl);
        JSONObject token = JSONObject.parseObject(accessToken);
        if (token.containsKey("access_token"))
        {
            token.put("now", now);
            RedisUtils.hset("access-token", wxConfig.ddAppId, token.toJSONString());
        }
        return token.getString("access_token");
    }

    /**
     * 获取ticket
     *
     * @param accessToken token
     * @return ticket
     */
    private String getWeiXinJsapi_Ticket(String appId, String accessToken)
    {
        String ticket = RedisUtils.hget("js-ticket", appId);
        long now = System.currentTimeMillis();
        if (ticket != null)
        {
            JSONObject token = JSONObject.parseObject(ticket);
            int expires_in = token.getInteger("expires_in");
            if (now - token.getLong("now") / 1000 < expires_in)
                return token.getString("ticket");
        }
        String ticketUrl = MessageFormat.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type={1}", accessToken, "jsapi");
        ticket = CmTool.makeHttpConnect(ticketUrl);
        SERVLET_LOG.debug("ticket=" + ticket + ",tokenUrl=" + ticketUrl);
        JSONObject token = JSONObject.parseObject(ticket);
        if (token.containsKey("ticket"))
        {
            token.put("now", now);
            RedisUtils.hset("js-ticket", appId, token.toJSONString());
        }
        return token.getString("ticket");
    }
}
