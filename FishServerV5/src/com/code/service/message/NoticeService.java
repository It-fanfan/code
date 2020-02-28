package com.code.service.message;

import com.alibaba.fastjson.JSONObject;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigEmail;
import com.code.dao.entity.message.Email;
import com.code.dao.entity.message.Notice;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.basic.BasePro;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.social.SocialBase;
import com.code.service.friend.FriendService;
import com.code.service.reward.RewardService;
import com.code.service.ui.FlushService;
import com.google.gson.reflect.TypeToken;
import com.utils.XwhTool;
import com.utils.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class NoticeService
{
    private static Logger LOG = LoggerFactory.getLogger(NoticeService.class);
    private UserCache userCache;

    public NoticeService(UserCache userCache)
    {
        this.userCache = userCache;
    }

    /**
     * 获取有效的公告
     *
     * @return 公告信息
     */
    private static Vector<Notice> getNotice()
    {
        Vector<Notice> notices = new Vector<>();
        Vector<Notice> data = FishInfoDb.instance().getCacheListByClass(Notice.class);
        if (data != null)
        {
            long current = System.currentTimeMillis();
            return data.stream().filter(Notice::getState).filter(notice -> notice.getStartTime().getTime() <= current && notice.getEndTime().getTime() >= current).collect(Collectors.toCollection(Vector::new));
        }
        return notices;
    }

    /**
     * 获取配置参数
     *
     * @param type 配置类型
     */
    public static ConfigEmail getConfigEmail(OperatorBase.MessageType type)
    {
        return (ConfigEmail) FishInfoDao.instance().getCacheKey(ConfigEmail.class, new String[]{"udpType", type.name()});
    }

    /**
     * 获取redis标签
     */
    private static String getRedisField()
    {
        return "flag_message";
    }

    /**
     * 获取公告标签
     */
    private static String getNoticeRedisField()
    {
        return "flag_notice";
    }

    /**
     * 获取公告信息
     *
     * @param history 残留记录
     */
    private Vector<Notice> getUserNotice(Vector<Integer> history)
    {
        Vector<Notice> notices = new Vector<>();
        getNotice().stream().filter(notice -> (history == null || !history.contains(notice.getId()))).forEachOrdered(notices::add);
        return notices;
    }

    /**
     * TODO:UI检测
     *
     * @return 检测判断
     */
    public boolean uiFlag()
    {
        return userCache.hexist(getRedisField());
    }

    /**
     * 添加一封系统邮件
     *
     * @param context 邮件内容
     * @param config  邮件配置
     */
    public void addMessageInfo(JSONObject json, String context, ConfigEmail config)
    {
        updateEmail(json.toJSONString(), config.getUdpType(), config.getUdpType(), config.getTitle(), context, config.getReward(), config.getButton(), OperatorBase.LeaveType.none.name(), new Timestamp(System.currentTimeMillis()));
        //添加被动刷新
        new FlushService(userCache).addFlag(FlushService.FlushType.user);
    }

    /**
     * 获取系统邮箱数据
     */
    public Vector<OperatorBase.MessageInfo> getSystemEmail(Vector<String> history)
    {
        Vector<Long> remove = new Vector<>();
        try
        {
            Vector<Email> emails = FishInfoDao.searchDate(Email.class, userCache.getUserId());
            if (emails != null)
            {
                LOG.debug("获取玩家邮件信息:size=" + emails.size() + "," + XwhTool.getJSONByFastJSON(emails));
                Vector<OperatorBase.MessageInfo> infos = new Vector<>();
                emails.forEach(element ->
                {
                    String id = String.valueOf(element.getId());
                    if (null != history && history.contains(id))
                        return;
                    OperatorBase.MessageInfo info = new OperatorBase.MessageInfo();
                    info.type = OperatorBase.MessageType.valueOf(element.getMessageType());
                    info.button = OperatorBase.ButtonType.valueOf(element.getButton());
                    info.context = element.getContext();
                    info.title = element.getTitle();
                    info.times = element.getTimes().getTime() / 1000;
                    info.icon = element.getIcon();
                    info.leave = OperatorBase.LeaveType.valueOf(element.getLeaveType());
                    info.reward = element.getReward();
                    info.id = id;
                    infos.add(info);
                    if (element.getReward() == null || element.getReward().isEmpty())
                    {
                        remove.add(element.getId());
                    }
                });
                return infos;
            }
            LOG.debug("获取玩家邮件信息:邮件历史为空,userId=" + userCache.getUserId());
        } finally
        {
            if (!remove.isEmpty())
            {
                removeData(remove);
            }
        }
        return new Vector<>();
    }

    /**
     * 移除数据
     *
     * @param remove 移除对象
     */
    private void removeData(Vector<Long> remove)
    {
        //移除表数据
        try
        {
            StringJoiner joiner = new StringJoiner(",");
            for (long id : remove)
            {
                joiner.add(Long.toString(id));
            }
            String collect = joiner.toString();
            List<String> SQLs = new Vector<>();
            SQLs.add("insert into record_email select * from email where id in(" + collect + ")");
            SQLs.add("delete from email where id in(" + collect + ")");
            FishInfoDao.instance().execBatchSQL(SQLs);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 领取邮件数据
     *
     * @param id 邮件信息
     */
    public OperatorBase.ERROR receiveMessage(String id)
    {
        Map<String, String> searchData = new HashMap<>();
        searchData.put("id", id);
        Vector<Email> emails = FishInfoDao.searchDate(Email.class, userCache.getUserId(), searchData);
        if (emails != null && !emails.isEmpty())
        {
            Vector<Long> remove = new Vector<>();
            emails.forEach(email ->
            {
                OperatorBase.MessageType type = OperatorBase.MessageType.valueOf(email.getMessageType());
                if (type == OperatorBase.MessageType.present)
                {
                    //进行互赠
                    Map<String, String> parameters = XwhTool.parseJsonBase(email.getParameters());
                    String friendId = parameters.get("userId");
                    if (friendId != null)
                    {
                        FriendService service = new FriendService(userCache);
                        SocialBase.ERROR error = service.presented(friendId);
                        LOG.debug("赠送调用结果:" + error);
                    }
                }
                Vector<BasePro.RewardInfo> rewards = RewardService.getRewardData(email.getReward());
                RewardService service = new RewardService(userCache);
                service.receiveReward(rewards, email.getMessageType());
                remove.add(email.getId());
            });
            if (!remove.isEmpty())
                removeData(remove);
        } else
        {
            return OperatorBase.ERROR.MESSAGE_IS_ERROR;
        }

        return OperatorBase.ERROR.SUCCESS;
    }

    /**
     * 进行更新邮件消息标签
     *
     * @param incr 增量
     */
    public void updateMessageFlag(int incr)
    {
        if (incr == 0)
            return;
        long value = userCache.hincrBy(getRedisField(), incr);
        if (value <= 0)
            userCache.hDel(getRedisField());
    }

    /**
     * 进行更新公告消息标签
     *
     * @param data 收取公告编号
     */
    private void updateNoticeFlag(Vector<Integer> data)
    {
        userCache.hSet(getNoticeRedisField(), XwhTool.getJSONByFastJSON(data));
    }

    /**
     * 获取玩家收取公告标签
     */
    private Vector<Integer> getUserNoticeFlag()
    {
        String json = userCache.getValue(getNoticeRedisField());
        Vector<Integer> data = XwhTool.parseJSONString(json, new TypeToken<Vector<Integer>>()
        {
        }.getType());
        if (data == null)
            data = new Vector<>();
        return data;
    }

    /**
     * 进行初始化调用
     */
    public void init()
    {
        Vector<Integer> history = getUserNoticeFlag();
        Vector<Notice> notices = getUserNotice(history);
        if (notices != null && !notices.isEmpty())
        {
            notices.forEach(notice ->
            {
                if (null == notice)
                    return;
                String userList = Objects.toString(notice.getUserList(), "");
                switch (notice.getInclude())
                {
                    case -1:
                    {
                        history.add(addNoticeMessage(notice));
                    }
                    break;
                    case 0:
                    {
                        String[] args = userList.split(",");
                        if (!Arrays.asList(args).contains(userCache.getUserId()))
                        {
                            history.add(addNoticeMessage(notice));
                        }
                    }
                    break;
                    case 1:
                    {
                        String[] args = userList.split(",");
                        if (Arrays.asList(args).contains(userCache.getUserId()))
                        {
                            history.add(addNoticeMessage(notice));
                        }
                    }
                    break;
                    default:
                        break;
                }
            });
            updateNoticeFlag(history);
        }
    }

    /**
     * 添加公告消息
     *
     * @param notice 公告
     */
    private int addNoticeMessage(Notice notice)
    {
        updateEmail(null, OperatorBase.MessageType.system.name(), notice.getIcon(), notice.getTitle(), notice.getMsg(), notice.getReward(), notice.getButton(), notice.getLeaveType(), new Timestamp(System.currentTimeMillis()));
        return notice.getId();
    }

    /**
     * 更新email数据
     *
     * @param parameters 参数数据
     * @param type       邮件类型
     * @param icon       邮件ICON
     * @param title      邮件标题
     * @param msg        邮件内容
     * @param reward     邮件奖励
     * @param button     邮件button
     * @param leave      邮件前往类型
     * @param times      邮件产生时间
     */
    private void updateEmail(String parameters, String type, String icon, String title, String msg, String reward, String button, String leave, Timestamp times)
    {
        try
        {
            if (userCache == null)
                return;
            Email email = new Email(type, userCache.userId(), icon, title, msg, reward, button, leave, times);
            email.setParameters(parameters);
            updateMessageFlag(1);
            FishInfoDao.instance().saveOrUpdate(email, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }
}
