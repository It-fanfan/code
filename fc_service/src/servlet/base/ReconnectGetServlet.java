package servlet.base;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbConfigConfirm;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 进行重连响应
 *
 * @author xuwei
 */
@WebServlet(urlPatterns = "/reconnectGet")
public class ReconnectGetServlet extends CmServletMain implements Serializable {
    /**
     * 街机服务器进行与客户端额外重连处理
     * 类型：
     */
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject requestPackage) {
        JSONObject result = new JSONObject();
        result.put("result", "success");
        String type = requestPackage.getString("type");
        switch (type) {
            //二维码请求
            case "rq": {
                PeDbConfigConfirm confirm = PeDbConfigConfirm.getConfirm(type);
                if (confirm != null) {
                    result.put("friendUrl", confirm.ddStatus ? confirm.ddYes : confirm.ddNo);
                    result.put("groupStatus", confirm.ddStatus);
                }
            }
            break;
            default:
                break;
        }
        return result;
    }
}
