package pipe;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.PeDbWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CmTool;
import tool.Log4j;
import tool.db.RedisUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuwei
 */
public class CmPipeServiceDemon implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(CmPipeServiceDemon.class);

    /**
     * 套接字信息
     */
    private ServerSocket server = null;

    /**
     * 链接socket交互
     */
    private static Vector<CmPipeSocket> sockets = new Vector<>();

    public static Vector<CmPipeSocket> getSockets()
    {
        return sockets;
    }

    /**
     * 缓存数据
     */
    public static final String REDIS_KEY = "match-service";

    /**
     * 构造一个 CmPipeClient
     */
    CmPipeServiceDemon(int port)
    {
        try
        {
            server = new ServerSocket(port);
        } catch (IOException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 更新房间数据
     *
     * @param roomInfo 最新房间信息
     * @param pkRoom   房间数组
     */
    static void updateRoomInfo(JSONObject roomInfo, JSONArray pkRoom)
    {
        String roomSeq = roomInfo.getString("roomSeq");
        for (int i = 0; i < pkRoom.size(); i++)
        {
            JSONObject service = pkRoom.getJSONObject(i);
            JSONArray buzyRooms = service.getJSONArray("buzyRooms");
            for (int j = 0; j < buzyRooms.size(); j++)
            {
                JSONObject room = buzyRooms.getJSONObject(j);
                if (room.getString("roomSeq").equals(roomSeq))
                {
                    room.putAll(roomInfo);
                    saveRoomRedis(room);
                    return;
                }
            }
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                Socket socket = server.accept();
                //2、调用accept()方法开始监听，等待客户端的连接
                //使用accept()阻塞等待客户请求，有客户
                //请求到来则产生一个Socket对象，并继续执行
                CmPipeSocket pipeSocket = new CmPipeSocket(socket);
                sockets.add(pipeSocket);
                pipeSocket.start();
            }

        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 进行移除房间数据
     */
    static void removeSocket(CmPipeSocket socket)
    {
        sockets.removeElement(socket);
        System.out.println("移除socket");
        updateWeight();
    }

    /**
     * 更新权重参数
     */
    static void updateWeight()
    {
        //通过地区分布服务器
        Vector<CmPipeSocket> sockets = getSockets();
        //当前策略配置
        String configStr = RedisUtils.hget(REDIS_KEY, "config");
        JSONObject config = new JSONObject();
        if (configStr != null)
        {
            config = JSONObject.parseObject(configStr);
        }

        JSONObject aresCache = new JSONObject();
        //地区分布
        for (CmPipeSocket socket : sockets)
        {
            //获取大区分配服务器IP
            String key = socket.getSocketKey();
            //获取区服务器
            JSONObject service = socket.getPkService();
            //所属区域
            int ares = service.getInteger("ares");
            JSONObject aresData = aresCache.getJSONObject(String.valueOf(ares));
            if (aresData == null)
            {
                aresData = new JSONObject();
            }
            //获取当前匹配服务容纳量
            int weight = service.getInteger("weight");
            JSONArray pkServices = socket.getPkRoom();
            JSONObject match = getMatchInfo(key, service, pkServices, config);
            //当前匹配服权限配置暂停
            if (weight <= 0)
            {
                continue;
            }
            //获取当前区域匹配服的承载量
            match.put("checkType", service.getString("checkType"));
            aresData.put(key, match);
            aresCache.put(String.valueOf(ares), aresData);
        }
        //进行存储给redis，通知外部服务器通过该规则调用该数据
        RedisUtils.hset(REDIS_KEY, "service", aresCache.toJSONString());
        //按照规则分配，权重越低，优先停止
        //进行检测各区域是否达到阀值，阀值通告
        warningNotice(aresCache);
        updateMatchOnline();
    }

    /**
     * 获取赛场在线人数信息
     */
    private static void updateMatchOnline()
    {
        JSONObject online = new JSONObject();
        //通过地区分布服务器
        Vector<CmPipeSocket> sockets = getSockets();
        for (CmPipeSocket socket : sockets)
        {
            JSONArray pkServices = socket.getPkRoom();
            if (pkServices == null)
            {
                continue;
            }
            for (int i = 0; i < pkServices.size(); i++)
            {
                JSONObject pkService = pkServices.getJSONObject(i);
                JSONArray buzyRooms = pkService.getJSONArray("buzyRooms");
                for (int j = 0; j < buzyRooms.size(); j++)
                {
                    JSONObject room = buzyRooms.getJSONObject(j);
                    if (room.getInteger("gameCode") <= 0)
                    {
                        continue;
                    }
                    int userCount = room.getInteger("userCount");
                    String matchKey = room.getString("matchKey");
                    Integer count = online.getInteger(matchKey);
                    if (count == null)
                    {
                        count = userCount;
                    } else
                    {
                        count += userCount;
                    }
                    online.put(matchKey, count);
                }
            }
        }
        RedisUtils.hset(REDIS_KEY, "online", online.toJSONString());
    }

    /**
     * 进行保存房间到redis中
     *
     * @param room 房间数据
     */
    private static void saveRoomRedis(JSONObject room)
    {
        try
        {
            //进行保存房间信息
            String roomSeq = room.getString("roomSeq");
            String statusStr = "status";
            //不存在状态，不进行存储
            if (!room.containsKey(statusStr))
            {
                return;
            }
            String status = room.getString(statusStr);
            //进行保存房间数据
            String key = "roomSeq-" + roomSeq;
            switch (status)
            {
                case "loading":
                {
                    RedisUtils.set(key, room.toJSONString());
                    RedisUtils.pexpire(key, 10000);
                }
                break;
                case "killed":
                {
                    updateRoomData(room, key, status);
                    RedisUtils.pexpire(key, 10000);
                }
                break;
                default:
                {
                    updateRoomData(room, key, status);
                    RedisUtils.pexpire(key, 1000 * 60 * 30);
                }
                break;
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 更新用户状态信息
     *
     * @param room   房间数据
     * @param key    key值
     * @param status 状态数据
     */
    private static void updateRoomData(JSONObject room, String key, String status)
    {
        String str = RedisUtils.get(key);
        if (str == null)
        {
            str = room.toJSONString();
        } else
        {
            JSONObject temp = JSONObject.parseObject(str);
            temp.put("status", status);
            temp.put("userCount", room.get("userCount"));
            temp.put("vpUserIndexs", room.get("vpUserIndexs"));
            temp.put("isSingle", room.get("isSingle"));
            JSONArray vpUserFinish = room.getJSONArray("vpUserFinish");
            if (!vpUserFinish.isEmpty())
            {
                temp.put("vpUserFinish", vpUserFinish);
            }
            JSONArray vpRecords = room.getJSONArray("vpRecords");
            if (!vpRecords.isEmpty())
            {
                temp.put("vpRecords", vpRecords);
            }
            String finishResultStr = "finishResult";
            if (room.containsKey(finishResultStr))
            {
                temp.put(finishResultStr, room.getJSONObject(finishResultStr));
            }
            str = temp.toJSONString();
        }
        RedisUtils.set(key, str);
    }


    /**
     * 获取在线人数
     *
     * @param pkService 赛区信息
     * @return 在线人数
     */
    private static int getOnlineUser(JSONObject pkService)
    {
        AtomicInteger online = new AtomicInteger();
        JSONArray rooms = pkService.getJSONArray("buzyRooms");
        if (rooms != null)
        {
            for (int i = 0; i < rooms.size(); i++)
            {
                JSONObject room = rooms.getJSONObject(i);
                if (room != null)
                {
                    int userCount = room.getInteger("userCount");
                    online.addAndGet(userCount);
                }
            }
        }
        return online.intValue();
    }

    /**
     * 获取当前配置服相关参数
     */
    private static JSONObject getMatchInfo(String key, JSONObject service, JSONArray pkServices, JSONObject config)
    {
        JSONObject match = new JSONObject();
        match.put("key", key);
        match.put("port", service.getString("port"));
        match.put("weight", service.getString("weight"));
        //获取当前匹配服能够容纳房间数据
        JSONArray pkRooms = new JSONArray();
        if (pkServices != null)
        {
            for (int i = 0; i < pkServices.size(); i++)
            {
                JSONObject pkService = pkServices.getJSONObject(i);

                String addr = pkService.getString("addr");
                Boolean pause = config.containsKey(addr) && config.getBoolean(addr);
                //最大分配用户数
                int maxPlayer;
                //空闲房间数
                int idle = pkService.getInteger("idleCount");
                //繁忙房间数
                int buzy = pkService.getInteger("buzyCount");
                //允许值
                int allow = pkService.getInteger("allow");
                //总房间数
                int roomSize = buzy + idle;
                if (pkService.containsKey("maxPlayer"))
                {
                    maxPlayer = pkService.getInteger("maxPlayer");
                } else
                {
                    maxPlayer = roomSize;
                }
                int online = getOnlineUser(pkService);
                //关停状态，不进行计算该服务器房间
                if (!pause)
                {
                    //进行判断
                    if (allow == -1 || allow == 1)
                    {
                        setRoomInfo(match, "pk", roomSize, buzy);
                    }
                    if (allow == -1 || allow == 0)
                    {
                        setRoomInfo(match, "through", roomSize, buzy);
                    }
                }
                //房间服，相关数据
                JSONObject room = new JSONObject();
                room.put("addr", addr);
                room.put("port", pkService.getInteger("port"));
                room.put("allow", pkService.getInteger("allow"));
                room.put("weight", pkService.getInteger("weight"));
                room.put("buzySize", buzy);
                room.put("roomSize", roomSize);
                room.put("maxPlayer", maxPlayer);
                room.put("online", online);
                pkRooms.add(room);
            }
        }
        match.put("pkRoom", pkRooms);
        return match;
    }

    /**
     * 区域警告通知
     *
     * @param aresCache 区域服务器
     */
    private static void warningNotice(JSONObject aresCache)
    {
        //进行判断是否为总量驱动
        boolean warningFlag = Boolean.valueOf(Objects.toString(ReadConfig.get("warning-flag"), "false"));
        //预警检测
        String checkType = Objects.toString(ReadConfig.get("warning-check"), "room");
        //进行计算是否达到阀值高峰期
        //每个区域pk和through服房间数是否达到80%
        JSONArray logic = new JSONArray();
        for (Map.Entry<String, Object> set : aresCache.entrySet())
        {
            //地区内容
            String ares = set.getKey();
            JSONObject context = (JSONObject) set.getValue();
            //检测判断
            JSONArray temp = new JSONArray();
            if (checkType == null || "room".equals(checkType))
            {
                setRoomData(context, temp);
                //房间判断
            } else if ("user".equals(checkType))
            {
                //人数判断
                JSONObject pkCache = new JSONObject();
                int online = 0;
                if (context.containsKey("online"))
                {
                    online = context.getInteger("online");
                }
                pkCache.put("online", online);
                int total = 0;
                if (context.containsKey("maxPlayer"))
                {
                    total = context.getInteger("maxPlayer");
                }
                pkCache.put("total", total);
                pkCache.put("type", "user");
                temp.add(pkCache);
            }
            if (warningFlag)
            {
                updateWarningData(temp, logic);
                continue;
            }
            warning(ares, temp);
        }
        if (warningFlag)
        {
            warning("总地域", logic);
        }
    }

    /**
     * 设置房间数据
     *
     * @param context 查询数据
     * @param temp    汇总单项
     */
    private static void setRoomData(JSONObject context, JSONArray temp)
    {
        //当前地区pk房间数信息，through房间数信息
        int pk = 0, through = 0, buzyPk = 0, buzyThrough = 0;
        for (Map.Entry<String, Object> match : context.entrySet())
        {
            JSONObject value = (JSONObject) match.getValue();
            if (value.containsKey("pk"))
            {
                pk += value.getInteger("pk");
            }
            if (value.containsKey("buzy-pk"))
            {
                buzyPk += value.getInteger("buzy-pk");
            }
            if (value.containsKey("through"))
            {
                through += value.getInteger("through");
            }
            if (value.containsKey("buzy-through"))
            {
                buzyThrough += value.getInteger("buzy-through");
            }
        }
        JSONObject pkCache = new JSONObject();
        pkCache.put("online", buzyPk);
        pkCache.put("total", pk);
        pkCache.put("type", "pk");
        temp.add(pkCache);
        JSONObject throughCache = new JSONObject();
        throughCache.put("online", buzyThrough);
        throughCache.put("total", through);
        throughCache.put("type", "through");
        temp.add(throughCache);
    }

    /**
     * 设置警告数据
     *
     * @param temp  临时数据
     * @param logic 警告数据
     */
    private static void updateWarningData(JSONArray temp, JSONArray logic)
    {
        for (int i = 0; i < temp.size(); i++)
        {
            JSONObject sign = temp.getJSONObject(i);
            String type = sign.getString("type");
            boolean update = false;
            for (int j = logic.size() - 1; j >= 0; j--)
            {
                JSONObject collect = logic.getJSONObject(j);
                if (collect.getString("type").equals(type))
                {
                    collect.put("online", collect.getInteger("online") + sign.getInteger("online"));
                    collect.put("total", sign.getInteger("total"));
                    update = true;
                    break;
                }
            }
            if (update)
            {
                continue;
            }
            logic.add(sign);
        }
    }


    /**
     * 警告判斷
     *
     * @param ares  地區
     * @param logic 预警数据
     */
    private static void warning(String ares, JSONArray logic)
    {
        PeDbWeight peDbWeight = PeDbWeight.instance();

        for (int i = 0; i < logic.size(); i++)
        {
            JSONObject warning = logic.getJSONObject(i);
            String type = warning.getString("type");
            int online = warning.containsKey("online") ? warning.getInteger("online") : 0;
            int total = warning.containsKey("total") ? warning.getInteger("total") : 0;
            BigDecimal rate = CmTool.div(online, total, 2);
            //进行预警
            if (rate.compareTo(peDbWeight.getMaxLimit()) >= 0)
            {
                warningMaxNotice(peDbWeight, ares, rate, peDbWeight.getMaxLimit(), online, total, type);
            }
            boolean warningFlag = Boolean.valueOf(Objects.toString(ReadConfig.get("min-flag"), "false"));
            if (warningFlag)
            {
                continue;
            }
            if (rate.compareTo(peDbWeight.getMinLimit()) <= 0)
            {
                warningMinNotice(peDbWeight, ares, rate, peDbWeight.getMinLimit(), online, total, type);
            }
        }
    }

    /**
     * 通过类型获取服务信息
     *
     * @param type 服务类型
     * @return 信息
     */
    private static String getServiceName(String type)
    {
        switch (type)
        {
            case "pk":
            {
                return "pk服";
            }
            case "through":
            {
                return "闯关服";
            }
            default:
                return "默认服";
        }
    }

    /**
     * 设置游戏服配置
     *
     * @param peDbWeight 权重配置
     * @param ares       地区
     * @param rate       当前权重
     * @param min        下限配置
     * @param buzy       当前使用房间数
     * @param total      总房间数
     * @param type       服务器通配
     */
    private static void warningMinNotice(PeDbWeight peDbWeight, String ares, BigDecimal rate, BigDecimal min, int buzy, int total, String type)
    {
        String highTip = peDbWeight.ares.getString(ares);
        if (highTip == null)
        {
            highTip = ares;
        }
        String minPer = min.multiply(BigDecimal.valueOf(100)) + "%";
        String per = rate.multiply(BigDecimal.valueOf(100)) + "%";
        String serviceName = getServiceName(type);
        String name = type.startsWith("user") ? "人数" : "房间";
        String title = MessageFormat.format("街机服务器缩容通知-{0},{1}", serviceName, highTip);
        String highContent = MessageFormat.format("{0}{1}低于{2}设置阀值,目前{3}", highTip, name, minPer, per);

        String normalContent = MessageFormat.format("{0}{1}占用率为{2},可以下架服务器了", serviceName, name, per);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String grayContent = MessageFormat.format("总{0}数{1},使用数{2},报警时间:{3}", name, total, buzy, format.format(new Date()));
        peDbWeight.sendNotice(title, highContent, normalContent, grayContent);
    }

    /**
     * 警告通知
     *
     * @param peDbWeight 权重配置
     * @param ares       区域信息
     * @param rate       扩容rate
     * @param max        阀值通知
     * @param type       通报配置
     */
    private static void warningMaxNotice(PeDbWeight peDbWeight, String ares, BigDecimal rate, BigDecimal max, int buzy, int total, String type)
    {
        String highTip = peDbWeight.ares.getString(ares);
        if (highTip == null)
        {
            highTip = ares;
        }
        String maxPer = max.multiply(BigDecimal.valueOf(100)) + "%";
        String per = rate.multiply(BigDecimal.valueOf(100)) + "%";
        String serviceName = getServiceName(type);
        String name = type.startsWith("user") ? "人数" : "房间";
        String title = MessageFormat.format("街机服务器扩容通知-{0},{1}", serviceName, highTip);
        String highContent = MessageFormat.format("{0}{1}高于{2}设置阀值,目前{3}", highTip, name, maxPer, per);
        String normalContent = MessageFormat.format("{0}{1}空间已经占用达到{2},请及时扩容保障正常游戏", serviceName, name, per);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String grayContent = MessageFormat.format("当前总{0}数{1},已经使用数{2},报警时间:{3}", name, total, buzy, format.format(new Date()));
        peDbWeight.sendNotice(title, highContent, normalContent, grayContent);
    }

    /**
     * 设置房间信息
     *
     * @param room     房间数据
     * @param type     房间类型
     * @param roomSize 房间大小
     * @param buzy     繁忙房间
     */
    private static void setRoomInfo(JSONObject room, String type, int roomSize, int buzy)
    {
        //总房间数
        if (room.containsKey(type))
        {
            room.put(type, room.getInteger(type) + roomSize);

        } else
        {
            room.put(type, roomSize);
        }
        //繁忙房间数
        String key = "buzy-" + type;
        if (room.containsKey(key))
        {
            room.put(key, room.getInteger(key) + buzy);

        } else
        {
            room.put(key, buzy);
        }
    }
}
