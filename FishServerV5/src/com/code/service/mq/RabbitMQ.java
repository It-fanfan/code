package com.code.service.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.utils.ReadConfig;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息队列
 */
public class RabbitMQ
{
    private static Logger LOG = LoggerFactory.getLogger(RabbitMQ.class);
    //在线时长
    private final static String QUEUE_ONLINE_NAME = "fish-v5-logout";
    private final static String EXCHANGE_NAME_ONE = "exchangeNameOne";
    private final static String ROUTING_KEY_ONE = "queueOne";

    //付费
    private final static String QUEUE_PAY_NAME = "fish-v5-pay";
    private final static String EXCHANGE_NAME_TWO = "exchangeNameTwo";
    private final static String ROUTING_KEY_TWO = "queueTwo";

    private static RabbitMQ instance;

    private ConnectionFactory factory = null;

    private static RabbitMQ getInstance()
    {
        if (instance == null)
            instance = new RabbitMQ();
        return instance;
    }

    private RabbitMQ()
    {
        if (ReadConfig.containsKey("rabbitmq"))
        {
            //创建连接工厂
            factory = new ConnectionFactory();

            //设置rabbotMQ相关信息
            factory.setHost(ReadConfig.get("rabbitmq.host"));
            factory.setUsername(ReadConfig.get("rabbitmq.username"));
            factory.setPassword(ReadConfig.get("rabbitmq.password"));
            factory.setPort(Integer.valueOf(ReadConfig.get("rabbitmq.port")));
        }
    }

    /**
     * 添加队列数据
     *
     * @param queueName 队列名称
     * @param msg       消息
     */
    private void addQueue(String queueName, String exchangeName, String routingKey, String msg)
    {
        try
        {
            LOG.warn("队列发送数据:queueName=[" + queueName + "].data=" + msg);
            if (factory == null)
            {
                return;
            }
            //创建新的连接
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, "direct");
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());  //发送消息
            channel.close();
            connection.close();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 添加付费队列
     *
     * @param userId  玩家信息
     * @param goodsId 商品编号
     * @param price   虚拟币价格，折损100:1
     */
    public static void addPayQueue(long userId, int goodsId, long price, long payTime)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("goodsId", goodsId);
        jsonObject.put("price", price);
        jsonObject.put("times", payTime);
        getInstance().addQueue(QUEUE_PAY_NAME, EXCHANGE_NAME_ONE, ROUTING_KEY_ONE, jsonObject.toJSONString());
    }

    /**
     * 添加在线时长队列请求
     *
     * @param userId    玩家信息
     * @param startTime 开始时间
     * @param endTime   截止时间
     */
    public static void addOnlineQueue(long userId, int basinId, long startTime, long endTime)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("basinId", basinId);
        jsonObject.put("start", startTime);
        jsonObject.put("end", endTime);
        getInstance().addQueue(QUEUE_ONLINE_NAME, EXCHANGE_NAME_TWO, ROUTING_KEY_TWO, jsonObject.toJSONString());
    }

    public static void main(String[] args)
    {
        String msg = "{\"times\":1569322368019,\"goodsId\":6,\"price\":100,\"userId\":11}";
        addPayQueue(11, 6, 100, 1569322368019L);
        addPayQueue(11, 6, 100, 1569317978000L);
    }
}
