package com.code.servlet.operate;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.code.service.book.BookInitService;
import com.code.servlet.base.ServletMain;
import com.code.servlet.operate.ext.ActionTypeExt;
import com.code.servlet.operate.ext.ActionTypeExt.RequestImpl;
import com.code.servlet.operate.ext.ActionTypeExt.ResponseImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

@AvoidRepeatableCommit(filter = false, timeout = 1000)
@WebServlet(urlPatterns = "/operator/ActionType")
public class ActionTypeServlet extends ServletMain<RequestImpl, ResponseImpl>
{
    @Override
    protected ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = UserCache.getUserCache(String.valueOf(req.userid));

        ResponseImpl res = new ResponseImpl();
        res.status = true;
        res.code = STATUS_SUCCESS;
        if (Systemstatusinfo.getBoolean("sys_status"))
        {
            res.msg = "正式环境，功能禁止";
            res.code = 404;
            return res;
        }
        if (userCache == null)
        {
            res.msg = "玩家不存在，请确认参数再来！";
            return res;
        }
        if (req.entities == null || req.entities.isEmpty())
        {
            res.msg = "没有操作节点！";
            return res;
        }
        for (ActionTypeExt.OperateEntity entity : req.entities)
        {
            switch (entity.type)
            {
                //贝壳添加
                case shell:
                    userCache.incrShell(((Double) entity.value).longValue());
                    break;
                case book:
                {
                    ActionTypeExt.BookEntity bookEntity = (ActionTypeExt.BookEntity) entity.value;
                    if (bookEntity == null || bookEntity.books == null)
                        return res;
                    BookInitService bookService = new BookInitService(userCache);
                    bookEntity.books.forEach(book -> bookService.updateUserBook(book, BookInitService.BookType.open));
                }
                break;
                case basin:
                {
                    int basinId = ((Double) entity.value).intValue();
                    ConfigBasin config = (ConfigBasin) FishInfoDb.instance().getCacheKey(ConfigBasin.class, new String[]{"basinId", String.valueOf(basinId)});
                    BookInitService bookService = new BookInitService(userCache);
                    userCache.setBasinData(config.getBasinId(), config.getArchiveName());
                    Vector<Integer> otherWaterLevelBook = bookService.getOtherWaterLevelBook(basinId);
                    if (otherWaterLevelBook != null && !otherWaterLevelBook.isEmpty())
                    {
                        for (Integer bookId : otherWaterLevelBook)
                        {
                            bookService.updateUserBook(bookId, BookInitService.BookType.nothing);
                        }
                    }
                }
                break;
                default:
                    break;
            }
        }
        return res;
    }
}
