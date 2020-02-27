package com.test;

import com.code.servlet.operate.ext.ActionTypeExt;
import com.code.servlet.operate.ext.Refresh;
import com.utils.XwhTool;

import java.util.Vector;

public class UserOperatorTest {
    static String host = "https://cloudfishv4.gamesmvp.com/FishExtServer";

    public static void main(String[] arg) {
        sendRefresh(3);
    }

    /**
     * 給定贝壳
     *
     * @param shell 贝壳数
     */
    private static void sendShell(long shell) {
        String url = "/test/addfish";
        ActionTypeExt.RequestImpl req = new ActionTypeExt.RequestImpl();
        req.userid = 1;
        req.entities = new Vector<>();
        ActionTypeExt.OperateEntity entity = new ActionTypeExt.OperateEntity();
        entity.type = ActionTypeExt.ActionType.shell;
        entity.value = shell;
        req.entities.add(entity);
        String json = UiTest.sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }

    private static void sendRefresh(long userId) {
        String uri = "/operate/refresh";
        Refresh.Request req = new Refresh.Request();
        req.isall = true;
        req.userids = new Vector<>();
        req.userids.add(userId);
        String json = UiTest.sendPost(host, uri, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }
}
