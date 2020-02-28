package com.code.servlet.operate;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.servlet.base.ServletMain;
import com.code.servlet.operate.ext.Refresh;
import com.utils.db.RedisUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

@AvoidRepeatableCommit(filter = false, timeout = 30000)
@WebServlet(urlPatterns = "/operate/refresh")
public class RefreshServlet extends ServletMain<Refresh.Request, Refresh.Response>
{

    @Override
    protected Refresh.Response doLogic(Refresh.Request req, HttpServletRequest request, HttpServletResponse response)
    {
        if (Systemstatusinfo.getBoolean("sys_status"))
        {
            Refresh.Response res = new Refresh.Response();
            res.msg = "正式环境，功能禁止";
            res.code = 404;
            return res;
        }
        String SQL = "SELECT TABLE_NAME as tn FROM information_schema.COLUMNS WHERE COLUMN_NAME = 'userId' AND TABLE_SCHEMA ='fishv5'";
        Vector<Refresh.TableName> tableNames = FishInfoDb.instance().findBySQL(SQL, Refresh.TableName.class);
        Vector<String> sqlList = new Vector<>();
        tableNames.forEach(tableName ->
        {
            String tn = tableName.table;
            if (req.isall)
            {
                sqlList.add("truncate table " + tn);
            } else
            {
                StringBuilder users = new StringBuilder();
                if (req.userids.size() > 1)
                    users.append(" in (");
                else
                    users.append(" =");
                int len = users.length();
                req.userids.forEach(id ->
                {
                    if (users.length() != len)
                    {
                        users.append(",");
                    }
                    users.append(id);
                    String key = UserCache.getUserKey(String.valueOf(id));
                    STAR.warn("delete redis " + key);
                    RedisUtils.del(key);
                });
                if (req.userids.size() > 1)
                {
                    users.append(")");
                }
                sqlList.add("delete from " + tn + " where userId" + users.toString());

            }
        });
        sqlList.forEach(sql -> FishInfoDb.instance().addQueue(sql));
        if (req.isall)
        {
            RedisUtils.flushdb();
        }
        Refresh.Response res = new Refresh.Response();
        res.status = true;
        res.code = STATUS_SUCCESS;
        return res;
    }
}
