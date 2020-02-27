package com.test;

import com.code.protocols.AbstractRequest;
import com.code.protocols.angling.AnglingBase;
import com.code.protocols.angling.AnglingResultExt;
import com.code.protocols.goods.Shop;
import com.code.protocols.login.Init;
import com.code.protocols.login.other.Logon;
import com.code.protocols.recover.FishOrderInitExt;
import com.code.protocols.recover.RefreshExt.RequestImpl;
import com.code.protocols.work.Activity;
import com.utils.XwhTool;
import com.utils.http.XwhHttp;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import static com.code.protocols.angling.AnglingBase.FishResult;
import static com.code.protocols.angling.AnglingBase.FishingRodConfig;

public class UiTest {
    Logon.ResponseImpl login;
    static String host = "http://192.168.1.55:8980/FishExtServer/";

    public UiTest() {
        //初始化
        sendInit();
        //初始登陆
        sendLogin();
    }

    private void sendInit() {
        String url = "/login/init";
        Init.RequestImpl req = new Init.RequestImpl();
        req.serverver = "v4.0.0";
        String json = sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }

    private void sendLogin() {
        String url = "/login/other";
        Logon.RequestImpl req = new Logon.RequestImpl();
        req.userid = 7;
        String json = sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        login = XwhTool.parseJSONByFastJSON(json, Logon.ResponseImpl.class);
        System.out.println(json);
    }

    private void sendShopList() {
        String url = "/shop/list";
        Shop.ListRequestImpl req = new Shop.ListRequestImpl();
        setBaseReq(req);
        String json = sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }

    private void sendWork() {
        System.out.println("发送活跃任务");
        String uri = "/work/activityList";
        Activity.ListRequest req = new Activity.ListRequest();
        setBaseReq(req);
        req.worktype = Activity.WorkType.all;
        String json = sendPost(host, uri, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
        System.out.println("签到");
        uri = "/work/receive";
        Activity.ReceiveRequest receive = new Activity.ReceiveRequest();
        setBaseReq(receive);
        receive.worktype = Activity.WorkType.reward;
        receive.workid = 2;
        json = sendPost(host, uri, XwhTool.getJSONByFastJSON(receive));
        System.out.println(json);
    }


    private void sendOrder() {
        String uri = "/order/init";
        FishOrderInitExt.RequestImpl req = new FishOrderInitExt.RequestImpl();
        setBaseReq(req);
        String json = sendPost(host, uri, XwhTool.getJSONByFastJSON(req));
        FishOrderInitExt.ResponseImpl res = XwhTool.parseJSONByFastJSON(json, FishOrderInitExt.ResponseImpl.class);
        System.out.println(json);
        sendRefreshOrder(res);
    }

    private void sendRefreshOrder(FishOrderInitExt.ResponseImpl res) {
        System.out.println("refresh");
        String uri = "/order/refresh";
        RequestImpl req = new RequestImpl();
        req.index = res.orders.firstElement().index;
        setBaseReq(req);
        String json = sendPost(host, uri, XwhTool.getJSONByFastJSON(req));
        System.out.println(json);
    }

    private void setBaseReq(AbstractRequest req) {
        req.logincode = login.logincode;
        req.userid = login.userid;
    }

    public void logic() {
        AnglingResultExt.RequestImpl req = new AnglingResultExt.RequestImpl();
        setBaseReq(req);
        req.fishes = new Vector<>();
        String[] skins = new String[]{"nanxing", "nvxing", "nanxing02", "nvxing02"};
        for (int i = 0; i < 4; i++) {

            FishResult element = new FishResult();
            element.ftid = ThreadLocalRandom.current().nextInt(57);
            req.fishes.add(element);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = "/angling/result";
        String json = sendPost(host, url, XwhTool.getJSONByFastJSON(req));
        //        AnglingInitExt.ResponseImpl res = XwhTool.parseJSONByFastJSON(json, AnglingInitExt.ResponseImpl.class);
        System.out.println("angling result !" + json);
    }

    public static void main(String[] args) {
//        try {
//            UiTest test = new UiTest();
//            //            for (int i = 0; i < 10; i++)
//            //            {
//            //                test.logic();
//            //            }
//            test.sendWork();
//            //            test.sendFriendEnterList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        FishingRodConfig rod = new AnglingBase.FishingRodConfig();
        rod.frequency = 12;
        rod.cost = 300;
        rod.video = 3;
        rod.endurance = 5;
        System.out.println(XwhTool.getJSONByFastJSON(rod));
    }

    public static String sendPost(String host, String uri, String send) {
        String post = null;
        try {
            URL url = new URL(host + uri);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setConnectTimeout(60000);
            httpConn.setReadTimeout(30000);

            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "text/xml");
            httpConn.setRequestProperty("token", uri);
            httpConn.setRequestProperty("Charset", "utf-8");
            if (send != null) {
                byte[] buffer = new BASE64Encoder().encode(send.getBytes("utf-8")).getBytes();
                httpConn.setRequestProperty("Content-length", String.valueOf(buffer.length));
                OutputStream out = new DataOutputStream(httpConn.getOutputStream());
                out.write(buffer);
                out.flush();
                out.close();
            }
            XwhHttp.ResponseCode responseCode = new XwhHttp.ResponseCode();
            responseCode.code = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode.code) {
                byte[] readBytes = new byte[4 * 1024];
                int readed = 0;
                InputStream inStream = httpConn.getInputStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while ((readed = inStream.read(readBytes)) != -1) {
                    byteStream.write(readBytes, 0, readed);
                }
                byte[] result = byteStream.toByteArray();
                post = new String(new BASE64Decoder().decodeBuffer(new String(result, "utf-8")));
                byteStream.close();
                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

}
