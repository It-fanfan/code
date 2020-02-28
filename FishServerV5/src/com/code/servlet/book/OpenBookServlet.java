package com.code.servlet.book;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.dao.entity.record.RecordBook;
import com.code.dao.use.FishInfoDao;
import com.code.protocols.core.AnglingBase;
import com.code.protocols.core.AnglingBase.ERROR;
import com.code.protocols.core.book.OpenBookExt.RequestImpl;
import com.code.protocols.core.book.OpenBookExt.ResponseImpl;
import com.code.service.book.BookInitService;
import com.code.servlet.base.ServletMain;
import com.utils.XWHMathTool;
import com.utils.log4j.Log4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 * 开图鉴
 */

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/book/open", name = "com.code.protocols.core.book.OpenBookExt")
public class OpenBookServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        ResponseImpl res = new ResponseImpl();
        BookInitService service = new BookInitService(userCache);
        int basinId = userCache.getBasin();
        ConfigFish bookConfig = (ConfigFish) FishInfoDb.instance().getCacheKey(ConfigFish.class, new String[]{"ftId", String.valueOf(req.bookid)});
        ERROR openBookCode = ERROR.SUCCESS;
        try
        {
            //不存在图鉴
            if (bookConfig == null)
            {
                openBookCode = ERROR.NO_BOOK;
                return res;
            }
            //不是同水域
            if (basinId != bookConfig.getBasin())
            {
                openBookCode = ERROR.NO_BASIN;
                return res;
            }
            int bookStatus = service.getBookStatus(req.bookid);
            if (bookStatus == -2 || bookStatus == BookInitService.BookType.open.getStatus())
            {
                openBookCode = ERROR.NO_LIGHT;
                return res;
            }
            int price = bookConfig.getLightExpend();
            if (bookStatus == BookInitService.BookType.stolen.getStatus())
            {
                price = XWHMathTool.multiply(price, getBookDamage()).intValue();
            }
            if (userCache.shell() < price)
            {
                openBookCode = ERROR.SHELL_LACKING;
                return res;
            }
            //更新图鉴
            service.updateUserBook(req.bookid, BookInitService.BookType.open);
            userCache.incrShell(-price);
            //进行保存点亮记录
            RecordBook record = new RecordBook();
            record.setUserId(userCache.userId());
            record.setBasinId(basinId);
            ConfigBasin configBasin = (ConfigBasin) FishInfoDb.instance().getCacheKey(ConfigBasin.class, new String[]{"basinId", String.valueOf(basinId)});
            if (configBasin != null)
                record.setBasinName(configBasin.getChineseName());
            record.setBook(bookConfig.getFtId());
            record.setBookName(bookConfig.getTypeName());
            record.setLighten(price);
            record.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            FishInfoDao.instance().saveOrUpdate(record, true);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        } finally
        {
            AnglingBase.updateResponseCode(openBookCode, res);
            res.basin = service.getUserBasin();
            res.setUserValue(userCache);
        }
        return res;
    }

    /**
     * 获取折损
     *
     * @return ..
     */
    private double getBookDamage()
    {
        return Systemstatusinfo.getDouble("book_damage", "0.5");
    }
}
