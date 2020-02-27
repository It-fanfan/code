package com.code.dao.db;

import com.code.dao.entity.achievement.ConfigAchievement;
import com.code.dao.entity.achievement.ConfigAchievementType;
import com.code.dao.entity.achievement.ConfigProp;
import com.code.dao.entity.fish.config.*;
import com.code.dao.entity.goods.GoodsDaily;
import com.code.dao.entity.goods.GoodsValue;
import com.code.dao.entity.message.Lamp;
import com.code.dao.entity.message.Notice;
import com.code.dao.entity.work.WorkActivity;
import com.code.dao.entity.work.WorkSign;
import com.code.dao.entity.work.WorkTurntable;
import com.code.dao.entity.work.WorkWeekActivity;
import com.utils.db.XWHResultSetMapper;
import com.utils.db.druid.DBPoolConnection;

import java.util.Vector;

public class FishInfoDb extends XWHResultSetMapper
{
    private static FishInfoDb instance;

    static
    {
        instance = new FishInfoDb();
    }

    private FishInfoDb()
    {
        try
        {
            initCache();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static FishInfoDb instance()
    {
        return instance;
    }

    @Override
    public String alias()
    {
        return DBPoolConnection.DB_ALIAS.DB_FISH_ALIAS;
    }

    @Override
    public Vector<Class<?>> getCacheClass()
    {
        Vector<Class<?>> vector = new Vector<>();
        vector.add(Config_version.class);
        vector.add(ConfigFish.class);
        vector.add(ConfigInvite.class);
        vector.add(ConfigBasin.class);
        vector.add(ConfigShare.class);
        vector.add(Systemstatusinfo.class);
        vector.add(ConfigOrderIntroduce.class);
        vector.add(ConfigOrderBoss.class);
        vector.add(ConfigEmail.class);
        vector.add(ConfigNotice.class);
        vector.add(Lamp.class);
        vector.add(Notice.class);
        vector.add(ConfigMaterial.class);
        vector.add(ConfigProp.class);
        vector.add(ConfigAchievement.class);
        vector.add(ConfigAchievementType.class);
        //shop
        vector.add(GoodsValue.class);
        vector.add(GoodsDaily.class);
        //work
        vector.add(WorkActivity.class);
        vector.add(WorkSign.class);
        vector.add(WorkWeekActivity.class);
        vector.add(WorkTurntable.class);
        return vector;
    }
}
