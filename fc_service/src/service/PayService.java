package service;

import com.alibaba.fastjson.JSONObject;
import config.CmProjectConfig;
import config.ReadConfig;
import config.WxConfig;
import db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CmTool;
import tool.Log4j;
import tool.SignatureAlgorithm;
import tool.XMLHandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static tool.CmTool.createNonceStr;

/**
 * 支付服務
 */
<<<<<<< HEAD
public class PayService {
=======
public class PayService
{
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    private static final Logger LOG = LoggerFactory.getLogger(PayService.class);
    private static final Logger PAY = LoggerFactory.getLogger("MoneyLog");
    // 签名类型
    private static final String SIGN_TYPE = "MD5";

    //查单接口地址
    private static final String QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    private PeDbWxConfig config;

<<<<<<< HEAD
    public PayService(String appId) {
=======
    public PayService(String appId)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        this.config = PeDbWxConfig.getConfigFast(appId);
    }

    /**
     * 响应支付信息 (再次签名)
     *
     * @param signMap 参数
     */
<<<<<<< HEAD
    private Map<String, String> signResponse(Map<String, String> signMap) {
=======
    private Map<String, String> signResponse(Map<String, String> signMap)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        // 支付参数和sign
        Map<String, String> result = new HashMap<>();

        long timeStamp = System.currentTimeMillis();
        String nonceStr = createNonceStr();
        String packge = signMap.get("prepay_id");

        result.put("appId", config.ddAppId);
        result.put("timeStamp", String.valueOf(timeStamp / 1000));
        result.put("nonceStr", nonceStr);
        result.put("package", "prepay_id=" + packge);
        result.put("signType", SIGN_TYPE);

        // 签名
        SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm(config.ddKey, result);
        String createSign = signatureAlgorithm.createSign();
        result.put("sign", createSign);
        System.out.println(result);
        return result;
    }

    /**
     * 预支付请求
     *
     * @param request 请求参数
     * @return 返回参数结果
     */
<<<<<<< HEAD
    public JSONObject prePay(JSONObject request) {
=======
    public JSONObject prePay(JSONObject request)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject response = new JSONObject();
        //查找商品
        int goodsId = request.getInteger("goodsId");
        PeDbGoodsValue goods = PeDbGoodsValue.getGoodsFast(goodsId);
        //查找用户openid
        String ddUid = request.getString("uid");
<<<<<<< HEAD
        if (goods == null) {
=======
        if (goods == null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            response.put("result", "goods is empty!");
            return response;
        }

<<<<<<< HEAD
        try {
            // 预支付请求,按照分保存
            int totalFee = new BigDecimal(100).multiply(goods.ddPrice).intValue();
            String openId = UserService.getOpenId(ddUid, config.ddAppId);
            JSONObject create = prePayOrderCreate(totalFee, openId, goods.ddName, ddUid + "#" + goodsId);
            System.out.println("create=" + create);
            if (create.getString("result").equals("success")) {
=======
        try
        {
            // 预支付请求,按照分保存
            int total_fee = new BigDecimal(100).multiply(goods.ddPrice).intValue();
            String openId = UserService.getOpenId(ddUid, config.ddAppId);
            JSONObject create = prePayOrderCreate(total_fee, openId, goods.ddName, ddUid + "#" + goodsId);
            System.out.println("create=" + create);
            if (create.getString("result").equals("success"))
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                //进行保存数据库
                PeDbOrder order = PeDbOrder.createOrder(create.getString("orderid"), ddUid, openId, config.ddAppId, goodsId, "weChat", config.ddMchId, goods.ddPrice);
                order.insertObject(CmDbSqlResource.instance());
            }
            return create;
<<<<<<< HEAD
        } catch (Exception e) {
=======
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return response;
    }

    /**
     * 检测是否匹配
     *
     * @param map 内容
     * @param key 查询参数
     * @return 是否匹配
     */
<<<<<<< HEAD
    private static boolean existResult(Map<String, String> map, String key) {
=======
    private static boolean existResult(Map<String, String> map, String key)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        String resultCode = map.get(key);
        return "success".equalsIgnoreCase(resultCode);
    }

    /**
     * 响应客户端 请求 (进行预支付---->返回支付参数)
     *
     * @param total_fee 金额
     * @param openid    用户编号
     * @param body      支付内容
     * @param attach    携带参数
     * @return 预订单支付结果
     */
<<<<<<< HEAD
    private JSONObject prePayOrderCreate(int total_fee, String openid, String body, String attach) {
=======
    private JSONObject prePayOrderCreate(int total_fee, String openid, String body, String attach)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        JSONObject response = new JSONObject();

        // 产生预支付订单(统一下单)
        String partnerTradeNo = CmTool.getOnceRandomOrderNo();
        Map<String, String> orderMap = wxUnifiedorder(openid, config, partnerTradeNo, body, total_fee, attach);
        LOG.debug("orderMap=" + JSONObject.toJSONString(orderMap));
<<<<<<< HEAD
        if (null == orderMap) {
            response.put("result", "failed-paymap");
            return response;
        }
        if (!existResult(orderMap, "result_code")) {
=======
        if (null == orderMap)
        {
            response.put("result", "failed-paymap");
            return response;
        }
        if (!existResult(orderMap, "result_code"))
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            response.put("result", "failed-payparam");
            return response;
        }
        // 将组合数据再次签名(5个参数 加 )
        Map<String, String> signResponse = signResponse(orderMap);
        response.put("orderid", partnerTradeNo);
        response.put("notifyurl", "/pay/wxCallBackByClient");
        response.put("appid", signResponse.get("appId"));
        response.put("timestamp", signResponse.get("timeStamp"));
        response.put("noncestr", signResponse.get("nonceStr"));
        response.put("prepayid", signResponse.get("package"));
        response.put("signtype", signResponse.get("signType"));
        response.put("sign", signResponse.get("sign"));
        response.put("result", "success");
        return response;
    }

    /**
     * 微信支付下单的方法
     *
     * @param openid       openid
     * @param config       配置
     * @param out_trade_no tradeNo
     * @param body         body
     * @param total_fee    price
     * @param attach       param
     * @return 结果
     */
<<<<<<< HEAD
    private static Map<String, String> wxUnifiedorder(String openid, PeDbWxConfig config, String out_trade_no, String body, int total_fee, String attach) {
        try {
=======
    private static Map<String, String> wxUnifiedorder(String openid, PeDbWxConfig config, String out_trade_no, String body, int total_fee, String attach)
    {
        try
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            Map<String, String> signMap = new HashMap<>();

            signMap.put("openid", openid);
            signMap.put("appid", config.ddAppId);
            signMap.put("mch_id", config.ddMchId);
            signMap.put("nonce_str", createNonceStr());
            signMap.put("out_trade_no", out_trade_no);
            signMap.put("body", body);
            signMap.put("total_fee", String.valueOf(total_fee));
            signMap.put("trade_type", "JSAPI");
            signMap.put("attach", attach);
            signMap.put("notify_url", ReadConfig.wxNotifyUrl);
            SignatureAlgorithm algorithm = new SignatureAlgorithm(config.ddKey, signMap);
            LOG.debug("签名校验:" + signMap);
            String xml = algorithm.getSignXml();
            String p12 = CmProjectConfig.getWxP12FilePath(config.ddP12), p12Password = config.ddP12Password;
            String result = CmTool.sendHttps(xml, WxConfig.UNIFIEDORDER_URL, p12, p12Password);
            System.out.println(signMap + "," + CmTool.getJSONByFastJSON(config) + "," + result + "," + p12 + "," + p12Password);
            XMLHandler handler = XMLHandler.parse(result);
            return handler.getXmlMap();
<<<<<<< HEAD
        } catch (Exception e) {
=======
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }

        return null;
    }

    /**
     * 查询微信支付订单
     *
     * @param orderId 订单号
     */
<<<<<<< HEAD
    private Map<String, String> searchPayOrder(String orderId) {
        // 查询订单在数据库中是否存在

        // 封装查询订单参数
        Map<String, String> searchOrderMap = new HashMap<>();
        searchOrderMap.put("appid", config.ddAppId);
        searchOrderMap.put("mch_id", config.ddMchId);
        searchOrderMap.put("out_trade_no", orderId);
        searchOrderMap.put("nonce_str", createNonceStr());
        searchOrderMap.put("sign_type", SIGN_TYPE);

        SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm(config.ddKey, searchOrderMap);
        String searchOrderXml = signatureAlgorithm.getSignXml();

        try {
            // 从微信平台里查询支付订单
            String searchResultXml = CmTool.sendHttps(searchOrderXml, QUERY_URL, CmProjectConfig.getWxP12FilePath(config.ddP12), config.ddP12Password);
            LOG.warn("查询订单结果:" + searchResultXml);
            XMLHandler parse = XMLHandler.parse(searchResultXml);
            return parse.getXmlMap();
        } catch (Exception e) {
=======
    private Map<String, String> searchPayOrder(String orderId)
    {
        // 查询订单在数据库中是否存在

        // 封装查询订单参数
        Map<String, String> searchOrder_map = new HashMap<>();
        searchOrder_map.put("appid", config.ddAppId);
        searchOrder_map.put("mch_id", config.ddMchId);
        searchOrder_map.put("out_trade_no", orderId);
        searchOrder_map.put("nonce_str", createNonceStr());
        searchOrder_map.put("sign_type", SIGN_TYPE);

        SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm(config.ddKey, searchOrder_map);
        String searchOrderXml = signatureAlgorithm.getSignXml();

        try
        {
            // 从微信平台里查询支付订单
            String searchResultXml = CmTool.sendHttps(searchOrderXml, QUERY_URL, CmProjectConfig.getWxP12FilePath(config.ddP12), config.ddP12Password);
            LOG.debug("查询订单结果:" + searchResultXml);
            XMLHandler parse = XMLHandler.parse(searchResultXml);
            return parse.getXmlMap();
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 订单是否成功
     *
     * @param orderMap 订单回调内容
     * @return 结果
     */
<<<<<<< HEAD
    private boolean orderIsSuccess(Map<String, String> orderMap) {
        String resultCode = "result_code", returnCode = "return_code", tradeState = "trade_state";
        boolean result = existResult(orderMap, resultCode) && existResult(orderMap, returnCode);
        if (result && orderMap.containsKey(tradeState)) {
            return existResult(orderMap, tradeState);
        }
        return result;
=======
    private boolean orderIsSuccess(Map<String, String> orderMap)
    {
        return existResult(orderMap, "result_code") && existResult(orderMap, "return_code") && existResult(orderMap, "trade_state");
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    }

    /**
     * 微信客户端回调通知结果
     *
     * @param request 请求参数
     */
<<<<<<< HEAD
    public JSONObject wxClientCallback(JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            String orderid = request.getString("orderid");
            //检测数据表，该单是否已经处理
            PeDbOrder order = PeDbOrder.gainObject(orderid);
            if (order == null) {
                response.put("result", "order is empty");
                return response;
            }
            if (order.ddTrans == null) {
=======
    public JSONObject wxClientCallback(JSONObject request)
    {
        JSONObject response = new JSONObject();
        try
        {
            String orderid = request.getString("orderid");
            //检测数据表，该单是否已经处理
            PeDbOrder order = PeDbOrder.gainObject(orderid);
            if (order == null)
            {
                response.put("result", "order is empty");
                return response;
            }
            if (order.ddTrans == null)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                //未处理，进行访问查单
                // 查询微信订单
                Map<String, String> searchPayOrder = searchPayOrder(orderid);
                resultCallback(searchPayOrder, order);
            }
            response.put("result", "success");

<<<<<<< HEAD
        } catch (Exception e) {
=======
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return response;
    }

    /**
     * 订单支付,结果回调
     *
     * @param searchPayOrder 回调支付内容
     * @param order          订单信息
     * @throws Exception 异常
     */
<<<<<<< HEAD
    private void resultCallback(Map<String, String> searchPayOrder, PeDbOrder order) throws Exception {
        if (searchPayOrder != null) {
=======
    private void resultCallback(Map<String, String> searchPayOrder, PeDbOrder order) throws Exception
    {
        if (searchPayOrder != null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            boolean status = orderIsSuccess(searchPayOrder);
            order.ddState = status ? 1 : 2;
            order.ddTrans = new Timestamp(System.currentTimeMillis());
            order.ddOrder = searchPayOrder.get("transaction_id");
            order.ddError = searchPayOrder.get("err_code") + "#" + searchPayOrder.get("err_code_des");
            order.update();
            //支付成功
<<<<<<< HEAD
            if (status) {
                PeDbGoodsValue goods = PeDbGoodsValue.getGoodsFast(order.ddGId);
                if (goods != null) {
                    int value = goods.ddValue;
                    String payJson = UserService.getStr(order.ddUid, "payInfo");
                    JSONObject pay = null;
                    if (payJson != null) {
                        pay = JSONObject.parseObject(payJson);
                    }
                    if (pay == null) {
                        pay = new JSONObject();
                    }
                    String goodKey = String.valueOf(goods.ddId);
                    int total = 0;
                    if (goods.ddFrist) {
                        if (!pay.containsKey(goodKey) || (total = pay.getInteger(goodKey)) <= 0) {
=======
            if (status)
            {
                PeDbGoodsValue goods = PeDbGoodsValue.getGoodsFast(order.ddGId);
                if (goods != null)
                {
                    int value = goods.ddValue;
                    String payJson = UserService.getStr(order.ddUid, "payInfo");
                    JSONObject pay = null;
                    if (payJson != null)
                        pay = JSONObject.parseObject(payJson);
                    if (pay == null)
                        pay = new JSONObject();
                    String goodKey = String.valueOf(goods.ddId);
                    int total = 0;
                    if (goods.ddFrist)
                    {
                        if (!pay.containsKey(goodKey) || (total = pay.getInteger(goodKey)) <= 0)
                        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                            value *= 2;
                        }
                    }
                    pay.put(goodKey, total + 1);
                    //购买商品，添加数值
                    JSONObject extra = new JSONObject();
                    extra.put("type", "goods");
                    //复活次数
                    extra.put("extra", String.valueOf(goods.ddId));
                    extra.put("appId", order.ddAppId);
                    UserService.addValue(order.ddUid, goods.ddGoodsType, value, extra);
                    UserService.setCache(order.ddUid, "payInfo", pay.toJSONString());
                }
                PAY.info("search order:" + CmTool.getJSONByFastJSON(order) + "," + goods);

            }
        }
    }

    /**
     * 微信异步回调通知
     *
     * @param notice 微信异步通知
     */
<<<<<<< HEAD
    public void wxAnsynCallBack(Map<String, String> notice) {
        String orderId = notice.get("out_trade_no");
        PeDbOrder order = PeDbOrder.gainObject(orderId);
        if (order != null && order.ddTrans == null) {
            try {
                resultCallback(notice, order);
            } catch (Exception e) {
=======
    public void wxAnsynCallBack(Map<String, String> notice)
    {
        String orderId = notice.get("out_trade_no");
        PeDbOrder order = PeDbOrder.gainObject(orderId);
        if (order != null && order.ddTrans == null)
        {
            try
            {
                resultCallback(notice, order);
            } catch (Exception e)
            {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                LOG.error(Log4j.getExceptionInfo(e));
            }
        }
    }

    /**
     * 支付结果回调
     *
     * @param xmlmap json map
     * @return bool
     */
<<<<<<< HEAD
    public static boolean existPayResult(Map<String, String> xmlmap) {
        if (xmlmap == null) {
=======
    public static boolean existPayResult(Map<String, String> xmlmap)
    {
        if (xmlmap == null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            return false;
        }
        return existResult(xmlmap, "return_code") && existResult(xmlmap, "result_code");
    }

    /**
     * 创建一笔提现订单
     *
<<<<<<< HEAD
     * @param uid   用户编号
     * @param rmb   提现金额
     * @param appId 商户平台
     * @return 提现记录
     * @throws Exception 异常信息
     */
    public static PeDbRecharge createRecharge(String uid, BigDecimal rmb, String appId) throws Exception {
        PeDbRecharge element = new PeDbRecharge();
        element.ddStatus = 0;
        element.ddAppId = appId;
=======
     * @param jsonObject 用户信息
     */
    public static PeDbRecharge createRecharge(JSONObject jsonObject) throws Exception
    {
        String uid = jsonObject.getString("uid");
        BigDecimal rmb = jsonObject.getBigDecimal("rmb");
        PeDbRecharge element = new PeDbRecharge();
        element.ddStatus = 0;
        element.ddAppId = jsonObject.getString("appId");
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        element.ddId = CmTool.getOnceRandomOrderNo();
        element.ddRmb = rmb;
        element.ddTimes = new Timestamp(System.currentTimeMillis());
        element.ddUid = uid;
        PeDbWxConfig wxConfig = PeDbWxConfig.getConfigFast(element.ddAppId);
<<<<<<< HEAD
        if (wxConfig != null && wxConfig.ddMchId != null) {
            element.ddRechargeAppId = element.ddAppId;
            element.ddRechargeOpenId = UserService.getOpenId(uid, element.ddAppId);
        } else {
=======
        if (wxConfig != null && wxConfig.ddMchId != null)
        {
            element.ddRechargeAppId = element.ddAppId;
            element.ddRechargeOpenId = UserService.getOpenId(uid, element.ddAppId);
        } else
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            element.ddStatus = 1;
        }
        element.insertObject(CmDbSqlResource.instance());
        return element;
    }

<<<<<<< HEAD
    /**
     * 创建一笔提现订单
     *
     * @param jsonObject 用户信息
     */
    public static PeDbRecharge createRecharge(JSONObject jsonObject) throws Exception {
        String uid = jsonObject.getString("uid");
        BigDecimal rmb = jsonObject.getBigDecimal("rmb");
        return createRecharge(uid, rmb, jsonObject.getString("appId"));
    }

=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed

    //    /**
    //     * 进行订单提现
    //     *
    //     * @param recharge recharge order data
    //     * @return get result data
    //     */
    //    public Map<String, String> recharge(PeDbRecharge recharge)
    //    {
    //        // 转化为成分
    //        int amount = recharge.ddRmb.multiply(new BigDecimal(100)).intValue();
    //        String orderId = recharge.ddId;
    //        String appId = recharge.ddAppId;
    //        String uid = recharge.ddUid;
    //        String tip = recharge.ddTip;
    //        return recharge(orderId, amount, appId, uid, tip);
    //    }

    /**
     * 提现相关接口
     * 企业付款到零钱
     *
     * @param orderid  订单编号
     * @param amount   提现金额
     * @param wxConfig 提现绑定appId
     * @param openid   提现用户
     * @param desc     提现描述
     * @return 提现结果
     */
<<<<<<< HEAD
    private Map<String, String> recharge(String orderid, int amount, PeDbWxConfig wxConfig, String openid, String desc, String ip) {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("mch_appid", wxConfig.ddAppId);
        signMap.put("mchid", wxConfig.ddMchId);
        if (!WxConfig.DEVICE_INFO.isEmpty()) {
            signMap.put("device_info", WxConfig.DEVICE_INFO);
        }
=======
    private Map<String, String> recharge(String orderid, int amount, PeDbWxConfig wxConfig, String openid, String desc, String ip)
    {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("mch_appid", wxConfig.ddAppId);
        signMap.put("mchid", wxConfig.ddMchId);
        if (!WxConfig.DEVICE_INFO.isEmpty())
            signMap.put("device_info", WxConfig.DEVICE_INFO);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        signMap.put("nonce_str", createNonceStr());
        signMap.put("partner_trade_no", orderid);
        signMap.put("openid", openid);
        signMap.put("check_name", WxConfig.CHECK_NAME.name());
        signMap.put("re_user_name", "default");
        signMap.put("amount", String.valueOf(amount));
<<<<<<< HEAD
        if (desc != null) {
            signMap.put("desc", desc);
        } else {
            signMap.put("desc", WxConfig.DESC);
        }
        signMap.put("spbill_create_ip", ip);
        SignatureAlgorithm algorithm = new SignatureAlgorithm(wxConfig.ddKey, signMap);
        String xml = algorithm.getSignXml();
        try {
=======
        if (desc != null)
            signMap.put("desc", desc);
        else
            signMap.put("desc", WxConfig.DESC);
        signMap.put("spbill_create_ip", ip);
        SignatureAlgorithm algorithm = new SignatureAlgorithm(wxConfig.ddKey, signMap);
        String xml = algorithm.getSignXml();
        try
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            String reuslt = CmTool.sendHttps(xml, WxConfig.TRANSFERS_URL, wxConfig.ddP12, wxConfig.ddP12Password);
            System.out.println(reuslt);
            XMLHandler handler = XMLHandler.parse(reuslt);
            return handler.getXmlMap();
<<<<<<< HEAD
        } catch (Exception e) {
=======
        } catch (Exception e)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }
}
