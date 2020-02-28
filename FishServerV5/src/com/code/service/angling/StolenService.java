package com.code.service.angling;

import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.userinfo.UserSteal;
import com.code.dao.entity.record.RecordSteal;

class StolenService
{

    StolenService()
    {

    }

    /**
     * 增删被偷记录数据
     *
     * @param recordSteal 单个邮件
     */
    static void setUserStolenDetail(RecordSteal recordSteal) throws Exception
    {
        int book = Integer.valueOf(recordSteal.getValue());
        UserSteal steal = new UserSteal();
        steal.setFtId(book);
        steal.setStealUserId(recordSteal.getStealUserId());
        steal.setUpdateTime(recordSteal.getUpdateTime());
        steal.setUserId(recordSteal.getUserId());
        FishInfoDb.instance().saveOrUpdate(steal, true);
    }
}
