package service;

import com.alibaba.fastjson.JSONObject;
import db.PeDbGame;
import db.PeDbRoundExt;
import db.PeDbRoundGame;
import db.PeDbRoundMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CmServletListener;
import tool.CmTool;
import tool.Log4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 赛场维护 1/5s,5s中一次轮询
 */
public class RoundSafeguard implements Runnable
{
    private static Logger LOG = LoggerFactory.getLogger(RoundSafeguard.class);
    private ScheduledExecutorService executor;

    public RoundSafeguard(ScheduledExecutorService executor)
    {
        this.executor = executor;
    }

    //赛场记录编号
    private volatile Set<String> records;

    public void init()
    {
        records = RoundSave.getMatchList();
    }

    @Override
    public void run()
    {
        init();
        CountDownLatch latch = new CountDownLatch(2);
        try
        {
            //进行查询
            //维护正常赛制
            updateRoundGame(latch);
            //维护群赛制
            updateRoundGroup(latch);
            latch.await();
            //进行结算判断
            records.forEach(key -> this.executor.execute(new RoundSave(key)));
            //更新数据库
            PeDbRoundGame.init();
            PeDbRoundMatch.init();
            PeDbRoundExt.init();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 更新常规赛制
     */
    private void updateRoundGame(CountDownLatch latch)
    {
        CmServletListener.scheduler.execute(() ->
        {
            Map<Integer, PeDbRoundGame> roundGameMap = PeDbRoundGame.getMatchesFast();
            Map<Integer, JSONObject> games = new HashMap<>();
            long now = System.currentTimeMillis();
            CountDownLatch temp = new CountDownLatch(roundGameMap.size());
            try
            {
                roundGameMap.forEach((k, v) -> CmServletListener.scheduler.execute(() ->
                {
                    if (v.ddState)
                    {
                        updateRoundRecord(games, now, k, v.ddRound, v.ddGame, v.ddName, v.ddEnd, v.ddStart, false);
                    }
                    temp.countDown();
                }));
                temp.await();
                //更新赛场信息
                RoundSave.updateNowRoundRecord(games, false);

            } catch (Exception e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }
        });
        latch.countDown();
    }

    /**
     * 维护群赛制
     */
    private void updateRoundGroup(CountDownLatch latch)
    {
        CmServletListener.scheduler.execute(() ->
        {
            Map<Integer, PeDbRoundMatch> roundGroupMap = PeDbRoundMatch.getMatchesFast();
            long now = System.currentTimeMillis();
            CountDownLatch temp = new CountDownLatch(roundGroupMap.size());
            roundGroupMap.forEach((k, v) -> CmServletListener.scheduler.execute(() ->
            {
                if (v.ddState)
                    updateRoundRecord(null, now, k, v.ddRound, v.ddGame, v.ddName, v.ddEnd, v.ddStart, true);
                temp.countDown();
            }));
        });

        latch.countDown();
    }

    /**
     * 更新赛场记录
     *
     * @param games   赛场记录
     * @param now     当前时间
     * @param k       赛场编号
     * @param ddRound 赛制编号
     * @param ddGame  游戏编号
     * @param ddEnd   截至时间
     * @param ddStart 开始时间
     * @param isGroup 是否群标签
     */
    private void updateRoundRecord(Map<Integer, JSONObject> games, long now, Integer k, String ddRound, int ddGame, String name, Timestamp ddEnd, Timestamp ddStart, boolean isGroup)
    {
        PeDbRoundExt roundExt = PeDbRoundExt.getRoundFast(ddRound);
        //检测赛场是否结束
        if (ddEnd.getTime() < now || roundExt == null || now < ddStart.getTime())
            return;
        //获取索引
        PeDbGame game = PeDbGame.getGameFast(ddGame);
        if (game == null)
            return;
        JSONObject data = getMatchIndex(game.ddIsPk == 1, now, ddStart, ddEnd, roundExt);
        int index = data.getInteger("index");
        String matchKey = RoundSave.getField(k, isGroup, index);
        JSONObject instance;
        if (!records.contains(matchKey))
        {
            //符合条件赛场，进行创建
            JSONObject matchInfo = new JSONObject();
            matchInfo.put("code", k);
            matchInfo.put("round", ddRound);
            matchInfo.put("submit", data.getLong("submit"));
            matchInfo.put("start", data.getLong("start"));
            matchInfo.put("end", data.getLong("end"));
            matchInfo.put("gameCode", ddGame);
            matchInfo.put("isGroup", isGroup);
            matchInfo.put("matchKey", matchKey);
            matchInfo.put("priority", roundExt.ddPriority);
            matchInfo.put("name", name);
            matchInfo.put("index", index);
            RoundSave.updateStatus(matchInfo, 0, "running");
            records.add(matchKey);
            instance = matchInfo;
        } else
        {
            instance = RoundSave.getRoundInfo(matchKey);
        }
        if (games == null)
            return;
        //获取当前游戏优先级
        int priority = roundExt.ddPriority;
        if (!games.containsKey(ddGame))
        {
            games.put(ddGame, instance);
            return;
        }
        int temp = games.get(ddGame).getInteger("priority");
        //当前游戏中，赛场优先级越小为最优
        if (temp > priority)
        {
            games.put(ddGame, instance);
        }
    }

    /**
     * 获取当前索引
     *
     * @param isPk    pk模式
     * @param now     当前时间
     * @param ddStart 赛场开启时间
     * @return 赛场索引
     */
    private JSONObject getMatchIndex(boolean isPk, long now, Timestamp ddStart, Timestamp ddEnd, PeDbRoundExt ext)
    {
        long ddTime = ext.ddTime + (isPk ? 5 * 60 : 0);
        BigDecimal value = CmTool.div(now - ddStart.getTime(), 1000 * ddTime, 2, RoundingMode.HALF_DOWN);
        JSONObject data = new JSONObject();
        //当前索引
        int index = value.intValue();
        data.put("index", index);
        //开始时间
        long start = ddStart.getTime() + (index * 1000 * ddTime);
        //截至时间,PK模式结算往后移
        long submit = ddStart.getTime() + (index + 1) * 1000 * ddTime;
        long end = submit - (isPk ? 5 * 60 * 1000 : 0);
        //赛制结束立马结束
        if (submit > ddEnd.getTime())
        {
            submit = ddEnd.getTime();
        }
        if (end > ddEnd.getTime())
        {
            end = ddEnd.getTime();
        }
        data.put("start", start);
        data.put("end", end);
        data.put("submit", submit);
        //获取当场开始时间
        return data;
    }
}
