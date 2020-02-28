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

/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/wx/ansyn", name = "weChat Ansyn")
public class WxAnsynCallbackServlet extends CmServletMain implements Serializable
{
    /**
     * 获取格式内容
     */
    @Override
    protected String getContextType()
    {
        return "xml";
    }

    @Override
    protected String handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, String content)
    {
        try
        {
            SERVLET_LOG.warn("WxAnsynCallbackServlet:" + content);
            XMLHandler resultXml = XMLHandler.parse(content);
            Map<String, String> xmlMap = resultXml.getXmlMap();

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
