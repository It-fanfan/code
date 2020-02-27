package db;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CmServletListener;
import tool.CmTool;
import tool.Log4j;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 权重分配配置逻辑
 */
@Entity
public class PeDbWeight
{
    //最小阀值
    private BigDecimal minLimit;
    //最大阀值
    private BigDecimal maxLimit;
    //默认群组
    private int tag;
    //地区方位
    public JSONObject ares = new JSONObject();

    private static PeDbWeight instance;

    private static SendMessageNotice notice = null;

    private static Logger LOG = LoggerFactory.getLogger(PeDbWeight.class);

    /**
     * 唯一实体类
     */
    public static PeDbWeight instance()
    {
        return instance;
    }

    /**
     * 发送通知
     *
     * @param title     标题
     * @param highTip   高亮文字
     * @param normalTip 正常文字
     * @param grayTip   灰色文字
     */
    public void sendNotice(String title, String highTip, String normalTip, String grayTip)
    {
        //报警通知{10}:群组,{1}:标题,{2}:高亮文字,{3}:正常文字,{4}:低灰文字
        String noticeUrl = "https://sms2.down10ad.com/smsv2.php?tag={0}&t1={1}&h1={2}&h2={3}&h3={4}";
        String url = MessageFormat.format(noticeUrl, tag, title, highTip, normalTip, grayTip);
        notice.messageUrl.put(title, url);

    }

    /**
     * 30s进行执行一次
     */
    public static class SendMessageNotice implements Runnable
    {
        public Map<String, String> messageUrl = new ConcurrentHashMap<>();

        @Override
        public void run()
        {
            if (!messageUrl.isEmpty())
            {
                Vector<String> message = new Vector<>(messageUrl.values());
                messageUrl.clear();
                try
                {
                    message.forEach(CmTool::makeHttpConnect);
                    LOG.error(JSONObject.toJSONString(message));
                } catch (Exception e)
                {
                    LOG.error(Log4j.getExceptionInfo(e));
                }
            }
        }
    }

    public static void main(String[] arg)
    {
        PeDbWeight weight = new PeDbWeight();
        weight.sendNotice("扩容测试", "扩容地区{全}", "阀值预警，房间使用高于80%", "低于20%进行缩容,高于80%进行缩容。");
    }

    public static void init()
    {
        instance = new PeDbWeight();
        if (notice == null)
        {
            notice = new SendMessageNotice();
            //30s執行一次
            CmServletListener.scheduler.scheduleWithFixedDelay(notice, 0, 30, TimeUnit.SECONDS);
        }
    }

    private PeDbWeight()
    {
        ares.put("0", "华东-上海");
        ares.put("1", "华北-北京");
        ares.put("2", "华南-广州");
        ares.put("3", "西南-成都");
        double min = Double.valueOf(Objects.toString(ReadConfig.get("weight-min"), "0.1"));
        minLimit = BigDecimal.valueOf(min);
        double max = Double.valueOf(Objects.toString(ReadConfig.get("weight-max"), "0.9"));
        maxLimit = BigDecimal.valueOf(max);
        tag = Integer.valueOf(Objects.toString(ReadConfig.get("weight-tag"), "30"));
    }

    public BigDecimal getMinLimit()
    {
        return minLimit;
    }

    public BigDecimal getMaxLimit()
    {
        return maxLimit;
    }

}
