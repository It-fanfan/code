package com.test;

import com.code.protocols.midas.GetBalance;
import com.code.protocols.midas.WxToken;
import com.utils.ReadConfig;
import com.utils.XwhTool;
import com.utils.http.XwhHttp;

public class TokenTest
{
    static String host = "http://43.254.45.69:11120/Midas";

    public TokenTest()
    {
        //初始化
        sendToken();
    }

    private void sendToken()
    {
        String url = "/midas/token";
        WxToken.RequestImpl req = new WxToken.RequestImpl();
        req.appId = ReadConfig.get("appid");
        req.appSecret = ReadConfig.get("secret");
        String json = XwhHttp.sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }

    private void sendBalance(String openid, String sessionKey)
    {
        String url = "/midas/getBalance";
        GetBalance.RequestImpl req = new GetBalance.RequestImpl();
        req.appId = ReadConfig.get("appid");
        req.openid = openid;
        req.sessionKey = sessionKey;
        String json = XwhHttp.sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }

    public static void main(String[] args)
    {
        try
        {
            TokenTest test = new TokenTest();
            String opneId = "ocymP4pulgfE6q5PJ30pAIiYsll4";//loginCode.getOpenId();
            String sessionKey = "NAiNIs3iVftW2kU/KfzUTQ==";// loginCode.getWeChatSessionKey();
            test.sendBalance(opneId, sessionKey);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
