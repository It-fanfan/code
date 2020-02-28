package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.*;
import com.fish.dao.second.model.Recharge;
import com.fish.dao.second.model.UserApp;
import com.fish.dao.second.model.UserInfo;
import com.fish.dao.second.model.UserValue;
import com.fish.protocols.GetParameter;
import com.fish.utils.log4j.Log4j;
import com.fish.utils.tool.CmTool;
import com.fish.utils.tool.SignatureAlgorithm;
import com.fish.utils.tool.WxConfig;
import com.fish.utils.tool.XMLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fish.utils.tool.CmTool.createNonceStr;

@Service
public class RechargeService implements BaseService<Recharge>
{
    private static final Logger LOG = LoggerFactory.getLogger(RechargeService.class);
    @Autowired
    RechargeMapper rechargeMapper;
    @Autowired
    UserAppMapper userAppMapper;

    @Autowired
    WxConfigMapper wxConfigMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserValueMapper userValueMapper;

    @Override
    //查询所有提现申请
    public List<Recharge> selectAll(GetParameter parameter)
    {
        List<Recharge> recharges = rechargeMapper.selectAll();
        List<Recharge> rechargeList = new ArrayList<>();
        for (Recharge recharge : recharges)
        {
            String dduid = recharge.getDduid();
            UserApp userApp = userAppMapper.selectByPrimaryKey(dduid);
            if (userApp != null)
            {
                String ddoid = userApp.getDdoid();
                recharge.setDdopenid(ddoid);
            }
            String ddAppId = recharge.getDdappid();
            com.fish.dao.second.model.WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(ddAppId);
            if (wxConfig != null)
            {
                String productName = wxConfig.getProductName();
                Integer programType = wxConfig.getProgramType();
                recharge.setProductName(productName);
                recharge.setProgramType(programType);
            }
            UserValue userValue = userValueMapper.selectByPrimaryKey(dduid);
            if (userValue != null)
            {
                //剩余金额
                Integer ddMoney = userValue.getDdmoney();
                recharge.setRemainAmount(ddMoney / 100);
            }

            UserInfo userInfo = userInfoMapper.selectByDdUid(dduid);
            if (userInfo != null)
            {
                String userName = userInfo.getDdname();
                recharge.setUserName(userName);
            }
            String ddRechargeOpenId = recharge.getDdRechargeOpenId();
            String ddRechargeAppId = recharge.getDdRechargeAppId();
            if (ddRechargeOpenId != null && ddRechargeOpenId.length() > 0)
            {
                UserApp userPublic = userAppMapper.selectByOpenId(ddRechargeOpenId);
                String uidPublic = userPublic.getDduid();
                recharge.setDdRechargeUid(uidPublic);
            }
            if (recharge.getDdstatus() == 200)
            {
                recharge.setDdrmbed(recharge.getDdrmb());
            }
            Integer programType = recharge.getProgramType();
            if (programType == 1)
            {
                rechargeList.add(recharge);
            }
        }
        return rechargeList;
    }

    //新增信息
    public int insert(Recharge record)
    {

        return rechargeMapper.insert(record);
    }

    //更新信息
    public int updateByPrimaryKeySelective(Recharge record)
    {

        return rechargeMapper.updateByPrimaryKeySelective(record);
    }

    //默认排序
    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("ddtimes");
        parameter.setOrder("desc");
    }

    @Override
    public Class<Recharge> getClassInfo()
    {
        return Recharge.class;
    }

    //筛选
    @Override
    public boolean removeIf(Recharge recharge, JSONObject searchData)
    {
        //        String times = searchData.get("times");
        //        Date[] parse = XwhTool.parseDate(times);
        //        if (times != null && times.length() != 0) {
        //            if (recharge.getDdtimes().before(parse[0]) || recharge.getDdtimes().after(parse[1])) {
        //                return true;
        //            }
        //        }
        if (existTimeFalse(recharge.getDdtimes(), searchData.getString("times")))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("userName"), recharge.getUserName()))
        {
            return true;
        }
        if (existValueFalse(searchData.getString("productName"), recharge.getProductName()))
        {
            return true;
        }
        return (existValueFalse(searchData.getString("ddStatus"), recharge.getDdstatus()));
    }

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
    private Map<String, String> recharge(String orderid, int amount, com.fish.dao.second.model.WxConfig wxConfig, String openid, String desc, String ip)
    {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("mch_appid", wxConfig.getDdappid());
        signMap.put("mchid", wxConfig.getDdmchid());
        if (!WxConfig.DEVICE_INFO.isEmpty())
            signMap.put("device_info", WxConfig.DEVICE_INFO);
        signMap.put("nonce_str", createNonceStr());
        signMap.put("partner_trade_no", orderid);
        signMap.put("openid", openid);
        signMap.put("check_name", WxConfig.CHECK_NAME.name());
        signMap.put("re_user_name", "default");
        signMap.put("amount", String.valueOf(amount));
        if (desc != null)
            signMap.put("desc", desc);
        else
            signMap.put("desc", WxConfig.DESC);
        signMap.put("spbill_create_ip", ip);
        SignatureAlgorithm algorithm = new SignatureAlgorithm(wxConfig.getDdkey(), signMap);
        String xml = algorithm.getSignXml();
        String ddp12 = wxConfig.getDdp12();
        String ddp12password = wxConfig.getDdp12password();
        try
        {
            String reuslt = CmTool.sendHttps(xml, WxConfig.TRANSFERS_URL, wxConfig.getDdp12(), wxConfig.getDdp12password());
            System.out.println(reuslt);
            XMLHandler handler = XMLHandler.parse(reuslt);
            return handler.getXmlMap();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    //微信提现接口调用
    public int getCash(List<Recharge> productInfo)
    {
        int index = 0;
        int error = 0;
        int errorCount = 0;
        for (Recharge recharge : productInfo)
        {

            Integer programType = recharge.getProgramType();
            if (programType == 1)
            {
                String dduid = recharge.getDduid();
                String ddappid = recharge.getDdappid();
                UserApp userApp = userAppMapper.searchOppenId(dduid, ddappid);
                String oppenId = "";
                if (userApp != null)
                {
                    oppenId = userApp.getDdoid();
                }
                String orderId = recharge.getDdid();

                BigDecimal num100 = new BigDecimal("100");
                BigDecimal ddrmb = recharge.getDdrmb().multiply(num100);
                int rmb = ddrmb.intValue();
                com.fish.dao.second.model.WxConfig wxConfig = wxConfigMapper.selectByPrimaryKey(ddappid);
                String hostAddress = "";
                try
                {
                    InetAddress address = InetAddress.getLocalHost();
                    hostAddress = address.getHostAddress();
                } catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                System.out.println(orderId + "rmb:" + rmb + "wxConfig:" + wxConfig + "oppenId:" + oppenId + "描述:" + "野火争霸赛奖金" + rmb / 100 + "元" + "hostAddress:" + "114.93.211.181");
                //               Map<String, String> backCharge = recharge(orderId, rmb, wxConfig, oppenId, "野火争霸赛奖金" + rmb / 100 + "元", "114.93.211.181");
                //               String return_code = backCharge.get("return_code");
                //               if(return_code.equals("FAIL")){
                //                   String return_msg = backCharge.get("return_msg");
                //                   recharge.setDdtip(return_msg);
                //                   recharge.setDdstatus(2);
                //                   recharge.setDdtrans(new Timestamp(new Date().getTime()));
                //                   error = rechargeMapper.updateByPrimaryKeySelective(recharge);
                //                   errorCount = errorCount+error;
                //               }else if(return_code.equals("SUCCESS")){
                //                   String result_code = backCharge.get("result_code");
                //                   if(result_code.equals("SUCCESS")){
                //                       recharge.setDdstatus(200);
                //                       recharge.setDdtip("");
                //                       recharge.setDdtrans(new Timestamp(new Date().getTime()));
                //                       rechargeMapper.updateByPrimaryKeySelective(recharge);
                //                       index++;
                //                   }else if(result_code.equals("FAIL")){
                //                       String err_code = backCharge.get("err_code");
                //                       recharge.setDdtip(err_code);
                //                       recharge.setDdstatus(2);
                //                       recharge.setDdtrans(new Timestamp(new Date().getTime()));
                //                       error = rechargeMapper.updateByPrimaryKeySelective(recharge);
                //                       errorCount = errorCount+error;
                //                   }
                //               }
                //               LOG.info("提现返回结果 :"+return_code);
                //           }
                //
                //        }
                //        if(productInfo.size() == index){
                //            return 200;
                //        }else {
                //            return 404;
                //        }

            }

        }
        return 200;
    }
}