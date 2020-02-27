package servlet.login;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbAppConfig;
import db.PeDbInitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CmServletMain;
import servlet.login.api.CmLogin;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = "/initConfig", name = "initConfig")
public class InitConfigServlet extends CmServletMain implements CmLogin
{
    public static final Logger LOG = LoggerFactory.getLogger(InitConfigServlet.class);

    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage)
    {
        JSONObject result = new JSONObject();
        String appId = (String) requestPackage.get("appId");
        String version = (String) requestPackage.get("version");
        LOG.info(requestPackage.toJSONString());
        PeDbInitConfig configsFast = PeDbInitConfig.getConfigsFast(appId, version);
        result.put("result", "success");
        if (configsFast == null)
        {
            PeDbAppConfig appConfig = PeDbAppConfig.getConfigsFast(appId);
            if (appConfig != null)
            {
                boolean isCheck = version.equals(appConfig.ddCheckVersion);
                result.put("turtle", isCheck);
                if (!isCheck)
                    result.put("url", appConfig.ddGameUrl);
                return result;
            }
        }
        if (configsFast != null)
        {
            result.put("turtle", configsFast.ddJokeLogo);
            if (!configsFast.ddJokeLogo)
            {
                result.put("url", configsFast.ddGameUrl);
            }
            return result;
        }
        result.put("turtle", true);
        return result;
    }
}
