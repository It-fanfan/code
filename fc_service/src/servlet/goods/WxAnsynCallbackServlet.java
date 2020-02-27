package servlet.goods;

import db.CmDbSqlResource;
import service.PayService;
import servlet.CmServletMain;
import tool.Log4j;
import tool.XMLHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

@WebServlet(urlPatterns = "/wx/ansyn", name = "weChat Ansyn")
public class WxAnsynCallbackServlet extends CmServletMain implements Serializable
{
    /**
     * 获取格式内容
     */
    protected String getContextType()
    {
        return "xml";
    }

    protected String handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, String content)
    {
        try
        {
            SERVLET_LOG.warn("WxAnsynCallbackServlet:" + content);
            XMLHandler result_xml = XMLHandler.parse(content);
            Map<String, String> xmlMap = result_xml.getXmlMap();

            String appId = xmlMap.get("appid");
            PayService service = new PayService(appId);
            service.wxAnsynCallBack(xmlMap);
        } catch (Exception e)
        {
            SERVLET_LOG.error(Log4j.getExceptionInfo(e));
        }
        return getWxResponse();
    }

    /**
     * 获取最终XML流信息
     */
    private static String getWxResponse()
    {
        return "<xml>\n<return_code><![CDATA[SUCCESS]]></return_code>\n<return_msg><![CDATA[OK]]></return_msg>\n</xml>";
    }
}
