package com.code.servlet.pay.vivo;

import com.alibaba.fastjson.JSONObject;
import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.entity.goods.GoodsValue;
import com.code.dao.sdk.RecordPay;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.login.LoginRequest;
import com.code.protocols.sdk.Vivo;
import com.code.service.goods.ShopService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;
import com.utils.http.XwhHttp;
import com.utils.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

@AvoidRepeatableCommit(filter = false)
@WebServlet(urlPatterns = "/pay/vivo/prepayment", name = "vivo pay prepayment")
public class VivoPrepaymentServlet extends ServletMain<Vivo.PayRequest, Vivo.PaySignResponse> {
    private UserCache userCache;
    private static final String APP_SECRET = "4b7a94a2aaa79db3061ad117ee9d43b9";

    @Override
    protected Vivo.PaySignResponse doLogic(Vivo.PayRequest pay, HttpServletRequest request, HttpServletResponse response) {
        GoodsValue goods = ShopService.getGoodsValue(pay.goodsid);

        String signMethod = "MD5";
        String version = "1.0.0";
        String packageName = "com.blazefire.fish.vivo";
        String orderId = "";
        String random = getRandomString(3);
        long l = System.currentTimeMillis();
        orderId = random + "_" + l;
        BigDecimal price = new BigDecimal(goods.getRmb() * pay.total).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN);
        Timestamp startTime = goods.getStartTime();
        String orderTime = XwhTool.getFormaterTime(System.currentTimeMillis(), "yyyyMMddHHmmss");
        String orderTitle = goods.getGoodsName();
        String orderDesc = goods.getGoodsName();
        Vivo.PaySignResponse res = new Vivo.PaySignResponse();
        try {
            RecordPay recordPay = new RecordPay();
            recordPay.setGoodsId(pay.goodsid);
            recordPay.setInsertTime(new Timestamp(System.currentTimeMillis()));
            recordPay.setMoney(price);
            recordPay.setOrderId(orderId);
            recordPay.setPayType(LoginRequest.Platform.vivo.name());
            recordPay.setPlatform(pay.platform.name());
            recordPay.setTransTime(startTime);
            recordPay.setUserId(Integer.valueOf(pay.userid));
            FishInfoDao.saveOrUpdate(recordPay, true);
        } catch (Exception e) {
            STAR.error(Log4j.getExceptionInfo(e));
        }
        JSONObject paraMap = new JSONObject();
        paraMap.put("version", version);
        paraMap.put("signMethod", signMethod);
        paraMap.put("packageName", packageName);
        paraMap.put("cpOrderNumber", orderId);
//      paraMap.put("notifyUrl", "notifyUrl");
        paraMap.put("orderTime", orderTime);
        paraMap.put("orderAmount", price);
        paraMap.put("orderTitle", orderTitle);
        paraMap.put("orderDesc", orderDesc);
        String sign = createSign(paraMap);
        paraMap.put("signature", sign);
        System.out.println(paraMap);
        try {
            res.param = XwhHttp.sendPostByGson("https://pay.vivo.com.cn/vivopay/order/request", paraMap);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return res;
    }

    /**
     * 生成签名
     */
    private static String createSign(JSONObject jsonObject) {
        Map<String, Object> signMap = jsonObject.getInnerMap();
        Vector<String> keys = new Vector<>(signMap.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        int len = sb.length();
        for (String key : keys) {
            if (len != sb.length())
                sb.append("&");
            sb.append(key).append("=").append(jsonObject.get(key));
        }

        String appKey = DigestUtils.md5Hex(APP_SECRET.getBytes(StandardCharsets.UTF_8));
        String signStr = sb.append(appKey.toLowerCase()).toString().toLowerCase();
        String sign = DigestUtils.md5Hex(signStr.getBytes(StandardCharsets.UTF_8));
        System.out.println("appKey=" + appKey + ",signStr=" + signStr + ",sign=" + sign);
        return sign;
    }

    private static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
